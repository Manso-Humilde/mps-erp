package com.mps.erp.ars.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mps.erp.accounting.model.Receivable;
import com.mps.erp.accounting.repository.ReceivableRepository;
import com.mps.erp.accounting.service.AccountingService;
import com.mps.erp.ars.dto.ARSInvoiceResponse;
import com.mps.erp.consultation.model.Consultation;
import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.model.*;
import com.mps.erp.fiscal.service.NCFService;
import com.mps.erp.ars.repository.ARSInvoiceRepository;
import com.mps.erp.ars.repository.ARSRepository;
import com.mps.erp.consultation.repository.ConsultationRepository;
import com.mps.erp.repository.PatientRepository;
import com.mps.erp.service.LogoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mps.erp.ars.model.ARSInvoice;
import com.mps.erp.ars.model.ARS;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;

@Service
public class ARSInvoiceService {
    private final ARSInvoiceRepository arsInvoiceRepository;
    private final ConsultationRepository consultationRepository;
    private final ARSRepository arsRepository;
    private final NCFService ncfService;
    private final PatientRepository patientRepository;
    private final LogoService logoService;
    private final AccountingService accountingService;
    private final ReceivableRepository receivableRepository;

    public ARSInvoiceService(ARSInvoiceRepository arsInvoiceRepository, ConsultationRepository consultationRepository, ARSRepository arsRepository, NCFService ncfService, PatientRepository patientRepository, LogoService logoService, AccountingService accountingService, ReceivableRepository receivableRepository) {
        this.arsInvoiceRepository = arsInvoiceRepository;
        this.consultationRepository = consultationRepository;
        this.arsRepository = arsRepository;
        this.ncfService = ncfService;
        this.patientRepository = patientRepository;
        this.logoService = logoService;
        this.accountingService = accountingService;
        this.receivableRepository = receivableRepository;
    }

    @Transactional
    public List<ARSInvoiceResponse> generateMonthlyARSInvoices(int year, int month) {
        Long tenantId = TenantContext.getCurrentTenant();

        LocalDateTime inicio = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime fin = inicio.plusMonths(1).minusSeconds(1);

        List<ARS> arsList = arsRepository.findByTenantId(tenantId);
        List<ARSInvoice> invoicesGeneradas = new ArrayList<>();

        for (ARS ars : arsList) {
            List<Consultation> consultas = consultationRepository.findByTenantIdAndArsIdAndFechaConsultaBetween(
                    tenantId, ars.getId(), inicio, fin
            );

            if (!consultas.isEmpty()) {
                BigDecimal montoBruto = consultas.stream()
                        .map(Consultation::getMontoTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal retencionIsr = montoBruto.multiply(new BigDecimal("0.10"));
                BigDecimal montoNeto = montoBruto.subtract(retencionIsr);

                String ncf = ncfService.consumeNextNCF(tenantId, "B01");

                ARSInvoice invoice = new ARSInvoice();
                invoice.setTenantId(tenantId);
                invoice.setArsId(ars.getId());
                invoice.setNcf(ncf);
                invoice.setMontoBruto(montoBruto);
                invoice.setRetencionIsr(retencionIsr);
                invoice.setMontoNeto(montoNeto);
                invoice.setCantidadConsultas(consultas.size());
                invoice.setPeriodo(String.format("%d-%02d", year, month));
                invoice.setEstadoDgii(DGIIStatus.APROBADA);
                invoice.setFechaEmision(LocalDateTime.now());

                ARSInvoice saved = arsInvoiceRepository.save(invoice);

                // Crear cuenta por cobrar
                Receivable receivable = new Receivable();
                receivable.setTenantId(tenantId);
                receivable.setTipo("ARS");
                receivable.setTerceroId(saved.getArsId());
                receivable.setFacturaId(saved.getId());
                receivable.setFacturaTipo("FACTURA_ARS");
                receivable.setMonto(saved.getMontoNeto());
                receivable.setSaldoPendiente(saved.getMontoNeto());
                receivable.setFechaEmision(LocalDate.now());
                receivable.setFechaVencimiento(LocalDate.now().plusDays(45));
                receivable.setEstado("PENDIENTE");

                receivableRepository.save(receivable);

                accountingService.registrarIngresoARS(
                        saved.getId(),
                        saved.getMontoNeto(),
                        saved.getArsId()
                );

                invoicesGeneradas.add(saved);
            }
        }

        return invoicesGeneradas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ARSInvoiceResponse> getARSInvoicesByDoctor() {
        Long tenantId = TenantContext.getCurrentTenant();
        return arsInvoiceRepository.findByTenantIdOrderByFechaEmisionDesc(tenantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public byte[] generatePdf(Long invoiceId) {
        try {
            ARSInvoice invoice = arsInvoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Factura ARS no encontrada"));

            ARS ars = arsRepository.findById(invoice.getArsId())
                    .orElseThrow(() -> new RuntimeException("ARS no encontrada"));

            String[] periodo = invoice.getPeriodo().split("-");
            int year = Integer.parseInt(periodo[0]);
            int month = Integer.parseInt(periodo[1]);
            LocalDateTime inicio = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime fin = inicio.plusMonths(1).minusSeconds(1);

            List<Consultation> consultas = consultationRepository.findByTenantIdAndArsIdAndFechaConsultaBetween(
                    invoice.getTenantId(), invoice.getArsId(), inicio, fin);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

            Paragraph title = new Paragraph("FACTURA ARS - CRÉDITO FISCAL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.addCell(createCell("NCF: " + invoice.getNcf(), boldFont, Rectangle.NO_BORDER));
            headerTable.addCell(createCell("Fecha: " + invoice.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    normalFont, Rectangle.NO_BORDER, Element.ALIGN_RIGHT));
            document.add(headerTable);
            document.add(new Paragraph(" "));

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{50, 50});

            PdfPCell emisorCell = new PdfPCell();
            emisorCell.setBorder(Rectangle.BOX);
            emisorCell.setPadding(10);
            emisorCell.setBackgroundColor(new Color(250, 250, 250));
            Paragraph emisor = new Paragraph();
            emisor.add(new Chunk("DATOS DEL EMISOR\n", headerFont));
            emisor.add(new Chunk("Nombre: MPS ERP\n", normalFont));
            emisor.add(new Chunk("RNC/Cédula: 000-0000000-0\n", normalFont));
            emisorCell.addElement(emisor);
            infoTable.addCell(emisorCell);

            PdfPCell arsCell = new PdfPCell();
            arsCell.setBorder(Rectangle.BOX);
            arsCell.setPadding(8);
            Paragraph arsInfo = new Paragraph();
            arsInfo.add(new Chunk("DATOS DE LA ARS\n", headerFont));
            arsInfo.add(new Chunk("Nombre: " + ars.getNombre() + "\n", normalFont));
            arsInfo.add(new Chunk("RNC/Cédula: " + (ars.getRnc() != null ? ars.getRnc() : "N/A") + "\n", normalFont));
            arsInfo.add(new Chunk("Periodo: " + invoice.getPeriodo() + "\n", normalFont));
            arsCell.addElement(arsInfo);
            infoTable.addCell(arsCell);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            PdfPTable detailTable = new PdfPTable(5);
            detailTable.setWidthPercentage(100);
            detailTable.setWidths(new float[]{5, 30, 15, 30, 20});
            detailTable.setHeaderRows(1);

            String[] headers = {"#", "PACIENTE", "FECHA", "SERVICIO", "MONTO"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, headerFont));
                headerCell.setBackgroundColor(new Color(200, 200, 200));
                headerCell.setPadding(6);
                detailTable.addCell(headerCell);
            }

            int counter = 1;
            for (Consultation c : consultas) {
                PdfPCell cellPaciente = createCell(String.valueOf(counter), normalFont);
                PdfPCell cellNombre = createCell(getPatientName(c.getPatientId()), normalFont);
                PdfPCell cellFecha = createCell(c.getFechaConsulta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont);

                // Corregido: manejar null en tipoServicio
                String servicioNombre = c.getTipoServicio() != null ? c.getTipoServicio().getNombre() : "No especificado";
                PdfPCell cellServicio = createCell(servicioNombre, normalFont);

                PdfPCell cellMonto = createCell(String.format("RD$ %.2f", c.getMontoTotal()), normalFont, Element.ALIGN_RIGHT);

                if (counter % 2 == 0) {
                    Color lightGray = new Color(240, 240, 240);
                    cellPaciente.setBackgroundColor(lightGray);
                    cellNombre.setBackgroundColor(lightGray);
                    cellFecha.setBackgroundColor(lightGray);
                    cellServicio.setBackgroundColor(lightGray);
                    cellMonto.setBackgroundColor(lightGray);
                }

                detailTable.addCell(cellPaciente);
                detailTable.addCell(cellNombre);
                detailTable.addCell(cellFecha);
                detailTable.addCell(cellServicio);
                detailTable.addCell(cellMonto);
                counter++;
            }

            document.add(detailTable);
            document.add(new Paragraph(" "));

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTotalRow(totalTable, "Monto Bruto:", String.format("RD$ %.2f", invoice.getMontoBruto()), normalFont);
            addTotalRow(totalTable, "Retención ISR (10%):", String.format("RD$ %.2f", invoice.getRetencionIsr()), normalFont);
            addTotalRow(totalTable, "Monto Neto:", String.format("RD$ %.2f", invoice.getMontoNeto()), boldFont);

            document.add(totalTable);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private PdfPCell createCell(String text, Font font) {
        return createCell(text, font, Rectangle.BOX, Element.ALIGN_LEFT);
    }

    private PdfPCell createCell(String text, Font font, int border) {
        return createCell(text, font, border, Element.ALIGN_LEFT);
    }

    private PdfPCell createCell(String text, Font font, int border, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setPadding(4);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private void addTotalRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private String getPatientName(Long patientId) {
        return patientRepository.findById(patientId)
                .map(Patient::getNombreCompleto)
                .orElse("Desconocido");
    }

    private ARSInvoiceResponse convertToResponse(ARSInvoice invoice) {
        String arsNombre = arsRepository.findById(invoice.getArsId())
                .map(ARS::getNombre)
                .orElse("ARS Desconocida");

        return new ARSInvoiceResponse(
                invoice.getId(),
                invoice.getTenantId(),
                invoice.getArsId(),
                arsNombre,
                invoice.getNcf(),
                invoice.getMontoBruto().doubleValue(),
                invoice.getRetencionIsr().doubleValue(),
                invoice.getMontoNeto().doubleValue(),
                invoice.getCantidadConsultas(),
                invoice.getPeriodo(),
                invoice.getEstadoDgii(),
                invoice.getMensajeDgii(),
                invoice.getFechaEmision()
        );
    }

    public List<ARSInvoiceResponse> getARSInvoicesByDoctorAndARS(Long arsId) {
        Long tenantId = TenantContext.getCurrentTenant();
        return arsInvoiceRepository.findByTenantIdAndArsId(tenantId, arsId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
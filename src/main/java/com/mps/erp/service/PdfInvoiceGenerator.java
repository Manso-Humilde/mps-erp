package com.mps.erp.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.mps.erp.model.Patient;
import com.mps.erp.model.PatientInvoice;
import com.mps.erp.consultation.model.Consultation;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfInvoiceGenerator {

    public byte[] generatePatientInvoice(PatientInvoice invoice, Patient patient, Consultation consultation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

            Paragraph title = new Paragraph("FACTURA DE PACIENTE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{50, 50});

            PdfPCell cell = new PdfPCell(new Phrase("NCF: " + invoice.getNcf(), boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(cell);

            String fechaStr = invoice.getFechaEmision() != null ?
                    invoice.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
            cell = new PdfPCell(new Phrase("Fecha: " + fechaStr, normalFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(cell);

            document.add(headerTable);
            document.add(new Paragraph(" "));

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{50, 50});

            PdfPCell emisorCell = new PdfPCell();
            emisorCell.setBorder(Rectangle.BOX);
            emisorCell.setPadding(8);
            Paragraph emisor = new Paragraph();
            emisor.add(new Chunk("DATOS DEL EMISOR\n", headerFont));
            emisor.add(new Chunk("Nombre: MPS ERP\n", normalFont));
            emisor.add(new Chunk("RNC/Cédula: 000-0000000-0\n", normalFont));
            emisor.add(new Chunk("Teléfono: 809-000-0000\n", normalFont));
            emisor.add(new Chunk("Dirección: Calle Principal 123\n", normalFont));
            emisorCell.addElement(emisor);
            infoTable.addCell(emisorCell);

            PdfPCell clienteCell = new PdfPCell();
            clienteCell.setBorder(Rectangle.BOX);
            clienteCell.setPadding(8);
            Paragraph cliente = new Paragraph();
            cliente.add(new Chunk("DATOS DEL CLIENTE\n", headerFont));
            cliente.add(new Chunk("Nombre: " + patient.getNombreCompleto() + "\n", normalFont));
            cliente.add(new Chunk("RNC/Cédula: " + patient.getNumeroIdentificacion() + "\n", normalFont));
            String telefono = patient.getTelefono() != null ? patient.getTelefono() : "N/A";
            cliente.add(new Chunk("Teléfono: " + telefono + "\n", normalFont));
            String direccion = patient.getDireccion() != null ? patient.getDireccion() : "N/A";
            cliente.add(new Chunk("Dirección: " + direccion + "\n", normalFont));
            clienteCell.addElement(cliente);
            infoTable.addCell(clienteCell);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            PdfPTable detailTable = new PdfPTable(4);
            detailTable.setWidthPercentage(100);
            detailTable.setWidths(new float[]{10, 40, 25, 25});
            detailTable.setHeaderRows(1);

            String[] headers = {"CANTIDAD", "DESCRIPCIÓN", "SUB-TOTAL", "TOTAL"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, headerFont));
                headerCell.setPadding(6);
                detailTable.addCell(headerCell);
            }

            detailTable.addCell(new PdfPCell(new Phrase("1", normalFont)));
            String descripcion = "Consulta General";
            detailTable.addCell(new PdfPCell(new Phrase(descripcion, normalFont)));
            double monto = invoice.getMontoTotal() != null ? invoice.getMontoTotal().doubleValue() : 0.0;
            detailTable.addCell(new PdfPCell(new Phrase(String.format("RD$ %.2f", monto), normalFont)));
            detailTable.addCell(new PdfPCell(new Phrase(String.format("RD$ %.2f", monto), normalFont)));

            document.add(detailTable);
            document.add(new Paragraph(" "));

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{50, 50});

            addTotalRow(totalTable, "Sub-Total Exento:", "RD$ 0.00", normalFont);
            addTotalRow(totalTable, "Sub-Total Gravado:", String.format("RD$ %.2f", monto), normalFont);
            addTotalRow(totalTable, "ITBIS (18%):", "RD$ 0.00", normalFont);
            addTotalRow(totalTable, "TOTAL:", String.format("RD$ %.2f", monto), boldFont);

            document.add(totalTable);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }

        return baos.toByteArray();
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
}
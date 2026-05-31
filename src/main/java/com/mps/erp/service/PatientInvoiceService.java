package com.mps.erp.service;

import com.mps.erp.accounting.model.Receivable;
import com.mps.erp.accounting.repository.ReceivableRepository;
import com.mps.erp.accounting.service.AccountingService;
import com.mps.erp.dto.PatientInvoiceResponse;
import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.model.*;
import com.mps.erp.fiscal.service.NCFService;
import com.mps.erp.model.Patient;
import com.mps.erp.consultation.repository.ConsultationRepository;
import com.mps.erp.repository.PatientInvoiceRepository;
import com.mps.erp.repository.PatientRepository;
import com.mps.erp.consultation.model.Consultation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientInvoiceService {
    private final PatientInvoiceRepository patientInvoiceRepository;
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final NCFService ncfService;
    private final PdfInvoiceGenerator pdfInvoiceGenerator;
    private final AccountingService accountingService;
    private final ReceivableRepository receivableRepository;

    public PatientInvoiceService(PatientInvoiceRepository patientInvoiceRepository, ConsultationRepository consultationRepository, PatientRepository patientRepository, NCFService ncfService, PdfInvoiceGenerator pdfInvoiceGenerator, AccountingService accountingService, ReceivableRepository receivableRepository) {
        this.patientInvoiceRepository = patientInvoiceRepository;
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.ncfService = ncfService;
        this.pdfInvoiceGenerator = pdfInvoiceGenerator;
        this.accountingService = accountingService;
        this.receivableRepository = receivableRepository;
    }

    @Transactional
    public PatientInvoiceResponse createPatientInvoice(Long consultationId) {
        Long tenantId = TenantContext.getCurrentTenant();

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Patient patient = patientRepository.findById(consultation.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Determinar tipo de NCF y monto a facturar
        String ncfType;
        Double montoFacturar;

        if (patient.getArsId() != null && patient.getArsId() > 0) {
            // Paciente con ARS -> Tipo B01 (Crédito fiscal) - Facturar monto total
            ncfType = "B01";
            montoFacturar = consultation.getMontoTotal().doubleValue();
        } else {
            // Paciente sin ARS -> Tipo E32 (Consumidor final) - Facturar copago
            ncfType = "E32";
            montoFacturar = consultation.getCopagoPaciente().doubleValue();
        }

        String ncf = ncfService.consumeNextNCF(tenantId, ncfType);

        PatientInvoice invoice = new PatientInvoice();
        invoice.setTenantId(tenantId);
        invoice.setPatientId(consultation.getPatientId());
        invoice.setConsultationId(consultationId);
        invoice.setNcf(ncf);
        invoice.setMontoTotal(java.math.BigDecimal.valueOf(montoFacturar));
        invoice.setEstadoDgii(DGIIStatus.APROBADA);
        invoice.setMensajeDgii("Aceptado por DGII");

        PatientInvoice saved = patientInvoiceRepository.save(invoice);

        // Crear cuenta por cobrar
        Receivable receivable = new Receivable();
        receivable.setTenantId(tenantId);
        receivable.setTipo("PACIENTE");
        receivable.setTerceroId(invoice.getPatientId());
        receivable.setFacturaId(saved.getId());
        receivable.setFacturaTipo("FACTURA_PACIENTE");
        receivable.setMonto(invoice.getMontoTotal());
        receivable.setSaldoPendiente(invoice.getMontoTotal());
        receivable.setFechaEmision(LocalDate.now());
        receivable.setFechaVencimiento(LocalDate.now().plusDays(30)); // Vence en 30 días
        receivable.setEstado("PENDIENTE");

        receivableRepository.save(receivable);

        // Registrar asiento contable
        accountingService.registrarIngresoConsulta(
                saved.getConsultationId(),
                java.math.BigDecimal.valueOf(montoFacturar),
                saved.getPatientId()
        );

        return convertToResponse(saved);
    }

    public byte[] generatePdf(Long invoiceId) {
        PatientInvoice invoice = patientInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Patient patient = patientRepository.findById(invoice.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Consultation consultation = consultationRepository.findById(invoice.getConsultationId())
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        return pdfInvoiceGenerator.generatePatientInvoice(invoice, patient, consultation);
    }

    public List<PatientInvoiceResponse> getInvoicesByDoctor() {
        Long tenantId = TenantContext.getCurrentTenant();
        return patientInvoiceRepository.findByTenantIdOrderByFechaEmisionDesc(tenantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PatientInvoiceResponse convertToResponse(PatientInvoice invoice) {
        return new PatientInvoiceResponse(
                invoice.getId(),
                invoice.getTenantId(),
                "Doctor Enterprise",
                invoice.getPatientId(),
                "Paciente",
                invoice.getConsultationId(),
                invoice.getNcf(),
                invoice.getMontoTotal().doubleValue(),
                invoice.getMontoItbis().doubleValue(),
                invoice.getMontoNeto().doubleValue(),
                invoice.getTipoPagador(),
                invoice.getEstadoDgii(),
                invoice.getMensajeDgii(),
                invoice.getFechaEmision()
        );
    }
}
package com.mps.erp.controller;

import com.mps.erp.dto.PatientInvoiceResponse;
import com.mps.erp.service.PatientInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices/patient")
public class PatientInvoiceController {
    private final PatientInvoiceService patientInvoiceService;

    public PatientInvoiceController(PatientInvoiceService patientInvoiceService) {
        this.patientInvoiceService = patientInvoiceService;
    }

    @PostMapping("/{consultationId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<PatientInvoiceResponse> createPatientInvoice(@PathVariable Long consultationId) {
        return ResponseEntity.ok(patientInvoiceService.createPatientInvoice(consultationId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<List<PatientInvoiceResponse>> getPatientInvoices() {
        return ResponseEntity.ok(patientInvoiceService.getInvoicesByDoctor());
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdf = patientInvoiceService.generatePdf(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=factura-paciente-" + id + ".pdf")
                .body(pdf);
    }
}
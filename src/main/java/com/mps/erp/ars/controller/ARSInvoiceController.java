package com.mps.erp.ars.controller;

import com.mps.erp.ars.dto.ARSInvoiceResponse;
import com.mps.erp.ars.service.ARSInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices/ars")
public class ARSInvoiceController {

    private final ARSInvoiceService arsInvoiceService;

    public ARSInvoiceController(ARSInvoiceService arsInvoiceService) {
        this.arsInvoiceService = arsInvoiceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO')")
    public ResponseEntity<List<ARSInvoiceResponse>> getARSInvoices() {
        List<ARSInvoiceResponse> invoices = arsInvoiceService.getARSInvoicesByDoctor();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/ars/{arsId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO')")
    public ResponseEntity<List<ARSInvoiceResponse>> getARSInvoicesByARS(@PathVariable Long arsId) {
        List<ARSInvoiceResponse> invoices = arsInvoiceService.getARSInvoicesByDoctorAndARS(arsId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdf = arsInvoiceService.generatePdf(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=factura-ars-" + id + ".pdf")
                .body(pdf);
    }

    @PostMapping("/generar-reporte")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO')")
    public ResponseEntity<List<ARSInvoiceResponse>> generarReporteMensual(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(arsInvoiceService.generateMonthlyARSInvoices(year, month));
    }
}
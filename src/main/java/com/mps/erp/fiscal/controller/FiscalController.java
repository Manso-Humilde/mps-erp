package com.mps.erp.fiscal.controller;

import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.fiscal.model.FiscalDocument;
import com.mps.erp.fiscal.service.ElectronicInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.mps.erp.fiscal.repository.FiscalDocumentRepository;
import java.util.List;

@RestController
@RequestMapping("/api/fiscal")
public class FiscalController {
    private final ElectronicInvoiceService invoiceService;
    private final FiscalDocumentRepository fiscalRepository;

    public FiscalController(ElectronicInvoiceService invoiceService, FiscalDocumentRepository fiscalRepository) {
        this.invoiceService = invoiceService;
        this.fiscalRepository = fiscalRepository;
    }

    @PostMapping("/invoices")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE')")
    public ResponseEntity<FiscalDocument> createInvoice(@RequestBody ElectronicInvoiceService.InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.issueInvoice(request));
    }

    @GetMapping("/invoices")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE')")
    public ResponseEntity<List<FiscalDocument>> getMyInvoices() {
        return ResponseEntity.ok(fiscalRepository.findByTenantIdOrderByFechaEmisionDesc(TenantContext.getCurrentTenant()));
    }
}

package com.mps.erp.fiscal.service;

import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.fiscal.model.FiscalDocument;
import com.mps.erp.fiscal.service.NCFService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mps.erp.fiscal.repository.FiscalDocumentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ElectronicInvoiceService {
    private final NCFService ncfService;
    private final FiscalDGIIService dgiiService;
    private final FiscalDocumentRepository fiscalRepository;

    public ElectronicInvoiceService(NCFService ncfService, FiscalDGIIService dgiiService, FiscalDocumentRepository fiscalRepository) {
        this.ncfService = ncfService;
        this.dgiiService = dgiiService;
        this.fiscalRepository = fiscalRepository;
    }

    @Transactional
    public FiscalDocument issueInvoice(InvoiceRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();
        String ncf = ncfService.consumeNextNCF(tenantId, request.tipoEcf());

        FiscalDocument document = new FiscalDocument();
        document.setTenantId(tenantId);
        document.setNcf(ncf);
        document.setTipoEcf(request.tipoEcf());
        document.setRncEmisor(request.rncEmisor());
        document.setRncReceptor(request.rncReceptor());
        document.setMontoBruto(request.amount());
        document.setMontoExento(request.amount());
        document.setMontoTotal(request.amount());
        document.setFechaEmision(LocalDateTime.now());

        FiscalDocument savedDoc = fiscalRepository.save(document);
        FiscalDGIIService.DGIIResponse response = dgiiService.submitElectronicInvoice(savedDoc);
        
        savedDoc.setEstadoDgii(response.status());
        savedDoc.setTrackId(response.trackId());
        savedDoc.setMensajeDgii(response.message());

        return fiscalRepository.save(savedDoc);
    }

    public record InvoiceRequest(
        String tipoEcf, 
        String rncEmisor, 
        String rncReceptor, 
        BigDecimal amount
    ) {}
}

package com.mps.erp.fiscal.service;

import com.mps.erp.fiscal.model.FiscalDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("fiscalDGIIService")
public class FiscalDGIIService {
    private static final Logger log = LoggerFactory.getLogger(FiscalDGIIService.class);

    public DGIIResponse submitElectronicInvoice(FiscalDocument document) {
        log.info("Submitting e-CF {} to DGII", document.getNcf());
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        boolean success = Math.random() < 0.98;
        if (success) {
            return new DGIIResponse(
                "APROBADA", 
                "E-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "Documento aceptado satisfactoriamente por DGII."
            );
        } else {
            return new DGIIResponse(
                "RECHAZADA", 
                null, 
                "Error 403: El RNC del receptor no es válido para este tipo de e-CF."
            );
        }
    }

    public record DGIIResponse(String status, String trackId, String message) {}
}
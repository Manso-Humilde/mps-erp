package com.mps.erp.fiscal.service;

import com.mps.erp.ars.model.ARSInvoice;
import com.mps.erp.model.DGIIStatus;
import com.mps.erp.model.PatientInvoice;
import com.mps.erp.ars.repository.ARSInvoiceRepository;
import com.mps.erp.repository.PatientInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DGIISchedulerWorker {

    private final PatientInvoiceRepository patientInvoiceRepository;
    private final ARSInvoiceRepository arsInvoiceRepository;
    private final DGIIService dgiiService;

    public DGIISchedulerWorker(PatientInvoiceRepository patientInvoiceRepository,
                               ARSInvoiceRepository arsInvoiceRepository,
                               DGIIService dgiiService) {
        this.patientInvoiceRepository = patientInvoiceRepository;
        this.arsInvoiceRepository = arsInvoiceRepository;
        this.dgiiService = dgiiService;
    }

    // Ejecutar cada 30 segundos
    @Scheduled(fixedDelay = 30000)
    public void processPendingInvoices() {
        // --- Procesar Facturas de Pacientes ---
        List<PatientInvoice> pendingPatientInvoices = patientInvoiceRepository.findByEstadoDgii(DGIIStatus.PENDIENTE_ENVIO);
        
        for (PatientInvoice invoice : pendingPatientInvoices) {
            try {
                String signedXml = dgiiService.signXML(invoice.getXmlEf(), invoice.getDoctorId());
                String trackId = dgiiService.sendToDGII(signedXml);
                invoice.setTrackId(trackId);
                invoice.setEstadoDgii(DGIIStatus.EN_PROCESO);
                patientInvoiceRepository.save(invoice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // --- Procesar Facturas de ARS ---
        List<ARSInvoice> pendingArsInvoices = arsInvoiceRepository.findByEstadoDgii(DGIIStatus.PENDIENTE_ENVIO);
        
        for (ARSInvoice invoice : pendingArsInvoices) {
            try {
                String signedXml = dgiiService.signXML(invoice.getXmlEf(), invoice.getDoctorId());
                String trackId = dgiiService.sendToDGII(signedXml);
                invoice.setTrackId(trackId);
                invoice.setEstadoDgii(DGIIStatus.EN_PROCESO);
                arsInvoiceRepository.save(invoice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Ejecutar cada 1 minuto
    @Scheduled(fixedDelay = 60000)
    public void checkInProcessTrackIds() {
        // --- Pacientes ---
        List<PatientInvoice> inProcessPatientInvoices = patientInvoiceRepository.findByEstadoDgii(DGIIStatus.EN_PROCESO);
        
        for (PatientInvoice invoice : inProcessPatientInvoices) {
            String status = dgiiService.checkTrackIdStatus(invoice.getTrackId());
            
            if ("APROBADA".equals(status)) {
                invoice.setEstadoDgii(DGIIStatus.APROBADA);
                invoice.setMensajeDgii("Factura aprobada exitosamente por DGII.");
            } else {
                invoice.setEstadoDgii(DGIIStatus.RECHAZADA);
                invoice.setMensajeDgii("Rechazada por la DGII. Revise los errores sintácticos.");
            }
            patientInvoiceRepository.save(invoice);
        }

        // --- ARS ---
        List<ARSInvoice> inProcessArsInvoices = arsInvoiceRepository.findByEstadoDgii(DGIIStatus.EN_PROCESO);
        
        for (ARSInvoice invoice : inProcessArsInvoices) {
            String status = dgiiService.checkTrackIdStatus(invoice.getTrackId());
            
            if ("APROBADA".equals(status)) {
                invoice.setEstadoDgii(DGIIStatus.APROBADA);
                invoice.setMensajeDgii("Factura aprobada exitosamente por DGII.");
            } else {
                invoice.setEstadoDgii(DGIIStatus.RECHAZADA);
                invoice.setMensajeDgii("Rechazada por la DGII. Revise los errores sintácticos.");
            }
            arsInvoiceRepository.save(invoice);
        }
    }
}

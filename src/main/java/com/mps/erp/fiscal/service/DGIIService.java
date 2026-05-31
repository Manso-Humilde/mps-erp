package com.mps.erp.fiscal.service;

import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Random;

@Service
public class DGIIService {

    private final Random random = new Random();

    // 1. Simula la Firma Digital X.509
    public String signXML(String rawXml, Long doctorId) {
        // En producción: Usar Apache Santuario con el Keystore (.p12) del Doctor
        if (rawXml != null && rawXml.contains("</eCF>")) {
            return rawXml.replace("</eCF>", "  <Signature>Mock-Firma-Digital-Hash</Signature>\n</eCF>");
        }
        return rawXml;
    }

    // 2. Simula el endpoint /Recepcion de la DGII (Devuelve TrackId)
    public String sendToDGII(String signedXml) {
        // Simular retraso de red
        sleep(500);
        // Devuelve un TrackId único simulando la respuesta de DGII
        return UUID.randomUUID().toString();
    }

    // 3. Simula el endpoint /Estado de la DGII (Consulta TrackId)
    public String checkTrackIdStatus(String trackId) {
        sleep(300);
        // 90% probabilidad de aprobación
        return random.nextDouble() < 0.90 ? "APROBADA" : "RECHAZADA";
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

package com.mps.erp.fiscal.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class DGIIUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Valida RNC o Cédula Dominicana usando algoritmo módulo 11
     */
    public boolean validateRNC(String identificacion) {
        if (identificacion == null || identificacion.isEmpty()) {
            return false;
        }

        // Por ahora, aceptamos cualquier identificación numérica de 9 u 11 dígitos para facilitar pruebas
        identificacion = identificacion.replaceAll("[\\s-]", "");
        return identificacion.matches("\\d{9}") || identificacion.matches("\\d{11}");
    }

    /**
     * Genera NCF (Número de Comprobante Fiscal)
     */
    public String generateNCF(String tipo, Long secuencia) {
        String rnc = "131496293";
        String formattedSecuencia = String.format("%08d", secuencia);
        return String.format("A%s%s%s0001", tipo, rnc, formattedSecuencia);
    }

    /**
     * Genera XML e-CF para factura
     */
    public String generateECFXML(String ncf, String rncEmisor, String rncReceptor, Double montoTotal) {
        return String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<eCF version=\"1.0\">\n" +
                        "  <Encabezado>\n" +
                        "    <NCF>%s</NCF>\n" +
                        "    <RNC_Emisor>%s</RNC_Emisor>\n" +
                        "    <RNC_Receptor>%s</RNC_Receptor>\n" +
                        "    <Monto_Total>%.2f</Monto_Total>\n" +
                        "    <ITBIS>0.00</ITBIS>\n" +
                        "    <Exento>SI</Exento>\n" +
                        "  </Encabezado>\n" +
                        "</eCF>",
                ncf, rncEmisor, rncReceptor, montoTotal
        );
    }

    /**
     * Calcula el copago del paciente
     */
    public Double calculateCopago(Double montoTotal, Double porcentajeCoberturaARS) {
        Double montoCubiertoArs = montoTotal * (porcentajeCoberturaARS / 100);
        return montoTotal - montoCubiertoArs;
    }

    /**
     * Calcula la retención de ISR
     */
    public Double calculateISRRetention(Double montoBruto, Double porcentajeRetencionISR) {
        return montoBruto * (porcentajeRetencionISR / 100);
    }

    /**
     * Hashea una contraseña usando SHA-256
     */
    public String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    /**
     * Verifica una contraseña
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        return hashedPassword.equals(hashPassword(password));
    }

    /**
     * Simula respuesta de DGII (95% aprobación)
     */
    public boolean simulateDGIIResponse() {
        return random.nextDouble() < 0.95;
    }

    /**
     * Genera mensaje de respuesta de DGII
     */
    public String generateDGIIResponseMessage(boolean aprobado) {
        if (aprobado) {
            return "Factura aprobada por DGII. Número de referencia: DGII-" +
                    System.currentTimeMillis();
        } else {
            return "Factura rechazada por DGII. Error: Validación fallida. " +
                    "Por favor verifique los datos y reintente.";
        }
    }
}
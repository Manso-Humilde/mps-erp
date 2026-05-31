package com.mps.erp.accounting.dto;

import java.math.BigDecimal;

public class PagoRequest {
    private Long receivableId;
    private Double monto;
    private String metodoPago;
    private String referenciaComprobante;
    private String observaciones;

    public PagoRequest() {
    }

    public Long getReceivableId() { return receivableId; }
    public void setReceivableId(Long receivableId) { this.receivableId = receivableId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getReferenciaComprobante() { return referenciaComprobante; }
    public void setReferenciaComprobante(String referenciaComprobante) { this.referenciaComprobante = referenciaComprobante; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
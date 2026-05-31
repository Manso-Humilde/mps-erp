package com.mps.erp.accounting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoProveedorRequest {
    private Long payableId;
    private BigDecimal monto;
    private String metodoPago;
    private String referenciaComprobante;
    private String observaciones;

    // Getters y Setters
    public Long getPayableId() { return payableId; }
    public void setPayableId(Long payableId) { this.payableId = payableId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getReferenciaComprobante() { return referenciaComprobante; }
    public void setReferenciaComprobante(String referenciaComprobante) { this.referenciaComprobante = referenciaComprobante; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
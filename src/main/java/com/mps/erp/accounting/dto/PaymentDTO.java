package com.mps.erp.accounting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDTO {
    private LocalDate fecha;
    private String cliente;
    private BigDecimal monto;
    private String metodoPago;
    private String referenciaComprobante;
    private String observaciones;

    public PaymentDTO(LocalDate fecha, String cliente, BigDecimal monto,
                      String metodoPago, String referenciaComprobante, String observaciones) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.referenciaComprobante = referenciaComprobante;
        this.observaciones = observaciones;
    }

    // Getters
    public LocalDate getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public BigDecimal getMonto() { return monto; }
    public String getMetodoPago() { return metodoPago; }
    public String getReferenciaComprobante() { return referenciaComprobante; }
    public String getObservaciones() { return observaciones; }
}
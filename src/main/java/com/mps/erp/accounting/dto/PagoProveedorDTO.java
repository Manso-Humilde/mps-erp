package com.mps.erp.accounting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoProveedorDTO {
    private LocalDate fecha;
    private String proveedor;
    private BigDecimal monto;
    private String metodoPago;
    private String referenciaComprobante;
    private String observaciones;

    public PagoProveedorDTO(LocalDate fecha, String proveedor, BigDecimal monto,
                            String metodoPago, String referenciaComprobante, String observaciones) {
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.referenciaComprobante = referenciaComprobante;
        this.observaciones = observaciones;
    }

    // Getters
    public LocalDate getFecha() { return fecha; }
    public String getProveedor() { return proveedor; }
    public BigDecimal getMonto() { return monto; }
    public String getMetodoPago() { return metodoPago; }
    public String getReferenciaComprobante() { return referenciaComprobante; }
    public String getObservaciones() { return observaciones; }
}
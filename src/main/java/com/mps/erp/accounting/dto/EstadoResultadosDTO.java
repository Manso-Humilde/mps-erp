package com.mps.erp.accounting.dto;

import java.math.BigDecimal;

public class EstadoResultadosDTO {
    private String concepto;
    private BigDecimal monto;
    private String tipo;

    public EstadoResultadosDTO(String concepto, BigDecimal monto, String tipo) {
        this.concepto = concepto;
        this.monto = monto;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
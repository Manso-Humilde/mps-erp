package com.mps.erp.accounting.dto;

import java.math.BigDecimal;

public class ReceivableAntiguedadDTO {
    private Long receivableId;
    private String tipo;
    private String terceroNombre;
    private BigDecimal monto;
    private BigDecimal saldoPendiente;
    private String rangoDias;
    private Integer diasVencidos;

    // Constructor
    public ReceivableAntiguedadDTO(Long receivableId, String tipo, String terceroNombre,
                                   BigDecimal monto, BigDecimal saldoPendiente,
                                   String rangoDias, Integer diasVencidos) {
        this.receivableId = receivableId;
        this.tipo = tipo;
        this.terceroNombre = terceroNombre;
        this.monto = monto;
        this.saldoPendiente = saldoPendiente;
        this.rangoDias = rangoDias;
        this.diasVencidos = diasVencidos;
    }

    // Getters y Setters
    public Long getReceivableId() { return receivableId; }
    public void setReceivableId(Long receivableId) { this.receivableId = receivableId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTerceroNombre() { return terceroNombre; }
    public void setTerceroNombre(String terceroNombre) { this.terceroNombre = terceroNombre; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public String getRangoDias() { return rangoDias; }
    public void setRangoDias(String rangoDias) { this.rangoDias = rangoDias; }

    public Integer getDiasVencidos() { return diasVencidos; }
    public void setDiasVencidos(Integer diasVencidos) { this.diasVencidos = diasVencidos; }
}
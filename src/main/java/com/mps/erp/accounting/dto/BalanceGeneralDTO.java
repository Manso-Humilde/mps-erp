package com.mps.erp.accounting.dto;

import java.math.BigDecimal;

public class BalanceGeneralDTO {
    private String cuenta;
    private String codigo;
    private BigDecimal saldo;
    private String tipo;

    public BalanceGeneralDTO(String cuenta, String codigo, BigDecimal saldo, String tipo) {
        this.cuenta = cuenta;
        this.codigo = codigo;
        this.saldo = saldo;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
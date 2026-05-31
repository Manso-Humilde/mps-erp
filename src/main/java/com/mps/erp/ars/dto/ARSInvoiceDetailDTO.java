package com.mps.erp.ars.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ARSInvoiceDetailDTO {
    private String pacienteNombre;
    private String pacienteIdentificacion;
    private LocalDateTime fechaConsulta;
    private String tipoServicio;
    private BigDecimal montoTotal;

    public ARSInvoiceDetailDTO(String pacienteNombre, String pacienteIdentificacion, 
                               LocalDateTime fechaConsulta, String tipoServicio, 
                               BigDecimal montoTotal) {
        this.pacienteNombre = pacienteNombre;
        this.pacienteIdentificacion = pacienteIdentificacion;
        this.fechaConsulta = fechaConsulta;
        this.tipoServicio = tipoServicio;
        this.montoTotal = montoTotal;
    }

    // Getters
    public String getPacienteNombre() { return pacienteNombre; }
    public String getPacienteIdentificacion() { return pacienteIdentificacion; }
    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public String getTipoServicio() { return tipoServicio; }
    public BigDecimal getMontoTotal() { return montoTotal; }
}
package com.mps.erp.ars.dto;

import com.mps.erp.model.DGIIStatus;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class ARSInvoiceResponse {
    private Long id;
    private Long doctorId;
    private Long arsId;
    private String arsNombre;
    private String ncf;
    private Double montoBruto;
    private Double retencionIsr;
    private Double montoNeto;
    private Integer cantidadConsultas;
    private String periodo;
    private DGIIStatus dgiiStatus;
    private String mensajeDgii;
    private LocalDateTime fechaEmision;

    public ARSInvoiceResponse() {}

    public ARSInvoiceResponse(Long id, Long doctorId, Long arsId, String arsNombre, String ncf, Double montoBruto, Double retencionIsr, Double montoNeto, Integer cantidadConsultas, String periodo, DGIIStatus dgiiStatus, String mensajeDgii, LocalDateTime fechaEmision) {
        this.id = id;
        this.doctorId = doctorId;
        this.arsId = arsId;
        this.arsNombre = arsNombre;
        this.ncf = ncf;
        this.montoBruto = montoBruto;
        this.retencionIsr = retencionIsr;
        this.montoNeto = montoNeto;
        this.cantidadConsultas = cantidadConsultas;
        this.periodo = periodo;
        this.dgiiStatus = dgiiStatus;
        this.mensajeDgii = mensajeDgii;
        this.fechaEmision = fechaEmision;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getArsId() {
        return arsId;
    }

    public void setArsId(Long arsId) {
        this.arsId = arsId;
    }

    public String getArsNombre() {
        return arsNombre;
    }

    public void setArsNombre(String arsNombre) {
        this.arsNombre = arsNombre;
    }

    public String getNcf() {
        return ncf;
    }

    public void setNcf(String ncf) {
        this.ncf = ncf;
    }

    public Double getMontoBruto() {
        return montoBruto;
    }

    public void setMontoBruto(Double montoBruto) {
        this.montoBruto = montoBruto;
    }

    public Double getRetencionIsr() {
        return retencionIsr;
    }

    public void setRetencionIsr(Double retencionIsr) {
        this.retencionIsr = retencionIsr;
    }

    public Double getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(Double montoNeto) {
        this.montoNeto = montoNeto;
    }

    public Integer getCantidadConsultas() {
        return cantidadConsultas;
    }

    public void setCantidadConsultas(Integer cantidadConsultas) {
        this.cantidadConsultas = cantidadConsultas;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public DGIIStatus getDgiiStatus() {
        return dgiiStatus;
    }

    public void setDgiiStatus(DGIIStatus dgiiStatus) {
        this.dgiiStatus = dgiiStatus;
    }

    public String getMensajeDgii() {
        return mensajeDgii;
    }

    public void setMensajeDgii(String mensajeDgii) {
        this.mensajeDgii = mensajeDgii;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}
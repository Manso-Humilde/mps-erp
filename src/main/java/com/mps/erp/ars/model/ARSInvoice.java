package com.mps.erp.ars.model;

import com.mps.erp.infrastructure.TenantAwareEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.mps.erp.model.DGIIStatus;

@Entity
@Table(name = "ars_invoices")
public class ARSInvoice extends TenantAwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "ars_id", nullable = false)
    private Long arsId;

    @Column(nullable = false, unique = true)
    private String ncf;

    @Column(name = "monto_bruto", nullable = false)
    private BigDecimal montoBruto = BigDecimal.ZERO;

    @Column(name = "retencion_isr")
    private BigDecimal retencionIsr = BigDecimal.ZERO;

    @Column(name = "monto_neto", nullable = false)
    private BigDecimal montoNeto = BigDecimal.ZERO;

    @Column(name = "cantidad_consultas")
    private Integer cantidadConsultas;

    private String periodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_dgii")
    private DGIIStatus estadoDgii = DGIIStatus.PENDIENTE;

    @Column(name = "mensaje_dgii", columnDefinition = "TEXT")
    private String mensajeDgii;

    @Column(name = "xml_ef", columnDefinition = "TEXT")
    private String xmlEf;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
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

    public String getNcf() {
        return ncf;
    }

    public void setNcf(String ncf) {
        this.ncf = ncf;
    }

    public BigDecimal getMontoBruto() {
        return montoBruto;
    }

    public void setMontoBruto(BigDecimal montoBruto) {
        this.montoBruto = montoBruto;
    }

    public BigDecimal getRetencionIsr() {
        return retencionIsr;
    }

    public void setRetencionIsr(BigDecimal retencionIsr) {
        this.retencionIsr = retencionIsr;
    }

    public BigDecimal getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(BigDecimal montoNeto) {
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

    public DGIIStatus getEstadoDgii() {
        return estadoDgii;
    }

    public void setEstadoDgii(DGIIStatus estadoDgii) {
        this.estadoDgii = estadoDgii;
    }

    public String getMensajeDgii() {
        return mensajeDgii;
    }

    public void setMensajeDgii(String mensajeDgii) {
        this.mensajeDgii = mensajeDgii;
    }

    public String getXmlEf() {
        return xmlEf;
    }

    public void setXmlEf(String xmlEf) {
        this.xmlEf = xmlEf;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
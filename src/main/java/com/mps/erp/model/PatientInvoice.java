package com.mps.erp.model;

import com.mps.erp.infrastructure.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_invoices")
public class PatientInvoice extends TenantAwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "consultation_id", nullable = false)
    private Long consultationId;

    @Column(nullable = false, unique = true)
    private String ncf;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Column(name = "monto_itbis")
    private BigDecimal montoItbis = BigDecimal.ZERO;

    @Column(name = "monto_neto")
    private BigDecimal montoNeto = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagador")
    private PayerType tipoPagador = PayerType.PACIENTE;

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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getNcf() {
        return ncf;
    }

    public void setNcf(String ncf) {
        this.ncf = ncf;
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoItbis() {
        return montoItbis;
    }

    public void setMontoItbis(BigDecimal montoItbis) {
        this.montoItbis = montoItbis;
    }

    public BigDecimal getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(BigDecimal montoNeto) {
        this.montoNeto = montoNeto;
    }

    public PayerType getTipoPagador() {
        return tipoPagador;
    }

    public void setTipoPagador(PayerType tipoPagador) {
        this.tipoPagador = tipoPagador;
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

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getXmlEf() {
        return xmlEf;
    }

    public void setXmlEf(String xmlEf) {
        this.xmlEf = xmlEf;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
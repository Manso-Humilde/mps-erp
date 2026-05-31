package com.mps.erp.consultation.dto;

import com.mps.erp.consultation.model.ConsultationStatus;
import com.mps.erp.consultation.model.ServiceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ConsultationResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private ServiceType tipoServicio;
    private ConsultationStatus status;
    private String diagnostico;
    private String observaciones;
    private BigDecimal montoTotal;
    private BigDecimal montoCubiertoArs;
    private BigDecimal copago;
    private LocalDateTime createdAt;

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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public ServiceType getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(ServiceType tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public ConsultationStatus getStatus() {
        return status;
    }

    public void setStatus(ConsultationStatus status) {
        this.status = status;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoCubiertoArs() {
        return montoCubiertoArs;
    }

    public void setMontoCubiertoArs(BigDecimal montoCubiertoArs) {
        this.montoCubiertoArs = montoCubiertoArs;
    }

    public BigDecimal getCopago() {
        return copago;
    }

    public void setCopago(BigDecimal copago) {
        this.copago = copago;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
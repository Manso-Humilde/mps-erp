package com.mps.erp.consultation.dto;

import com.mps.erp.consultation.model.ServiceType;
import lombok.Data;

public class ConsultationRequest {
    private Long patientId;
    private Long serviceTypeId;  // en lugar de ServiceType tipoServicio
    private String diagnostico;
    private Double montoTotal;
    private String observaciones;
    private Double porcentajeCoberturaArs;

    // Getters y Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getPorcentajeCoberturaArs() {
        return porcentajeCoberturaArs;
    }

    public void setPorcentajeCoberturaArs(Double porcentajeCoberturaArs) {
        this.porcentajeCoberturaArs = porcentajeCoberturaArs;
    }
}
package com.mps.erp.ars.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import com.mps.erp.consultation.model.ServiceType;

@Table("ars_coverage")
public class ARSCoverage {
    @Id
    private Long id;
    private Long arsId;
    private ServiceType tipoServicio;
    private Double porcentajeCobertura;
    private Double porcentajeRetencionIsr;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ARSCoverage() {}

    public ARSCoverage(Long arsId, ServiceType tipoServicio,
                       Double porcentajeCobertura, Double porcentajeRetencionIsr) {
        this.arsId = arsId;
        this.tipoServicio = tipoServicio;
        this.porcentajeCobertura = porcentajeCobertura;
        this.porcentajeRetencionIsr = porcentajeRetencionIsr;
        this.activo = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getArsId() { return arsId; }
    public void setArsId(Long arsId) { this.arsId = arsId; }
    public ServiceType getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(ServiceType tipoServicio) { this.tipoServicio = tipoServicio; }
    public Double getPorcentajeCobertura() { return porcentajeCobertura; }
    public void setPorcentajeCobertura(Double porcentajeCobertura) { this.porcentajeCobertura = porcentajeCobertura; }
    public Double getPorcentajeRetencionIsr() { return porcentajeRetencionIsr; }
    public void setPorcentajeRetencionIsr(Double porcentajeRetencionIsr) { this.porcentajeRetencionIsr = porcentajeRetencionIsr; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
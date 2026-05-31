package com.mps.erp.consultation.model;

import com.mps.erp.infrastructure.TenantAwareEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation extends TenantAwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ars_id")
    private Long arsId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @ManyToOne
    @JoinColumn(name = "service_type_id")  // ✅ Correcto
    private ServiceType tipoServicio;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Column(name = "monto_cubierto_ars")
    private BigDecimal montoCubiertoArs = BigDecimal.ZERO;

    @Column(name = "copago_paciente")
    private BigDecimal copagoPaciente = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus estado = ConsultationStatus.COMPLETADA;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta = LocalDateTime.now();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArsId() {
        return arsId;
    }

    public void setArsId(Long arsId) {
        this.arsId = arsId;
    }

    @Override
    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public ServiceType getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(ServiceType tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
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

    public BigDecimal getCopagoPaciente() {
        return copagoPaciente;
    }

    public void setCopagoPaciente(BigDecimal copagoPaciente) {
        this.copagoPaciente = copagoPaciente;
    }

    public ConsultationStatus getEstado() {
        return estado;
    }

    public void setEstado(ConsultationStatus estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
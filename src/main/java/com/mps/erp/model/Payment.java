package com.mps.erp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("payments")
public class Payment {
    @Id
    private Long id;
    private Long doctorId;
    private Long arsId;
    private Long arsInvoiceId;
    private Double montoPagado;
    private Double montoPendiente;
    private PaymentStatus estado;
    private String referencia;
    private LocalDateTime fechaPago;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment() {}

    public Payment(Long doctorId, Long arsId, Long arsInvoiceId,
                   Double montoPagado, Double montoPendiente, String referencia) {
        this.doctorId = doctorId;
        this.arsId = arsId;
        this.arsInvoiceId = arsInvoiceId;
        this.montoPagado = montoPagado;
        this.montoPendiente = montoPendiente;
        this.estado = PaymentStatus.PENDIENTE;
        this.referencia = referencia;
        this.fechaPago = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getArsId() { return arsId; }
    public void setArsId(Long arsId) { this.arsId = arsId; }
    public Long getArsInvoiceId() { return arsInvoiceId; }
    public void setArsInvoiceId(Long arsInvoiceId) { this.arsInvoiceId = arsInvoiceId; }
    public Double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(Double montoPagado) { this.montoPagado = montoPagado; }
    public Double getMontoPendiente() { return montoPendiente; }
    public void setMontoPendiente(Double montoPendiente) { this.montoPendiente = montoPendiente; }
    public PaymentStatus getEstado() { return estado; }
    public void setEstado(PaymentStatus estado) { this.estado = estado; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
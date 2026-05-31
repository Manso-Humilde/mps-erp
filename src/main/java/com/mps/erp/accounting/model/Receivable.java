package com.mps.erp.accounting.model;

import com.mps.erp.infrastructure.TenantAwareEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receivables")
public class Receivable extends TenantAwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // PACIENTE, ARS

    @Column(name = "tercero_id", nullable = false)
    private Long terceroId;

    @Column(name = "factura_id", nullable = false)
    private Long facturaId;

    @Column(name = "factura_tipo", nullable = false)
    private String facturaTipo;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "saldo_pendiente", nullable = false)
    private BigDecimal saldoPendiente;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    private String estado = "PENDIENTE"; // PENDIENTE, PARCIAL, PAGADO, VENCIDO

    public Receivable() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getTerceroId() { return terceroId; }
    public void setTerceroId(Long terceroId) { this.terceroId = terceroId; }
    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }
    public String getFacturaTipo() { return facturaTipo; }
    public void setFacturaTipo(String facturaTipo) { this.facturaTipo = facturaTipo; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

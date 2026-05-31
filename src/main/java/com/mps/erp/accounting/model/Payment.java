package com.mps.erp.accounting.model;

import com.mps.erp.infrastructure.TenantAwareEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment extends TenantAwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // COBRO, PAGO

    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId;

    @Column(name = "referencia_tipo", nullable = false)
    private String referenciaTipo; // RECEIVABLE, PAYABLE

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago; // EFECTIVO, TRANSFERENCIA, CHEQUE

    @Column(name = "referencia_comprobante")
    private String referenciaComprobante;

    private String observaciones;

    public Payment() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
    public String getReferenciaTipo() { return referenciaTipo; }
    public void setReferenciaTipo(String referenciaTipo) { this.referenciaTipo = referenciaTipo; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getReferenciaComprobante() { return referenciaComprobante; }
    public void setReferenciaComprobante(String referenciaComprobante) { this.referenciaComprobante = referenciaComprobante; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
package com.mps.erp.fiscal.model;

import com.mps.erp.infrastructure.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ncf_sequences")
@EqualsAndHashCode(callSuper = true)
public class NCFSequence extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_ecf", nullable = false)
    private String tipoEcf;

    @Column(name = "prefijo", length = 3)
    private String prefijo = "E";

    @Column(name = "secuencia_actual", nullable = false)
    private Long secuenciaActual;

    @Column(name = "secuencia_final", nullable = false)
    private Long secuenciaFinal;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "activo")
    private Boolean activo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoEcf() {
        return tipoEcf;
    }

    public void setTipoEcf(String tipoEcf) {
        this.tipoEcf = tipoEcf;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Long getSecuenciaActual() {
        return secuenciaActual;
    }

    public void setSecuenciaActual(Long secuenciaActual) {
        this.secuenciaActual = secuenciaActual;
    }

    public Long getSecuenciaFinal() {
        return secuenciaFinal;
    }

    public void setSecuenciaFinal(Long secuenciaFinal) {
        this.secuenciaFinal = secuenciaFinal;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Formats the full NCF: E + Type(2) + Sequence(10)
     */
    public String getFormattedNCF() {
        return String.format("%s%s%010d", prefijo, tipoEcf.replace("E", ""), secuenciaActual);
    }
}

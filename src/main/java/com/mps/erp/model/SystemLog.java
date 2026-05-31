package com.mps.erp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("system_logs")
public class SystemLog {
    @Id
    private Long id;
    private Long userId;
    private String accion;
    private String entidad;
    private String descripcion;
    private String datosAdicionales;
    private LocalDateTime createdAt;

    public SystemLog() {}

    public SystemLog(Long userId, String accion, String entidad, String descripcion) {
        this.userId = userId;
        this.accion = accion;
        this.entidad = entidad;
        this.descripcion = descripcion;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDatosAdicionales() { return datosAdicionales; }
    public void setDatosAdicionales(String datosAdicionales) { this.datosAdicionales = datosAdicionales; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
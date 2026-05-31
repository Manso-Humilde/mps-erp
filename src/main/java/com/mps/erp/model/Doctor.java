package com.mps.erp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("doctors")
public class Doctor {
    @Id
    @Column("id")
    private Long id;

    @Column("nombre_completo")
    private String nombreCompleto;

    @Column("tipo_identificacion")
    private PersonType tipoIdentificacion;

    @Column("numero_identificacion")
    private String numeroIdentificacion;

    @Column("especialidad")
    private String especialidad;

    @Column("telefono")
    private String telefono;

    @Column("email")
    private String email;

    @Column("direccion")
    private String direccion;

    @Column("rnc")
    private String rnc;

    @Column("user_id")
    private Long userId;

    @Column("activo")
    private Boolean activo;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Doctor() {}

    public Doctor(String nombreCompleto, PersonType tipoIdentificacion,
                  String numeroIdentificacion, String especialidad, String rnc, Long userId) {
        this.nombreCompleto = nombreCompleto;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.especialidad = especialidad;
        this.rnc = rnc;
        this.userId = userId;
        this.activo = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public PersonType getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(PersonType tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getRnc() { return rnc; }
    public void setRnc(String rnc) { this.rnc = rnc; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
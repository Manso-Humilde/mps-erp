package com.mps.erp.dto;

import com.mps.erp.model.PersonType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientResponse {
    private Long id;
    private String nombreCompleto;
    private PersonType tipoIdentificacion;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private Long arsId;
    private String arsNombre;
    private String numeroSeguro;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PatientResponse(Long id, String nombreCompleto, PersonType tipoIdentificacion,
                           String numeroIdentificacion, LocalDate fechaNacimiento,
                           String telefono, String email, String direccion,
                           Long arsId, String arsNombre, String numeroSeguro, Boolean activo,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.arsId = arsId;
        this.arsNombre = arsNombre;
        this.numeroSeguro = numeroSeguro;
        this.activo = activo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public PersonType getTipoIdentificacion() { return tipoIdentificacion; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public Long getArsId() { return arsId; }
    public String getArsNombre() { return arsNombre; }
    public String getNumeroSeguro() { return numeroSeguro; }
    public Boolean getActivo() { return activo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
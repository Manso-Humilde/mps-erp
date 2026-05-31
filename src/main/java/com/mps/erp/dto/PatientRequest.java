package com.mps.erp.dto;

import com.mps.erp.model.PersonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PatientRequest {
    @NotBlank
    private String nombreCompleto;
    
    @NotNull
    private PersonType tipoIdentificacion;
    
    @NotBlank
    private String numeroIdentificacion;
    
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private Long arsId;
    private String numeroSeguro;

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public PersonType getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(PersonType tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public Long getArsId() { return arsId; }
    public void setArsId(Long arsId) { this.arsId = arsId; }
    
    public String getNumeroSeguro() { return numeroSeguro; }
    public void setNumeroSeguro(String numeroSeguro) { this.numeroSeguro = numeroSeguro; }
}
package com.mps.erp.dto;

import com.mps.erp.model.UserRole;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String nombreCompleto;
    private String email;
    private UserRole role;
    private Long doctorId;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String nombreCompleto, String email, 
                         UserRole role, Long doctorId) {
        this.token = token;
        this.userId = userId;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.role = role;
        this.doctorId = doctorId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}
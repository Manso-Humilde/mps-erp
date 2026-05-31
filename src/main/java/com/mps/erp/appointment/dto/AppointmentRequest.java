package com.mps.erp.appointment.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private Long serviceTypeId;
    private LocalDateTime fechaHoraInicio;
    private String motivo;
    private String notas;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(Long serviceTypeId) { this.serviceTypeId = serviceTypeId; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
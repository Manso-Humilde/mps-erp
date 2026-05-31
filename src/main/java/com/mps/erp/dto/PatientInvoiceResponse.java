package com.mps.erp.dto;

import com.mps.erp.model.DGIIStatus;
import com.mps.erp.model.PayerType;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class PatientInvoiceResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private Long consultationId;
    private String ncf;
    private Double montoTotal;
    private Double montoItbis;
    private Double montoNeto;
    private PayerType tipoPagador;
    private DGIIStatus estadoDgii;
    private String mensajeDgii;
    private LocalDateTime fechaEmision;

    public PatientInvoiceResponse() {}

    public PatientInvoiceResponse(Long id, Long doctorId, String doctorName, Long patientId, String patientName, 
                                  Long consultationId, String ncf, Double montoTotal, Double montoItbis, 
                                  Double montoNeto, PayerType tipoPagador, DGIIStatus estadoDgii, 
                                  String mensajeDgii, LocalDateTime fechaEmision) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.consultationId = consultationId;
        this.ncf = ncf;
        this.montoTotal = montoTotal;
        this.montoItbis = montoItbis;
        this.montoNeto = montoNeto;
        this.tipoPagador = tipoPagador;
        this.estadoDgii = estadoDgii;
        this.mensajeDgii = mensajeDgii;
        this.fechaEmision = fechaEmision;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }
    public String getNcf() { return ncf; }
    public void setNcf(String ncf) { this.ncf = ncf; }
    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }
    public Double getMontoItbis() { return montoItbis; }
    public void setMontoItbis(Double montoItbis) { this.montoItbis = montoItbis; }
    public Double getMontoNeto() { return montoNeto; }
    public void setMontoNeto(Double montoNeto) { this.montoNeto = montoNeto; }
    public PayerType getTipoPagador() { return tipoPagador; }
    public void setTipoPagador(PayerType tipoPagador) { this.tipoPagador = tipoPagador; }
    public DGIIStatus getEstadoDgii() { return estadoDgii; }
    public void setEstadoDgii(DGIIStatus estadoDgii) { this.estadoDgii = estadoDgii; }
    public String getMensajeDgii() { return mensajeDgii; }
    public void setMensajeDgii(String mensajeDgii) { this.mensajeDgii = mensajeDgii; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
}
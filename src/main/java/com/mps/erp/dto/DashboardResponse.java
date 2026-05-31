package com.mps.erp.dto;

import lombok.Data;
import java.util.List;
import com.mps.erp.consultation.dto.ConsultationResponse;

@Data
public class DashboardResponse {
    private Double ingresoMensual;
    private Double retencionTotal;
    private Double montoCobrado;
    private Double montoPendiente;
    private Long totalConsultas;
    private Long totalFacturasPendientes;
    private List<ConsultationResponse> ultimasConsultas;
    private List<PatientInvoiceResponse> ultimasFacturas;

    public DashboardResponse() {}

    public DashboardResponse(Double ingresoMensual, Double retencionTotal, Double montoCobrado, 
                             Double montoPendiente, Long totalConsultas, Long totalFacturasPendientes, 
                             List<ConsultationResponse> ultimasConsultas, 
                             List<PatientInvoiceResponse> ultimasFacturas) {
        this.ingresoMensual = ingresoMensual;
        this.retencionTotal = retencionTotal;
        this.montoCobrado = montoCobrado;
        this.montoPendiente = montoPendiente;
        this.totalConsultas = totalConsultas;
        this.totalFacturasPendientes = totalFacturasPendientes;
        this.ultimasConsultas = ultimasConsultas;
        this.ultimasFacturas = ultimasFacturas;
    }

    public Double getIngresoMensual() { return ingresoMensual; }
    public void setIngresoMensual(Double ingresoMensual) { this.ingresoMensual = ingresoMensual; }
    public Double getRetencionTotal() { return retencionTotal; }
    public void setRetencionTotal(Double retencionTotal) { this.retencionTotal = retencionTotal; }
    public Double getMontoCobrado() { return montoCobrado; }
    public void setMontoCobrado(Double montoCobrado) { this.montoCobrado = montoCobrado; }
    public Double getMontoPendiente() { return montoPendiente; }
    public void setMontoPendiente(Double montoPendiente) { this.montoPendiente = montoPendiente; }
    public Long getTotalConsultas() { return totalConsultas; }
    public void setTotalConsultas(Long totalConsultas) { this.totalConsultas = totalConsultas; }
    public Long getTotalFacturasPendientes() { return totalFacturasPendientes; }
    public void setTotalFacturasPendientes(Long totalFacturasPendientes) { this.totalFacturasPendientes = totalFacturasPendientes; }
    public List<ConsultationResponse> getUltimasConsultas() { return ultimasConsultas; }
    public void setUltimasConsultas(List<ConsultationResponse> ultimasConsultas) { this.ultimasConsultas = ultimasConsultas; }
    public List<PatientInvoiceResponse> getUltimasFacturas() { return ultimasFacturas; }
    public void setUltimasFacturas(List<PatientInvoiceResponse> ultimasFacturas) { this.ultimasFacturas = ultimasFacturas; }
}
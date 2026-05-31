package com.mps.erp.dto;

import com.mps.erp.model.DGIIStatus;
import com.mps.erp.model.PayerType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientInvoiceRequest {
    private Long patientId;
    private Long consultationId;
    private PayerType payerType;
    private String ncf;
    private Double subtotal;
    private Double itbis;
    private Double total;
    private DGIIStatus dgiiStatus;
    private LocalDate fechaFactura;
}
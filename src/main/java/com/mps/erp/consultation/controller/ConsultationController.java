package com.mps.erp.consultation.controller;

import com.mps.erp.consultation.dto.ConsultationRequest;
import com.mps.erp.consultation.dto.ConsultationResponse;
import com.mps.erp.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {
    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<ConsultationResponse> createConsultation(@Valid @RequestBody ConsultationRequest request) {
        return ResponseEntity.ok(consultationService.createConsultation(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultationResponse>> getConsultations() {
        return ResponseEntity.ok(consultationService.getConsultationsByDoctor());
    }
}
package com.mps.erp.controller;

import com.mps.erp.dto.PatientRequest;
import com.mps.erp.dto.PatientResponse;
import com.mps.erp.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.createPatient(request));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
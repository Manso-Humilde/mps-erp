package com.mps.erp.service;

import com.mps.erp.ars.repository.ARSRepository;
import com.mps.erp.dto.PatientRequest;
import com.mps.erp.dto.PatientResponse;
import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.model.Patient;
import com.mps.erp.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ARSRepository arsRepository;

    public PatientService(PatientRepository patientRepository, ARSRepository arsRepository) {
        this.patientRepository = patientRepository;
        this.arsRepository = arsRepository;
    }

    public List<PatientResponse> getAllPatients() {
        Long tenantId = TenantContext.getCurrentTenant();
        return patientRepository.findByTenantIdOrderByNombreCompletoAsc(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(Long id) {
        Long tenantId = TenantContext.getCurrentTenant();
        Patient patient = patientRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return toResponse(patient);
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();
        Patient patient = new Patient();
        patient.setTenantId(tenantId);
        patient.setNombreCompleto(request.getNombreCompleto());
        patient.setTipoIdentificacion(request.getTipoIdentificacion());
        patient.setNumeroIdentificacion(request.getNumeroIdentificacion());
        patient.setFechaNacimiento(request.getFechaNacimiento());
        patient.setTelefono(request.getTelefono());
        patient.setEmail(request.getEmail());
        patient.setDireccion(request.getDireccion());
        patient.setArsId(request.getArsId());
        patient.setNumeroSeguro(request.getNumeroSeguro());
        patient.setActivo(true);

        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();
        Patient patient = patientRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        patient.setNombreCompleto(request.getNombreCompleto());
        patient.setTipoIdentificacion(request.getTipoIdentificacion());
        patient.setNumeroIdentificacion(request.getNumeroIdentificacion());
        patient.setFechaNacimiento(request.getFechaNacimiento());
        patient.setTelefono(request.getTelefono());
        patient.setEmail(request.getEmail());
        patient.setDireccion(request.getDireccion());
        patient.setArsId(request.getArsId());
        patient.setNumeroSeguro(request.getNumeroSeguro());

        Patient updated = patientRepository.save(patient);
        return toResponse(updated);
    }

    @Transactional
    public void deletePatient(Long id) {
        Long tenantId = TenantContext.getCurrentTenant();
        Patient patient = patientRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        patient.setActivo(false);
        patientRepository.save(patient);
    }

    private PatientResponse toResponse(Patient patient) {
        final String[] arsNombre = {null};
        if (patient.getArsId() != null) {
            arsRepository.findById(patient.getArsId()).ifPresent(ars -> arsNombre[0] = ars.getNombre());
        }

        return new PatientResponse(
                patient.getId(),
                patient.getNombreCompleto(),
                patient.getTipoIdentificacion(),
                patient.getNumeroIdentificacion(),
                patient.getFechaNacimiento(),
                patient.getTelefono(),
                patient.getEmail(),
                patient.getDireccion(),
                patient.getArsId(),
                arsNombre[0],
                patient.getNumeroSeguro(),
                patient.getActivo(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
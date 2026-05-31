package com.mps.erp.consultation.service;

import com.mps.erp.consultation.dto.ConsultationRequest;
import com.mps.erp.consultation.dto.ConsultationResponse;
import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.consultation.model.Consultation;
import com.mps.erp.consultation.model.ConsultationStatus;
import com.mps.erp.consultation.model.ServiceType;
import com.mps.erp.consultation.repository.ConsultationRepository;
import com.mps.erp.consultation.repository.ServiceTypeRepository;
import com.mps.erp.model.Patient;
import com.mps.erp.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public ConsultationService(ConsultationRepository consultationRepository,
                               PatientRepository patientRepository,
                               ServiceTypeRepository serviceTypeRepository) {
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Transactional
    public ConsultationResponse createConsultation(ConsultationRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Cargar ServiceType desde el ID
        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));

        BigDecimal montoTotal = BigDecimal.valueOf(request.getMontoTotal());
        BigDecimal porcentajeArs = BigDecimal.valueOf(request.getPorcentajeCoberturaArs() != null ? request.getPorcentajeCoberturaArs() : 80.0);
        BigDecimal montoCubiertoArs = montoTotal.multiply(porcentajeArs)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal copagoPaciente = montoTotal.subtract(montoCubiertoArs);

        Consultation consultation = new Consultation();
        consultation.setTenantId(tenantId);
        consultation.setArsId(patient.getArsId());
        consultation.setPatientId(request.getPatientId());
        consultation.setTipoServicio(serviceType);
        consultation.setDiagnostico(request.getDiagnostico());
        consultation.setMontoTotal(montoTotal);
        consultation.setMontoCubiertoArs(montoCubiertoArs);
        consultation.setCopagoPaciente(copagoPaciente);
        consultation.setEstado(ConsultationStatus.COMPLETADA);
        consultation.setObservaciones(request.getObservaciones());

        Consultation saved = consultationRepository.save(consultation);

        return convertToResponse(saved);
    }

    public List<ConsultationResponse> getConsultationsByDoctor() {
        Long tenantId = TenantContext.getCurrentTenant();
        return consultationRepository.findByTenantIdAndEstado(tenantId, ConsultationStatus.COMPLETADA)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ConsultationResponse convertToResponse(Consultation c) {
        ConsultationResponse response = new ConsultationResponse();
        response.setId(c.getId());
        response.setDoctorId(c.getTenantId());
        response.setDoctorName("Doctor Enterprise");
        response.setPatientId(c.getPatientId());

        String patientName = patientRepository.findById(c.getPatientId())
                .map(p -> p.getNombreCompleto())
                .orElse("Paciente");
        response.setPatientName(patientName);

        response.setTipoServicio(c.getTipoServicio());  // ← Cambia esto: .getNombre() → solo el objeto
        response.setStatus(c.getEstado());
        response.setDiagnostico(c.getDiagnostico());
        response.setObservaciones(c.getObservaciones());
        response.setMontoTotal(c.getMontoTotal());
        response.setMontoCubiertoArs(c.getMontoCubiertoArs());
        response.setCopago(c.getCopagoPaciente());
        response.setCreatedAt(c.getCreatedAt());

        return response;
    }
}
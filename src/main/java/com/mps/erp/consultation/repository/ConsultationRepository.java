package com.mps.erp.consultation.repository;

import com.mps.erp.consultation.model.Consultation;
import com.mps.erp.consultation.model.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByTenantIdAndEstado(Long tenantId, ConsultationStatus estado);
    List<Consultation> findByTenantIdAndPatientIdOrderByFechaConsultaDesc(Long tenantId, Long patientId);
    List<Consultation> findByTenantIdAndArsIdAndFechaConsultaBetween(Long tenantId, Long arsId, LocalDateTime inicio, LocalDateTime fin);
}
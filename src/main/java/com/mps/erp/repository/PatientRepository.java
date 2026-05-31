package com.mps.erp.repository;

import com.mps.erp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByTenantIdAndId(Long tenantId, Long id);

    List<Patient> findByTenantIdOrderByNombreCompletoAsc(Long tenantId);

    Optional<Patient> findByTenantIdAndNumeroIdentificacion(Long tenantId, String numeroIdentificacion);

    List<Patient> findByTenantIdAndActivoTrueOrderByNombreCompletoAsc(Long tenantId);

    List<Patient> findByTenantIdAndArsId(Long tenantId, Long arsId);
}
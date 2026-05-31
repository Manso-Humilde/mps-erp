package com.mps.erp.repository;

import com.mps.erp.model.DGIIStatus;
import com.mps.erp.model.PatientInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientInvoiceRepository extends JpaRepository<PatientInvoice, Long> {
    List<PatientInvoice> findByTenantIdOrderByFechaEmisionDesc(Long tenantId);
    List<PatientInvoice> findByTenantIdAndPatientIdOrderByFechaEmisionDesc(Long tenantId, Long patientId);
    List<PatientInvoice> findByEstadoDgii(DGIIStatus estadoDgii);
}
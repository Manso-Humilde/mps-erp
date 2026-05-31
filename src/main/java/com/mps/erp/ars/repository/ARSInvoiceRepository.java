package com.mps.erp.ars.repository;

import com.mps.erp.ars.model.ARSInvoice;
import com.mps.erp.consultation.model.Consultation;
import com.mps.erp.model.DGIIStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ARSInvoiceRepository extends JpaRepository<ARSInvoice, Long> {
    List<ARSInvoice> findByTenantIdOrderByFechaEmisionDesc(Long tenantId);
    List<ARSInvoice> findByTenantIdAndArsId(Long tenantId, Long arsId);
    List<ARSInvoice> findByEstadoDgii(DGIIStatus estadoDgii);

    // En ARSInvoiceRepository.java, agrega:
    @Query("SELECT c FROM Consultation c WHERE c.tenantId = :tenantId AND c.fechaConsulta BETWEEN :inicio AND :fin AND c.estado = 'COMPLETADA'")
    List<Consultation> findCompletedConsultationsByPeriod(@Param("tenantId") Long tenantId,
                                                          @Param("inicio") LocalDateTime inicio,
                                                          @Param("fin") LocalDateTime fin);
}
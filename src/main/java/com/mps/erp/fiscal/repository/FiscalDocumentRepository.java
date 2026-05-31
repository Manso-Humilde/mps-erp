package com.mps.erp.fiscal.repository;

import com.mps.erp.fiscal.model.FiscalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalDocumentRepository extends JpaRepository<FiscalDocument, Long> {
    List<FiscalDocument> findByTenantIdOrderByFechaEmisionDesc(Long tenantId);
}
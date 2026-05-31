package com.mps.erp.ars.repository;

import com.mps.erp.ars.model.ARS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ARSRepository extends JpaRepository<ARS, Long> {
    List<ARS> findByTenantId(Long tenantId);
}
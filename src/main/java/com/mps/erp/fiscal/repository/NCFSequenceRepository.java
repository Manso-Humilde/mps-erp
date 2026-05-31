package com.mps.erp.fiscal.repository;

import com.mps.erp.fiscal.model.NCFSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NCFSequenceRepository extends JpaRepository<NCFSequence, Long> {

    Optional<NCFSequence> findByTenantIdAndTipoEcf(Long tenantId, String tipoEcf);/**
     * Finds and locks the sequence record for the specific tenant and e-CF type.
     * Prevents other transactions from reading or writing until this one commits.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM NCFSequence s WHERE s.tenantId = :tenantId AND s.tipoEcf = :tipoEcf AND s.activo = true")
    Optional<NCFSequence> findAndLockByTenantIdAndTipoEcf(@Param("tenantId") Long tenantId, @Param("tipoEcf") String tipoEcf);
}

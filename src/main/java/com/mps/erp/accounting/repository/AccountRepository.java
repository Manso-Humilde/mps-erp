package com.mps.erp.accounting.repository;

import com.mps.erp.accounting.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByTenantId(Long tenantId);
    List<Account> findByTenantIdAndActivoTrue(Long tenantId);
    List<Account> findByTenantIdAndTipo(Long tenantId, String tipo);

    Optional<Account> findByTenantIdAndCodigo(Long tenantId, String codigo);
}
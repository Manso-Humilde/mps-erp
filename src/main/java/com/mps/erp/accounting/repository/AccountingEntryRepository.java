package com.mps.erp.accounting.repository;

import com.mps.erp.accounting.model.AccountingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, Long> {
    List<AccountingEntry> findByTenantIdOrderByFechaDesc(Long tenantId);
    List<AccountingEntry> findByTenantIdAndFechaBetween(Long tenantId, LocalDate inicio, LocalDate fin);
    List<AccountingEntry> findByTenantIdAndReferenciaIdAndReferenciaTipo(Long tenantId, Long referenciaId, String referenciaTipo);
    List<AccountingEntry> findByTenantIdAndAccountId(Long tenantId, Long accountId);
    List<AccountingEntry> findByTenantIdAndFechaBetweenAndCategoria(Long tenantId, LocalDate inicio, LocalDate fin, String categoria);
}
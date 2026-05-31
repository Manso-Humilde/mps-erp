package com.mps.erp.accounting.repository;

import com.mps.erp.accounting.model.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceivableRepository extends JpaRepository<Receivable, Long> {
    List<Receivable> findByTenantIdAndEstado(Long tenantId, String estado);
    List<Receivable> findByTenantIdAndTerceroIdAndTipo(Long tenantId, Long terceroId, String tipo);
    List<Receivable> findByTenantIdAndEstadoIn(Long tenantId, List<String> estados);

}
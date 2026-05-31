package com.mps.erp.accounting.repository;

import com.mps.erp.accounting.model.Payable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PayableRepository extends JpaRepository<Payable, Long> {
    List<Payable> findByTenantId(Long tenantId);
    List<Payable> findByTenantIdAndEstado(Long tenantId, String estado);
    List<Payable> findByTenantIdAndEstadoIn(Long tenantId, List<String> estados);


}
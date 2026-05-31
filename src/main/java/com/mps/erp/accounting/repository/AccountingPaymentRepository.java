package com.mps.erp.accounting.repository;

import com.mps.erp.accounting.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AccountingPaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTenantIdAndReferenciaIdAndReferenciaTipo(Long tenantId, Long referenciaId, String referenciaTipo);

    // Agrega estos métodos:
    List<Payment> findByTenantIdAndTipoAndFechaBetween(Long tenantId, String tipo, LocalDate inicio, LocalDate fin);

    List<Payment> findByTenantIdAndTipoAndFechaBetweenAndMetodoPago(Long tenantId, String tipo, LocalDate inicio, LocalDate fin, String metodoPago);
}
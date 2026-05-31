package com.mps.erp.accounting.service;

import com.mps.erp.accounting.dto.BalanceGeneralDTO;
import com.mps.erp.accounting.dto.EstadoResultadosDTO;
import com.mps.erp.accounting.model.Account;
import com.mps.erp.accounting.model.AccountingEntry;
import com.mps.erp.accounting.repository.AccountRepository;
import com.mps.erp.accounting.repository.AccountingEntryRepository;
import com.mps.erp.infrastructure.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountingService {

    private final AccountRepository accountRepository;
    private final AccountingEntryRepository entryRepository;

    public AccountingService(AccountRepository accountRepository,
                             AccountingEntryRepository entryRepository) {
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
    }

    // Crear asiento contable automático al facturar consulta
    @Transactional
    public void registrarIngresoConsulta(Long consultaId, BigDecimal monto, Long pacienteId) {
        Long tenantId = TenantContext.getCurrentTenant();

        // Buscar cuenta de ingresos por consultas (código 4-01-01)
        Account cuentaIngreso = accountRepository.findByTenantIdAndCodigo(tenantId, "4-01-01")
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        AccountingEntry entry = new AccountingEntry();
        entry.setTenantId(tenantId);
        entry.setFecha(LocalDate.now());
        entry.setTipo("INGRESO");
        entry.setCategoria("CONSULTA");
        entry.setDescripcion("Consulta médica #" + consultaId);
        entry.setMonto(monto);
        entry.setAccountId(cuentaIngreso.getId());
        entry.setReferenciaId(consultaId);
        entry.setReferenciaTipo("CONSULTA");

        entryRepository.save(entry);
    }

    // Crear asiento por facturación ARS
    @Transactional
    public void registrarIngresoARS(Long facturaId, BigDecimal monto, Long arsId) {
        Long tenantId = TenantContext.getCurrentTenant();

        Account cuentaIngresoARS = accountRepository.findByTenantIdAndCodigo(tenantId, "4-01-02")
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        AccountingEntry entry = new AccountingEntry();
        entry.setTenantId(tenantId);
        entry.setFecha(LocalDate.now());
        entry.setTipo("INGRESO");
        entry.setCategoria("FACTURA_ARS");
        entry.setDescripcion("Facturación ARS #" + facturaId);
        entry.setMonto(monto);
        entry.setAccountId(cuentaIngresoARS.getId());
        entry.setReferenciaId(facturaId);
        entry.setReferenciaTipo("FACTURA_ARS");

        entryRepository.save(entry);
    }

    // Listar asientos
    public List<AccountingEntry> getEntries() {
        Long tenantId = TenantContext.getCurrentTenant();
        return entryRepository.findByTenantIdOrderByFechaDesc(tenantId);
    }

    // Libro diario por rango de fechas
    public List<AccountingEntry> getLedger(LocalDate inicio, LocalDate fin) {
        Long tenantId = TenantContext.getCurrentTenant();
        return entryRepository.findByTenantIdAndFechaBetween(tenantId, inicio, fin);
    }

    public BalanceGeneralDTO[] getBalanceGeneral() {
        Long tenantId = TenantContext.getCurrentTenant();

        BigDecimal totalActivos = calcularTotalPorTipo(tenantId, "ACTIVO");
        BigDecimal totalPasivos = calcularTotalPorTipo(tenantId, "PASIVO");
        BigDecimal totalPatrimonio = calcularTotalPorTipo(tenantId, "PATRIMONIO");
        BigDecimal totalIngresos = calcularTotalPorTipo(tenantId, "INGRESO");
        BigDecimal totalGastos = calcularTotalPorTipo(tenantId, "GASTO");

        return new BalanceGeneralDTO[] {
                new BalanceGeneralDTO("ACTIVOS", "", totalActivos, "ACTIVO"),
                new BalanceGeneralDTO("PASIVOS", "", totalPasivos, "PASIVO"),
                new BalanceGeneralDTO("PATRIMONIO", "", totalPatrimonio, "PATRIMONIO"),
                new BalanceGeneralDTO("INGRESOS", "", totalIngresos, "INGRESO"),
                new BalanceGeneralDTO("GASTOS", "", totalGastos, "GASTO")
        };
    }

    public EstadoResultadosDTO[] getEstadoResultados(LocalDate inicio, LocalDate fin) {
        Long tenantId = TenantContext.getCurrentTenant();

        BigDecimal ingresosTotales = calcularIngresosPorPeriodo(tenantId, inicio, fin);
        BigDecimal gastosTotales = calcularGastosPorPeriodo(tenantId, inicio, fin);
        BigDecimal utilidadMeta = ingresosTotales.subtract(gastosTotales);

        return new EstadoResultadosDTO[] {
                new EstadoResultadosDTO("INGRESOS TOTALES", ingresosTotales, "INGRESO"),
                new EstadoResultadosDTO("GASTOS TOTALES", gastosTotales, "GASTO"),
                new EstadoResultadosDTO("UTILIDAD / PÉRDIDA", utilidadMeta, "RESULTADO")
        };
    }

    private BigDecimal calcularTotalPorTipo(Long tenantId, String tipo) {
        List<Account> cuentas = accountRepository.findByTenantIdAndTipo(tenantId, tipo);
        BigDecimal total = BigDecimal.ZERO;
        for (Account cuenta : cuentas) {
            List<AccountingEntry> entries = entryRepository.findByTenantIdAndAccountId(tenantId, cuenta.getId());
            for (AccountingEntry entry : entries) {
                // INGRESOS y ACTIVOS suman
                if (tipo.equals("INGRESO") || tipo.equals("ACTIVO")) {
                    total = total.add(entry.getMonto());
                }
                // PASIVOS y GASTOS restan
                else if (tipo.equals("PASIVO") || tipo.equals("GASTO")) {
                    total = total.subtract(entry.getMonto());
                }
            }
        }
        return total;
    }

    private BigDecimal calcularIngresosPorPeriodo(Long tenantId, LocalDate inicio, LocalDate fin) {
        List<AccountingEntry> entries = entryRepository.findByTenantIdAndFechaBetween(tenantId, inicio, fin);
        BigDecimal total = BigDecimal.ZERO;
        for (AccountingEntry entry : entries) {
            if ("INGRESO".equals(entry.getTipo())) {
                total = total.add(entry.getMonto());
            }
        }
        return total;
    }

    private BigDecimal calcularGastosPorPeriodo(Long tenantId, LocalDate inicio, LocalDate fin) {
        List<AccountingEntry> entries = entryRepository.findByTenantIdAndFechaBetween(tenantId, inicio, fin);
        BigDecimal total = BigDecimal.ZERO;
        for (AccountingEntry entry : entries) {
            if ("EGRESO".equals(entry.getTipo())) {
                total = total.add(entry.getMonto());
            }
        }
        return total;
    }
}
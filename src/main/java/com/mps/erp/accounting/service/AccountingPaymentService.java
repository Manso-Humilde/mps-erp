package com.mps.erp.accounting.service;

import com.mps.erp.accounting.dto.*;
import com.mps.erp.accounting.model.Payable;
import com.mps.erp.accounting.model.Payment;
import com.mps.erp.accounting.model.Receivable;
import com.mps.erp.accounting.repository.AccountingPaymentRepository;
import com.mps.erp.accounting.repository.PayableRepository;
import com.mps.erp.accounting.repository.ReceivableRepository;
import com.mps.erp.ars.model.ARS;
import com.mps.erp.ars.repository.ARSRepository;
import com.mps.erp.infrastructure.tenant.TenantContext;
import com.mps.erp.model.Patient;
import com.mps.erp.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class AccountingPaymentService {

    private final ReceivableRepository receivableRepository;
    private final AccountingPaymentRepository paymentRepository;
    private final PatientRepository patientRepository;
    private final ARSRepository arsRepository;
    private final PayableRepository payableRepository;

    public AccountingPaymentService(ReceivableRepository receivableRepository, AccountingPaymentRepository paymentRepository, PatientRepository patientRepository, ARSRepository arsRepository, PayableRepository payableRepository) {
        this.receivableRepository = receivableRepository;
        this.paymentRepository = paymentRepository;
        this.patientRepository = patientRepository;
        this.arsRepository = arsRepository;
        this.payableRepository = payableRepository;
    }

    public List<Payable> getPayablesPendientes() {
        Long tenantId = TenantContext.getCurrentTenant();
        return payableRepository.findByTenantIdAndEstadoIn(tenantId, List.of("PENDIENTE", "PARCIAL"));
    }

    @Transactional
    public Payable crearPayable(PayableRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();

        Payable payable = new Payable();
        payable.setTenantId(tenantId);
        payable.setProveedorId(request.getProveedorId());
        payable.setDocumentoTipo("FACTURA_PROVEEDOR");
        payable.setDocumentoId(0L); // Temporal, ajustar según necesidad
        payable.setProveedorNombre(request.getProveedorNombre());
        payable.setMonto(request.getMonto());
        payable.setSaldoPendiente(request.getMonto());
        payable.setFechaEmision(LocalDate.now());
        payable.setFechaVencimiento(request.getFechaVencimiento());
        payable.setEstado("PENDIENTE");

        return payableRepository.save(payable);
    }

    @Transactional
    public Payment registrarPagoProveedor(PagoProveedorRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();

        Payable payable = payableRepository.findById(request.getPayableId())
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        if (request.getMonto().compareTo(payable.getSaldoPendiente()) > 0) {
            throw new RuntimeException("El monto excede el saldo pendiente");
        }

        Payment payment = new Payment();
        payment.setTenantId(tenantId);
        payment.setTipo("PAGO");
        payment.setReferenciaId(request.getPayableId());
        payment.setReferenciaTipo("PAYABLE");
        payment.setMonto(request.getMonto());
        payment.setFecha(LocalDate.now());
        payment.setMetodoPago(request.getMetodoPago());
        payment.setReferenciaComprobante(request.getReferenciaComprobante());
        payment.setObservaciones(request.getObservaciones());

        Payment saved = paymentRepository.save(payment);

        BigDecimal nuevoSaldo = payable.getSaldoPendiente().subtract(request.getMonto());
        payable.setSaldoPendiente(nuevoSaldo);
        payable.setEstado(nuevoSaldo.compareTo(BigDecimal.ZERO) == 0 ? "PAGADO" : "PARCIAL");
        payableRepository.save(payable);

        return saved;
    }

    @Transactional
    public Payment registrarPago(Long receivableId, BigDecimal monto, String metodoPago,
                                 String referenciaComprobante, String observaciones) {
        Long tenantId = TenantContext.getCurrentTenant();

        Receivable receivable = receivableRepository.findById(receivableId)
                .orElseThrow(() -> new RuntimeException("Cuenta por cobrar no encontrada"));

        if (!receivable.getTenantId().equals(tenantId)) {
            throw new RuntimeException("No autorizado");
        }

        if (monto.compareTo(receivable.getSaldoPendiente()) > 0) {
            throw new RuntimeException("El monto del pago excede el saldo pendiente");
        }

        Payment payment = new Payment();
        payment.setTenantId(tenantId);
        payment.setTipo("COBRO");
        payment.setReferenciaId(receivableId);
        payment.setReferenciaTipo("RECEIVABLE");
        payment.setMonto(monto);
        payment.setFecha(LocalDate.now());
        payment.setMetodoPago(metodoPago);
        payment.setReferenciaComprobante(referenciaComprobante);
        payment.setObservaciones(observaciones);

        Payment saved = paymentRepository.save(payment);

        BigDecimal nuevoSaldo = receivable.getSaldoPendiente().subtract(monto);
        receivable.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            receivable.setEstado("PAGADO");
        } else {
            receivable.setEstado("PARCIAL");
        }

        receivableRepository.save(receivable);

        return saved;
    }

    public List<Receivable> getReceivablesPendientes() {
        Long tenantId = TenantContext.getCurrentTenant();
        return receivableRepository.findByTenantIdAndEstadoIn(tenantId, List.of("PENDIENTE", "PARCIAL"));
    }

    public List<ReceivableAntiguedadDTO> getAntiguedadSaldos() {
        Long tenantId = TenantContext.getCurrentTenant();
        List<Receivable> receivables = receivableRepository.findByTenantIdAndEstadoIn(tenantId, List.of("PENDIENTE", "PARCIAL"));
        List<ReceivableAntiguedadDTO> resultado = new ArrayList<>();

        LocalDate hoy = LocalDate.now();

        for (Receivable r : receivables) {
            long diasVencidos = ChronoUnit.DAYS.between(r.getFechaVencimiento(), hoy);
            if (diasVencidos < 0) diasVencidos = 0;

            String rangoDias;
            if (diasVencidos <= 30) rangoDias = "0-30 días";
            else if (diasVencidos <= 60) rangoDias = "31-60 días";
            else if (diasVencidos <= 90) rangoDias = "61-90 días";
            else rangoDias = "90+ días";

            String terceroNombre = obtenerNombreTercero(r.getTerceroId(), r.getTipo());

            resultado.add(new ReceivableAntiguedadDTO(
                    r.getId(), r.getTipo(), terceroNombre,
                    r.getMonto(), r.getSaldoPendiente(),
                    rangoDias, (int) diasVencidos
            ));
        }

        return resultado;
    }

    private String obtenerNombreTercero(Long terceroId, String tipo) {
        if (tipo.equals("PACIENTE")) {
            return patientRepository.findById(terceroId).map(Patient::getNombreCompleto).orElse("Desconocido");
        } else if (tipo.equals("ARS")) {
            return arsRepository.findById(terceroId).map(ARS::getNombre).orElse("Desconocido");
        }
        return "Desconocido";
    }

    public List<PaymentDTO> getCobros(LocalDate inicio, LocalDate fin, String metodoPago) {
        Long tenantId = TenantContext.getCurrentTenant();

        List<Payment> payments;
        if (metodoPago != null && !metodoPago.isEmpty()) {
            payments = paymentRepository.findByTenantIdAndTipoAndFechaBetweenAndMetodoPago(
                    tenantId, "COBRO", inicio, fin, metodoPago);
        } else {
            payments = paymentRepository.findByTenantIdAndTipoAndFechaBetween(
                    tenantId, "COBRO", inicio, fin);
        }

        List<PaymentDTO> result = new ArrayList<>();
        for (Payment p : payments) {
            String cliente = obtenerNombreCliente(p.getReferenciaId());
            result.add(new PaymentDTO(p.getFecha(), cliente, p.getMonto(),
                    p.getMetodoPago(), p.getReferenciaComprobante(), p.getObservaciones()));
        }
        return result;
    }

    public List<PagoProveedorDTO> getPagosProveedor(LocalDate inicio, LocalDate fin, String proveedor) {
        Long tenantId = TenantContext.getCurrentTenant();

        List<Payment> payments = paymentRepository.findByTenantIdAndTipoAndFechaBetween(
                tenantId, "PAGO", inicio, fin);

        List<PagoProveedorDTO> result = new ArrayList<>();
        for (Payment p : payments) {
            Payable payable = payableRepository.findById(p.getReferenciaId()).orElse(null);
            if (payable != null) {
                if (proveedor == null || proveedor.isEmpty() ||
                        payable.getProveedorNombre().toLowerCase().contains(proveedor.toLowerCase())) {
                    result.add(new PagoProveedorDTO(p.getFecha(), payable.getProveedorNombre(),
                            p.getMonto(), p.getMetodoPago(), p.getReferenciaComprobante(), p.getObservaciones()));
                }
            }
        }
        return result;
    }

    private String obtenerNombreCliente(Long receivableId) {
        Receivable receivable = receivableRepository.findById(receivableId).orElse(null);
        if (receivable == null) return "Desconocido";

        if (receivable.getTipo().equals("PACIENTE")) {
            return patientRepository.findById(receivable.getTerceroId())
                    .map(Patient::getNombreCompleto).orElse("Desconocido");
        } else if (receivable.getTipo().equals("ARS")) {
            return arsRepository.findById(receivable.getTerceroId())
                    .map(ARS::getNombre).orElse("Desconocido");
        }
        return "Desconocido";
    }
}
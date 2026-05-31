package com.mps.erp.accounting.controller;

import com.mps.erp.accounting.dto.*;
import com.mps.erp.accounting.model.AccountingEntry;
import com.mps.erp.accounting.model.Payable;
import com.mps.erp.accounting.model.Payment;
import com.mps.erp.accounting.model.Receivable;
import com.mps.erp.accounting.service.AccountingPaymentService;
import com.mps.erp.accounting.service.AccountingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounting")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE')")
public class AccountingController {

    private final AccountingService accountingService;
    private final AccountingPaymentService accountingPaymentService;

    public AccountingController(AccountingService accountingService,
                                AccountingPaymentService accountingPaymentService) {
        this.accountingService = accountingService;
        this.accountingPaymentService = accountingPaymentService;
    }

    @GetMapping("/entries")
    public ResponseEntity<List<AccountingEntry>> getEntries() {
        return ResponseEntity.ok(accountingService.getEntries());
    }

    @GetMapping("/ledger")
    public ResponseEntity<List<AccountingEntry>> getLedger(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(accountingService.getLedger(inicio, fin));
    }

    @GetMapping("/receivables/pendientes")
    public ResponseEntity<List<Receivable>> getReceivablesPendientes() {
        return ResponseEntity.ok(accountingPaymentService.getReceivablesPendientes());
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> registrarPago(@RequestBody PagoRequest request) {
        Payment payment = accountingPaymentService.registrarPago(
                request.getReceivableId(),
                BigDecimal.valueOf(request.getMonto()),
                request.getMetodoPago(),
                request.getReferenciaComprobante(),
                request.getObservaciones()
        );
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/receivables/antiguedad")
    public ResponseEntity<List<ReceivableAntiguedadDTO>> getAntiguedadSaldos() {
        return ResponseEntity.ok(accountingPaymentService.getAntiguedadSaldos());
    }

    @GetMapping("/payables/pendientes")
    public ResponseEntity<List<Payable>> getPayablesPendientes() {
        return ResponseEntity.ok(accountingPaymentService.getPayablesPendientes());
    }

    @PostMapping("/payables")
    public ResponseEntity<Payable> crearPayable(@RequestBody PayableRequest request) {
        return ResponseEntity.ok(accountingPaymentService.crearPayable(request));
    }

    @PostMapping("/payables/pagos")
    public ResponseEntity<Payment> registrarPagoProveedor(@RequestBody PagoProveedorRequest request) {
        return ResponseEntity.ok(accountingPaymentService.registrarPagoProveedor(request));
    }


    @GetMapping("/balance-general")
    public ResponseEntity<BalanceGeneralDTO[]> getBalanceGeneral() {
        return ResponseEntity.ok(accountingService.getBalanceGeneral());
    }

    @GetMapping("/estado-resultados")
    public ResponseEntity<EstadoResultadosDTO[]> getEstadoResultados(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(accountingService.getEstadoResultados(inicio, fin));
    }

    @GetMapping("/payments/cobros")
    public ResponseEntity<List<PaymentDTO>> getCobros(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(required = false) String metodoPago) {
        return ResponseEntity.ok(accountingPaymentService.getCobros(inicio, fin, metodoPago));
    }

    @GetMapping("/payments/pagos")
    public ResponseEntity<List<PagoProveedorDTO>> getPagosProveedor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(required = false) String proveedor) {
        return ResponseEntity.ok(accountingPaymentService.getPagosProveedor(inicio, fin, proveedor));
    }

}
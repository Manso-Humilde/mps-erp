package com.mps.erp.ars.controller;

import com.mps.erp.ars.model.ARS;
import com.mps.erp.ars.service.ARSService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ars")
public class ARSController {

    private final ARSService arsService;

    public ARSController(ARSService arsService) {
        this.arsService = arsService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO')")
    public ResponseEntity<List<ARS>> getAllARS() {
        return ResponseEntity.ok(arsService.getAllARS());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ARS> getArsById(@PathVariable Long id) {
        return ResponseEntity.ok(arsService.findById(id));
    }

}

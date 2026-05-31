package com.mps.erp.ars.service;

import com.mps.erp.ars.model.ARS;
import com.mps.erp.ars.repository.ARSRepository;
import com.mps.erp.infrastructure.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ARSService {
    private final ARSRepository arsRepository;

    public ARSService(ARSRepository arsRepository) {
        this.arsRepository = arsRepository;
    }

    public List<ARS> getAllARS() {
        return arsRepository.findAll();
    }

    public ARS findById(Long id) {
        Long tenantId = TenantContext.getCurrentTenant();
        return arsRepository.findById(id)
                .filter(ars -> ars.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("ARS no encontrada"));
    }

}



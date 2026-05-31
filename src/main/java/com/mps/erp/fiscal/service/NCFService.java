package com.mps.erp.fiscal.service;

import com.mps.erp.fiscal.model.NCFSequence;
import com.mps.erp.fiscal.repository.NCFSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NCFService {

    private final NCFSequenceRepository ncfSequenceRepository;

    public NCFService(NCFSequenceRepository ncfSequenceRepository) {
        this.ncfSequenceRepository = ncfSequenceRepository;
    }

    @Transactional
    public String consumeNextNCF(Long tenantId, String tipoEcf) {
        NCFSequence sequence = ncfSequenceRepository.findAndLockByTenantIdAndTipoEcf(tenantId, tipoEcf)
                .orElseThrow(() -> new RuntimeException("No existe secuencia NCF para tipo: " + tipoEcf));

        Long nextNumber = sequence.getSecuenciaActual() + 1;
        sequence.setSecuenciaActual(nextNumber);

        ncfSequenceRepository.save(sequence);

        // Obtener el número del tipo (01 para B01, 32 para E32)
        String tipoNumero = tipoEcf.substring(1); // "01" o "32"

        // Formato según tipo:
        // B01 -> B01D0010000001
        // E32 -> E32D0010000001
        String tenantIdStr = String.format("D%03d", tenantId);
        String numeroStr = String.format("%07d", nextNumber);

        return tipoEcf + tenantIdStr + numeroStr;
    }
}
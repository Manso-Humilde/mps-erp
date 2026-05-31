package com.mps.erp.infrastructure.audit;

import com.mps.erp.infrastructure.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final JdbcTemplate jdbcTemplate;

    public AuditService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void logEvent(Long userId, String action, String entity, String entityId, String oldData, String newData) {
        String sql = "INSERT INTO audit_events (tenant_id, user_id, accion, entidad, entidad_id, datos_anteriores, datos_nuevos) " +
                     "VALUES (?, ?, ?, ?, ?, ?::jsonb, ?::jsonb)";
        
        jdbcTemplate.update(sql, 
            TenantContext.getCurrentTenant(),
            userId,
            action,
            entity,
            entityId,
            oldData,
            newData
        );
    }
}

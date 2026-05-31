package com.mps.erp.infrastructure.tenant;

import com.mps.erp.infrastructure.audit.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

@Component
public class TenantEntityListener {

    @PrePersist
    @PreUpdate
    public void setTenant(AuditableEntity entity) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            entity.setTenantId(tenantId);
        } else if (entity.getTenantId() == null) {
            // In a real enterprise system, we might throw an exception here 
            // unless it's a super-admin action.
            throw new IllegalStateException("Attempting to save entity without tenant context");
        }
    }
}

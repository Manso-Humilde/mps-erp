package com.mps.erp.infrastructure.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enterprise Tenant Context
 * Manages the current tenant (doctor/clinic) in a thread-safe manner.
 */
public class TenantContext {
    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(Long tenantId) {
        logger.debug("Setting tenant context to: {}", tenantId);
        currentTenant.set(tenantId);
    }

    public static Long getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}

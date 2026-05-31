package com.mps.erp.infrastructure.tenant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilterInterceptor implements HandlerInterceptor {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
            System.out.println("Tenant filter activado para: " + tenantId);
            System.out.println("=== TenantFilterInterceptor - tenantId: " + tenantId);
        }
        return true;
    }
}
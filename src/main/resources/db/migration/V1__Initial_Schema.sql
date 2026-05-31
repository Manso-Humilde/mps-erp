-- V1__Initial_Schema.sql

-- 1. Tenants (Doctors/Clinics)
CREATE TABLE tenants (
    id BIGSERIAL PRIMARY KEY,
    rnc_cedula VARCHAR(11) UNIQUE NOT NULL,
    nombre_comercial VARCHAR(255) NOT NULL,
    razon_social VARCHAR(255),
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    email VARCHAR(255) NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(50) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT true,
    version BIGINT DEFAULT 0,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, email)
);

-- 3. Patients
CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    nombre_completo VARCHAR(255) NOT NULL,
    tipo_identificacion VARCHAR(50) NOT NULL,
    numero_identificacion VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    telefono VARCHAR(50),
    version BIGINT DEFAULT 0,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, numero_identificacion)
);

-- 4. Consultations
CREATE TABLE consultations (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    patient_id BIGINT NOT NULL REFERENCES patients(id),
    motivo TEXT,
    diagnostico TEXT,
    monto_total DECIMAL(18, 2) NOT NULL DEFAULT 0.00,
    version BIGINT DEFAULT 0,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Audit Events
CREATE TABLE audit_events (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT,
    user_id BIGINT,
    accion VARCHAR(100) NOT NULL,
    entidad VARCHAR(100) NOT NULL,
    entidad_id VARCHAR(100),
    datos_anteriores JSONB,
    datos_nuevos JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_users_tenant ON users(tenant_id);
CREATE INDEX idx_patients_tenant ON patients(tenant_id);
CREATE INDEX idx_consultations_tenant ON consultations(tenant_id);
CREATE INDEX idx_audit_tenant ON audit_events(tenant_id);

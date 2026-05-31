-- V2__Fiscal_Module_DGII.sql

-- 1. NCF Sequences per Tenant
CREATE TABLE ncf_sequences (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    tipo_ecf VARCHAR(3) NOT NULL, -- E31, E32, E33, E34, E41, E43, E44
    prefijo CHAR(1) DEFAULT 'E',
    secuencia_actual BIGINT NOT NULL DEFAULT 1,
    secuencia_final BIGINT NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    activo BOOLEAN DEFAULT true,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, tipo_ecf)
);

-- 2. Fiscal Documents (e-CF)
CREATE TABLE fiscal_documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    ncf VARCHAR(13) UNIQUE NOT NULL,
    tipo_ecf VARCHAR(3) NOT NULL,
    rnc_emisor VARCHAR(11) NOT NULL,
    rnc_receptor VARCHAR(11),
    monto_bruto DECIMAL(18, 2) NOT NULL,
    monto_itbis DECIMAL(18, 2) DEFAULT 0.00,
    monto_exento DECIMAL(18, 2) DEFAULT 0.00,
    monto_total DECIMAL(18, 2) NOT NULL,
    xml_content TEXT,
    signature_data TEXT,
    track_id VARCHAR(100),
    estado_dgii VARCHAR(50) DEFAULT 'PENDIENTE', -- PENDIENTE, APROBADA, RECHAZADA
    mensaje_dgii TEXT,
    fecha_emision TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Fiscal Configuration (Tenant-specific DGII credentials)
CREATE TABLE fiscal_config (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT UNIQUE NOT NULL REFERENCES tenants(id),
    rnc VARCHAR(11) NOT NULL,
    razon_social VARCHAR(255) NOT NULL,
    certificado_digital_url TEXT,
    certificado_pass_encrypted TEXT,
    ambiente VARCHAR(20) DEFAULT 'CERTIFICACION', -- CERTIFICACION, PRODUCCION
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_fiscal_ncf ON fiscal_documents(ncf);
CREATE INDEX idx_fiscal_tenant_date ON fiscal_documents(tenant_id, fecha_emision);

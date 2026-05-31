-- V4__Enterprise_Accounting_Ledger.sql

-- 1. Chart of Accounts (Catálogo de Cuentas)
CREATE TABLE chart_of_accounts (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    padre_id BIGINT REFERENCES chart_of_accounts(id),
    nivel INTEGER DEFAULT 1,
    es_detalle BOOLEAN DEFAULT true,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, codigo)
);

-- 2. Journal Entries (Asientos Contables)
CREATE TABLE journal_entries (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    fecha DATE NOT NULL,
    descripcion TEXT NOT NULL,
    referencia VARCHAR(100), -- ID del documento origen (Factura, Pago, etc)
    tipo_documento VARCHAR(50), -- PATIENT_INVOICE, ARS_CLAIM, PAYMENT
    estado VARCHAR(20) DEFAULT 'POSTED', -- DRAFT, POSTED, CANCELLED
    created_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Journal Lines (Detalle de Asiento)
CREATE TABLE journal_lines (
    id BIGSERIAL PRIMARY KEY,
    entry_id BIGINT NOT NULL REFERENCES journal_entries(id) ON DELETE CASCADE,
    account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    debe DECIMAL(18, 2) DEFAULT 0.00,
    haber DECIMAL(18, 2) DEFAULT 0.00,
    memo TEXT,
    CHECK (debe >= 0 AND haber >= 0),
    CHECK (debe > 0 OR haber > 0)
);

-- 4. Accounting Configuration (Mapping business events to accounts)
CREATE TABLE accounting_config (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT UNIQUE NOT NULL REFERENCES tenants(id),
    cta_caja_id BIGINT REFERENCES chart_of_accounts(id),
    cta_cxc_pacientes_id BIGINT REFERENCES chart_of_accounts(id),
    cta_cxc_ars_id BIGINT REFERENCES chart_of_accounts(id),
    cta_ingresos_medicos_id BIGINT REFERENCES chart_of_accounts(id),
    cta_itbis_pagar_id BIGINT REFERENCES chart_of_accounts(id),
    cta_retencion_isr_id BIGINT REFERENCES chart_of_accounts(id)
);

-- Indexes
CREATE INDEX idx_journal_fecha ON journal_entries(tenant_id, fecha);
CREATE INDEX idx_lines_account ON journal_lines(account_id);

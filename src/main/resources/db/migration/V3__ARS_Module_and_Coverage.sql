-- V3__ARS_Module_and_Coverage.sql

-- 1. ARS Entities
CREATE TABLE ars (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rnc VARCHAR(11) UNIQUE NOT NULL,
    codigo_prestador VARCHAR(50), -- Código asignado al médico por esta ARS
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. ARS Plans (e.g. Básico, Complementario, etc.)
CREATE TABLE ars_plans (
    id BIGSERIAL PRIMARY KEY,
    ars_id BIGINT NOT NULL REFERENCES ars(id) ON DELETE CASCADE,
    nombre_plan VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT true
);

-- 3. Coverage Rules Motor
CREATE TABLE coverage_rules (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    plan_id BIGINT NOT NULL REFERENCES ars_plans(id) ON DELETE CASCADE,
    tipo_servicio VARCHAR(100) NOT NULL, -- CONSULTA, PROCEDIMIENTO, LABORATORIO
    porcentaje_cobertura DECIMAL(5, 2) NOT NULL DEFAULT 80.00,
    monto_fijo_copago DECIMAL(18, 2) DEFAULT 0.00,
    requiere_autorizacion BOOLEAN DEFAULT false,
    UNIQUE(tenant_id, plan_id, tipo_servicio)
);

-- 4. Update Patients to link with ARS
ALTER TABLE patients ADD COLUMN ars_id BIGINT REFERENCES ars(id);
ALTER TABLE patients ADD COLUMN ars_plan_id BIGINT REFERENCES ars_plans(id);
ALTER TABLE patients ADD COLUMN numero_seguro VARCHAR(50);

-- 5. Seed initial ARS data for Dominican Republic
INSERT INTO ars (nombre, rnc) VALUES 
('ARS SENASA (Básico)', '101850116'),
('ARS PALIC (MAPFRE)', '101115591'),
('ARS HUMANO', '101824425'),
('ARS UNIVERSAL', '101010250');

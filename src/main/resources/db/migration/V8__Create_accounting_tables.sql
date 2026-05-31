-- Plan de cuentas
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
codigo VARCHAR(20) NOT NULL,
nombre VARCHAR(255) NOT NULL,
tipo VARCHAR(20) NOT NULL,
nivel INTEGER DEFAULT 1,
parent_id BIGINT,
activo BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Asientos contables
CREATE TABLE IF NOT EXISTS accounting_entries (
id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
fecha DATE NOT NULL,
tipo VARCHAR(20) NOT NULL,
categoria VARCHAR(50) NOT NULL,
descripcion TEXT,
monto DECIMAL(12,2) NOT NULL,
account_id BIGINT NOT NULL,
referencia_id BIGINT,
referencia_tipo VARCHAR(50),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar plan de cuentas básico (solo si no existen)
INSERT INTO accounts (tenant_id, codigo, nombre, tipo, nivel)
SELECT 1, '1-01-01', 'Caja', 'ACTIVO', 1
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE codigo = '1-01-01');

INSERT INTO accounts (tenant_id, codigo, nombre, tipo, nivel)
SELECT 1, '1-01-02', 'Bancos', 'ACTIVO', 1
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE codigo = '1-01-02');

INSERT INTO accounts (tenant_id, codigo, nombre, tipo, nivel)
SELECT 1, '1-02-01', 'Cuentas por Cobrar - Pacientes', 'ACTIVO', 1
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE codigo = '1-02-01');

INSERT INTO accounts (tenant_id, codigo, nombre, tipo, nivel)
SELECT 1, '1-02-02', 'Cuentas por Cobrar - ARS', 'ACTIVO', 1
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE codigo = '1-02-02');

INSERT INTO accounts (tenant_id, codigo, nombre, tipo, nivel)
SELECT 1, '4-01-01', 'Ingresos por Consultas', 'INGRESO', 1
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE codigo = '4-01-01');

INSERT INTO accounts (tenant_id, codigo, nombre, tipo, nivel)
SELECT 1, '4-01-02', 'Ingresos por Facturación ARS', 'INGRESO', 1
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE codigo = '4-01-02');
(1, '1-01-02', 'Bancos', 'ACTIVO', 1),
(1, '1-02-01', 'Cuentas por Cobrar - Pacientes', 'ACTIVO', 1),
(1, '1-02-02', 'Cuentas por Cobrar - ARS', 'ACTIVO', 1),
(1, '4-01-01', 'Ingresos por Consultas', 'INGRESO', 1),
(1, '4-01-02', 'Ingresos por Facturación ARS', 'INGRESO', 1);
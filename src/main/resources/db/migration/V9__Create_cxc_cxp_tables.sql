-- Cuentas por Cobrar
CREATE TABLE IF NOT EXISTS receivables (
id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
tipo VARCHAR(20) NOT NULL, -- PACIENTE, ARS
tercero_id BIGINT NOT NULL,
factura_id BIGINT NOT NULL,
factura_tipo VARCHAR(50) NOT NULL, -- FACTURA_PACIENTE, FACTURA_ARS
monto DECIMAL(12,2) NOT NULL,
saldo_pendiente DECIMAL(12,2) NOT NULL,
fecha_emision DATE NOT NULL,
fecha_vencimiento DATE NOT NULL,
estado VARCHAR(20) DEFAULT 'PENDIENTE', -- PENDIENTE, PARCIAL, PAGADO, VENCIDO
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cuentas por Pagar
CREATE TABLE IF NOT EXISTS payables (
id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
proveedor_id BIGINT NOT NULL,
documento_id BIGINT NOT NULL,
documento_tipo VARCHAR(50) NOT NULL,
monto DECIMAL(12,2) NOT NULL,
saldo_pendiente DECIMAL(12,2) NOT NULL,
fecha_emision DATE NOT NULL,
fecha_vencimiento DATE NOT NULL,
estado VARCHAR(20) DEFAULT 'PENDIENTE',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Pagos
CREATE TABLE IF NOT EXISTS payments (
id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
tipo VARCHAR(20) NOT NULL, -- COBRO, PAGO
referencia_id BIGINT NOT NULL,
referencia_tipo VARCHAR(20) NOT NULL, -- RECEIVABLE, PAYABLE
monto DECIMAL(12,2) NOT NULL,
fecha DATE NOT NULL,
metodo_pago VARCHAR(20) NOT NULL, -- EFECTIVO, TRANSFERENCIA, CHEQUE
referencia_comprobante VARCHAR(100),
observaciones TEXT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_receivables_tenant ON receivables(tenant_id);
CREATE INDEX idx_receivables_tercero ON receivables(tercero_id, tipo);
CREATE INDEX idx_receivables_estado ON receivables(estado);
CREATE INDEX idx_payables_tenant ON payables(tenant_id);
CREATE INDEX idx_payables_proveedor ON payables(proveedor_id);
CREATE INDEX idx_payments_tenant ON payments(tenant_id);
CREATE INDEX idx_payments_referencia ON payments(referencia_id, referencia_tipo);
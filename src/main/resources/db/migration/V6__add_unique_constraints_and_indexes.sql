-- 1. UNIQUE para pacientes (mismo tenant no puede tener dos pacientes con misma identificación)
ALTER TABLE patients ADD CONSTRAINT unique_patient_tenant_identificacion UNIQUE (tenant_id, numero_identificacion);

-- 2. UNIQUE para facturas de paciente (mismo tenant no puede duplicar NCF)
ALTER TABLE patient_invoices ADD CONSTRAINT unique_patient_invoice_tenant_ncf UNIQUE (tenant_id, ncf);

-- 3. UNIQUE para facturas ARS
ALTER TABLE ars_invoices ADD CONSTRAINT unique_ars_invoice_tenant_ncf UNIQUE (tenant_id, ncf);

-- 4. UNIQUE para documentos fiscales
ALTER TABLE fiscal_documents ADD CONSTRAINT unique_fiscal_document_tenant_ncf UNIQUE (tenant_id, ncf);

-- 5. Índices para consultas por tenant (optimización)
CREATE INDEX idx_patients_tenant_id ON patients(tenant_id);
CREATE INDEX idx_consultations_tenant_id ON consultations(tenant_id);
CREATE INDEX idx_patient_invoices_tenant_id ON patient_invoices(tenant_id);
CREATE INDEX idx_ars_invoices_tenant_id ON ars_invoices(tenant_id);
CREATE INDEX idx_fiscal_documents_tenant_id ON fiscal_documents(tenant_id);
CREATE INDEX idx_users_tenant_id ON users(tenant_id);
CREATE INDEX idx_ncf_sequences_tenant_id ON ncf_sequences(tenant_id);
-- Tabla de citas
CREATE TABLE IF NOT EXISTS appointments (
id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
patient_id BIGINT NOT NULL,
doctor_id BIGINT NOT NULL,
service_type_id BIGINT NOT NULL,
fecha_hora_inicio TIMESTAMP NOT NULL,
fecha_hora_fin TIMESTAMP NOT NULL,
status VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
motivo VARCHAR(255),
notas TEXT,
consultation_id BIGINT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Tabla de horarios por médico
CREATE TABLE IF NOT EXISTS doctor_schedules (
id BIGSERIAL PRIMARY KEY,
tenant_id BIGINT NOT NULL,
doctor_id BIGINT NOT NULL,
dia_semana INT NOT NULL, -- 1=Lunes, 7=Domingo
hora_inicio TIME NOT NULL,
hora_fin TIME NOT NULL,
intervalo_minutos INT NOT NULL DEFAULT 30,
activo BOOLEAN DEFAULT TRUE
);

-- Índices
CREATE INDEX idx_appointments_tenant_id ON appointments(tenant_id);
CREATE INDEX idx_appointments_doctor_fecha ON appointments(doctor_id, fecha_hora_inicio);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_status ON appointments(status);
CREATE INDEX idx_doctor_schedules_tenant ON doctor_schedules(tenant_id);
CREATE INDEX idx_doctor_schedules_doctor ON doctor_schedules(doctor_id);

-- Unique constraint: no citas solapadas por médico
CREATE UNIQUE INDEX idx_unique_appointment_doctor_time
ON appointments(doctor_id, fecha_hora_inicio)
WHERE status NOT IN ('CANCELADA', 'NO_ASISTIO');
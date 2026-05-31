DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS ars_invoices CASCADE;
DROP TABLE IF EXISTS patient_invoices CASCADE;
DROP TABLE IF EXISTS consultations CASCADE;
DROP TABLE IF EXISTS ars_coverage CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS doctors CASCADE;
DROP TABLE IF EXISTS ars CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS system_logs CASCADE;
DROP TABLE IF EXISTS system_config CASCADE;

DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS person_type CASCADE;
DROP TYPE IF EXISTS service_type CASCADE;
DROP TYPE IF EXISTS consultation_status CASCADE;
DROP TYPE IF EXISTS dgii_status CASCADE;
DROP TYPE IF EXISTS payer_type CASCADE;
DROP TYPE IF EXISTS payment_status CASCADE;

CREATE TYPE user_role AS ENUM ('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'ASISTENTE');
CREATE TYPE person_type AS ENUM ('RNC', 'CEDULA', 'PASAPORTE');
CREATE TYPE service_type AS ENUM ('CONSULTA_GENERAL', 'ESPECIALIDAD', 'PROCEDIMIENTO', 'EXAMEN_LABORATORIO', 'IMAGENES_DIAGNOSTICO');
CREATE TYPE consultation_status AS ENUM ('PENDIENTE', 'EN_PROGRESO', 'COMPLETADA', 'CANCELADA');
CREATE TYPE dgii_status AS ENUM ('PENDIENTE_ENVIO', 'ENVIADO_DGII', 'EN_PROCESO', 'APROBADA', 'RECHAZADA');
CREATE TYPE billing_status AS ENUM ('PENDIENTE_ARS', 'FACTURADO_ARS', 'RECHAZADO_ARS');
CREATE TYPE payer_type AS ENUM ('PACIENTE', 'ARS', 'MIXTO');
CREATE TYPE payment_status AS ENUM ('PENDIENTE', 'PARCIAL', 'COMPLETADO', 'ANULADO');

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role user_role NOT NULL,
                       nombre_completo VARCHAR(255) NOT NULL,
                       doctor_id BIGINT,
                       activo BOOLEAN DEFAULT true,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doctors (
                         id BIGSERIAL PRIMARY KEY,
                         nombre_completo VARCHAR(255) NOT NULL,
                         tipo_identificacion person_type NOT NULL,
                         numero_identificacion VARCHAR(20) NOT NULL,
                         especialidad VARCHAR(100),
                         telefono VARCHAR(20),
                         email VARCHAR(255),
                         direccion TEXT,
                         rnc VARCHAR(11) NOT NULL,
                         user_id BIGINT UNIQUE,
                         activo BOOLEAN DEFAULT true,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ars (
                     id BIGSERIAL PRIMARY KEY,
                     nombre VARCHAR(255) NOT NULL,
                     codigo VARCHAR(50) UNIQUE NOT NULL,
                     rnc VARCHAR(11) NOT NULL,
                     telefono VARCHAR(20),
                     email VARCHAR(255),
                     direccion TEXT,
                     activo BOOLEAN DEFAULT true,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ars_coverage (
                              id BIGSERIAL PRIMARY KEY,
                              ars_id BIGINT NOT NULL REFERENCES ars(id) ON DELETE CASCADE,
                              tipo_servicio service_type NOT NULL,
                              porcentaje_cobertura NUMERIC(5,2) NOT NULL,
                              porcentaje_retencion_isr NUMERIC(5,2) NOT NULL,
                              activo BOOLEAN DEFAULT true,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patients (
                          id BIGSERIAL PRIMARY KEY,
                          doctor_id BIGINT NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
                          nombre_completo VARCHAR(255) NOT NULL,
                          tipo_identificacion person_type NOT NULL,
                          numero_identificacion VARCHAR(20) NOT NULL,
                          fecha_nacimiento DATE,
                          telefono VARCHAR(20),
                          email VARCHAR(255),
                          direccion TEXT,
                          ars_id BIGINT REFERENCES ars(id) ON DELETE SET NULL,
                          numero_seguro VARCHAR(50),
                          activo BOOLEAN DEFAULT true,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE consultations (
                               id BIGSERIAL PRIMARY KEY,
                               doctor_id BIGINT NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
                               patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
                               tipo_servicio service_type NOT NULL,
                               estado consultation_status DEFAULT 'COMPLETADA',
                               diagnostico TEXT NOT NULL,
                               observaciones TEXT,
                               monto_total NUMERIC(10,2) NOT NULL,
                               monto_cubierto_ars NUMERIC(10,2) NOT NULL,
                               copago_paciente NUMERIC(10,2) NOT NULL,
                               fecha_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               ars_invoice_id BIGINT,
                               estado_facturacion_ars VARCHAR(50) DEFAULT 'PENDIENTE_ARS',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patient_invoices (
                                  id BIGSERIAL PRIMARY KEY,
                                  doctor_id BIGINT NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
                                  patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
                                  consultation_id BIGINT NOT NULL REFERENCES consultations(id) ON DELETE CASCADE,
                                  ncf VARCHAR(30) UNIQUE NOT NULL,
                                  monto_total NUMERIC(10,2) NOT NULL,
                                  monto_itbis NUMERIC(10,2) DEFAULT 0.00,
                                  monto_neto NUMERIC(10,2) NOT NULL,
                                  tipo_pagador payer_type NOT NULL,
                                  estado_dgii dgii_status DEFAULT 'PENDIENTE_ENVIO',
                                  mensaje_dgii TEXT,
                                  xml_ef TEXT,
                                  fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ars_invoices (
                              id BIGSERIAL PRIMARY KEY,
                              doctor_id BIGINT NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
                              ars_id BIGINT NOT NULL REFERENCES ars(id) ON DELETE CASCADE,
                              ncf VARCHAR(30) UNIQUE NOT NULL,
                              monto_bruto NUMERIC(10,2) NOT NULL,
                              retencion_isr NUMERIC(10,2) NOT NULL,
                              monto_neto NUMERIC(10,2) NOT NULL,
                              cantidad_consultaciones INTEGER NOT NULL,
                              periodo VARCHAR(7) NOT NULL,
                              estado_dgii dgii_status DEFAULT 'PENDIENTE_ENVIO',
                              mensaje_dgii TEXT,
                              xml_ef TEXT,
                              fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          doctor_id BIGINT NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
                          ars_id BIGINT NOT NULL REFERENCES ars(id) ON DELETE CASCADE,
                          ars_invoice_id BIGINT NOT NULL REFERENCES ars_invoices(id) ON DELETE CASCADE,
                          monto_pagado NUMERIC(10,2) NOT NULL,
                          monto_pendiente NUMERIC(10,2) NOT NULL,
                          estado payment_status DEFAULT 'PENDIENTE',
                          referencia VARCHAR(100),
                          fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE system_logs (
                             id BIGSERIAL PRIMARY KEY,
                             user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
                             accion VARCHAR(100) NOT NULL,
                             entidad VARCHAR(100) NOT NULL,
                             descripcion TEXT,
                             datos_adicionales TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE system_config (
                               id BIGSERIAL PRIMARY KEY,
                               clave VARCHAR(255) UNIQUE NOT NULL,
                               valor TEXT NOT NULL,
                               descripcion TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
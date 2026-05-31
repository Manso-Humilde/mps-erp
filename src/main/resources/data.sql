INSERT INTO users (email, password, role, nombre_completo, doctor_id, activo) VALUES
                                                                                  ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'SUPER_ADMIN', 'Admin Principal', NULL, true),
                                                                                  ('contable', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN_CONTABLE', 'Contadora María García', NULL, true),
                                                                                  ('medico', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'MEDICO', 'Dr. Juan Pérez', NULL, true),
                                                                                  ('asistente', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ASISTENTE', 'Asistente Ana López', NULL, true);

INSERT INTO ars (nombre, codigo, rnc, telefono, email, direccion) VALUES
                                                                      ('ARS Humano', 'HUMANO', '131047698', '809-544-2424', 'info@ars-humano.com', 'Av. Winston Churchill #45, Santo Domingo'),
                                                                      ('ARS Palic Salud', 'PALIC', '131023456', '809-472-4567', 'servicio@palic.com.do', 'Paseo de la Juventud, Santo Domingo'),
                                                                      ('SeNaSa', 'SENASA', '131089012', '809-220-5400', 'contacto@senasa.gob.do', 'Av. San Martín #3, Santo Domingo'),
                                                                      ('ARS Universal', 'UNIVERSAL', '131065432', '809-682-8989', 'info@arsuniversal.com', 'Calle Principal #123, Santiago');

INSERT INTO doctors (nombre_completo, tipo_identificacion, numero_identificacion, especialidad, telefono, email, direccion, rnc, user_id) VALUES
                                                                                                                                              ('Dr. Juan Pérez', 'CEDULA', '00123456789', 'Medicina General', '809-555-0001', 'juan.perez@mps.com', 'Calle Médico #1, Santo Domingo', '131496293', 3),
                                                                                                                                              ('Dra. María Rodríguez', 'CEDULA', '00123456790', 'Pediatría', '809-555-0002', 'maria.rodriguez@mps.com', 'Calle Médico #2, Santiago', '131496294', NULL);

UPDATE users SET doctor_id = 1 WHERE id = 3;

INSERT INTO ars_coverage (ars_id, tipo_servicio, porcentaje_cobertura, porcentaje_retencion_isr) VALUES
                                                                                                     (1, 'CONSULTA_GENERAL', 80.00, 10.00),
                                                                                                     (1, 'ESPECIALIDAD', 85.00, 10.00),
                                                                                                     (1, 'PROCEDIMIENTO', 70.00, 10.00),
                                                                                                     (1, 'EXAMEN_LABORATORIO', 90.00, 5.00),
                                                                                                     (1, 'IMAGENES_DIAGNOSTICO', 85.00, 10.00),
                                                                                                     (2, 'CONSULTA_GENERAL', 75.00, 12.00),
                                                                                                     (2, 'ESPECIALIDAD', 80.00, 12.00),
                                                                                                     (2, 'PROCEDIMIENTO', 65.00, 12.00),
                                                                                                     (2, 'EXAMEN_LABORATORIO', 85.00, 8.00),
                                                                                                     (2, 'IMAGENES_DIAGNOSTICO', 80.00, 12.00),
                                                                                                     (3, 'CONSULTA_GENERAL', 70.00, 15.00),
                                                                                                     (3, 'ESPECIALIDAD', 75.00, 15.00),
                                                                                                     (3, 'PROCEDIMIENTO', 60.00, 15.00),
                                                                                                     (3, 'EXAMEN_LABORATORIO', 80.00, 10.00),
                                                                                                     (3, 'IMAGENES_DIAGNOSTICO', 75.00, 15.00),
                                                                                                     (4, 'CONSULTA_GENERAL', 85.00, 8.00),
                                                                                                     (4, 'ESPECIALIDAD', 90.00, 8.00),
                                                                                                     (4, 'PROCEDIMIENTO', 75.00, 8.00),
                                                                                                     (4, 'EXAMEN_LABORATORIO', 95.00, 5.00),
                                                                                                     (4, 'IMAGENES_DIAGNOSTICO', 90.00, 8.00);

INSERT INTO patients (doctor_id, nombre_completo, tipo_identificacion, numero_identificacion, fecha_nacimiento, telefono, email, direccion, ars_id, numero_seguro) VALUES
                                                                                                                                                                       (1, 'Carlos Martínez', 'CEDULA', '00198765432', '1985-05-15', '809-555-1001', 'carlos.martinez@email.com', 'Calle Paciente #1, Santo Domingo', 1, 'HUM-12345'),
                                                                                                                                                                       (1, 'Ana García', 'CEDULA', '00198765433', '1990-08-22', '809-555-1002', 'ana.garcia@email.com', 'Calle Paciente #2, Santo Domingo', 2, 'PAL-67890'),
                                                                                                                                                                       (1, 'Pedro López', 'CEDULA', '00198765434', '1978-03-10', '809-555-1003', 'pedro.lopez@email.com', 'Calle Paciente #3, Santo Domingo', 1, 'HUM-54321');

INSERT INTO consultations (doctor_id, patient_id, tipo_servicio, estado, diagnostico, observaciones, monto_total, monto_cubierto_ars, copago_paciente, fecha_consulta) VALUES
                                                                                                                                                                           (1, 1, 'CONSULTA_GENERAL', 'COMPLETADA', 'Gripe estacional', 'Paciente presentando síntomas de gripe. Recomendado reposo y medicamentos.', 2000.00, 1600.00, 400.00, CURRENT_TIMESTAMP - INTERVAL '1 day'),
                                                                                                                                                                           (1, 2, 'ESPECIALIDAD', 'COMPLETADA', 'Dolor abdominal recurrente', 'Paciente con antecedentes de gastritis. Se recomienda dieta especial y seguimiento.', 3500.00, 2625.00, 875.00, CURRENT_TIMESTAMP - INTERVAL '2 days');

INSERT INTO patient_invoices (doctor_id, patient_id, consultation_id, ncf, monto_total, monto_itbis, monto_neto, tipo_pagador, estado_dgii, mensaje_dgii, fecha_emision) VALUES
                                                                                                                                                                             (1, 1, 1, 'A0213149629300000010001', 400.00, 0.00, 400.00, 'PACIENTE', 'APROBADA', 'Factura aprobada por DGII. Número de referencia: DGII-1234567890', CURRENT_TIMESTAMP - INTERVAL '1 day'),
                                                                                                                                                                             (1, 2, 2, 'A0213149629300000020001', 875.00, 0.00, 875.00, 'PACIENTE', 'APROBADA', 'Factura aprobada por DGII. Número de referencia: DGII-1234567891', CURRENT_TIMESTAMP - INTERVAL '2 days');

INSERT INTO system_config (clave, valor, descripcion) VALUES
                                                          ('version', '1.0.0', 'Versión del sistema'),
                                                          ('nombre_sistema', 'MPS ERP', 'Nombre del sistema'),
                                                          ('pais', 'Republica Dominicana', 'País donde opera el sistema'),
                                                          ('dgii_simulador', 'true', 'Habilitar simulador de DGII para pruebas');
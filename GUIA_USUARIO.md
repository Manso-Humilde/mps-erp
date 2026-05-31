MPS ERP - Guía del Usuario
Sistema de Facturación Médica para República Dominicana
📋 Tabla de Contenido
Introducción
Requisitos del Sistema
Instalación y Configuración
Iniciar Sesión
Dashboard
Gestión de Pacientes
Gestión de Consultas Médicas
Facturación a Pacientes
Cumplimiento DGII
Reportes y Estadísticas
Solución de Problemas
Soporte
🎯 Introducción
MPS ERP es un sistema completo de facturación médica diseñado específicamente para cumplir con las normativas de la Dirección General de Impuestos Internos (DGII) de República Dominicana.

Características Principales
✅ Validación de RNC/Cédula (Módulo 11)
✅ Generación automática de NCF (Número de Comprobante Fiscal)
✅ Cálculo automático de copagos ARS
✅ Retención de ISR (10% sobre comprobantes > RD$50,000)
✅ Cálculo de ITBIS (18%)
✅ Generación de XML e-CF
✅ Gestión de pacientes y expedientes médicos
✅ Gestión de consultas y servicios
✅ Facturación de pacientes y reclamaciones ARS
✅ Dashboard con métricas en tiempo real
💻 Requisitos del Sistema
Para el Backend (Spring Boot)
Java 17 o superior
Maven 3.6+
PostgreSQL 12+ (o Docker)
Mínimo 4GB de RAM
2GB de espacio en disco
Para el Frontend (Vue 3)
Navegador moderno (Chrome, Firefox, Edge, Safari)
Conexión a internet (para carga de recursos CDN)
Requisitos de Red
El backend debe ejecutarse en puerto 8080
El frontend debe ejecutarse en puerto 3000
Ambos servicios deben estar en la misma red
🚀 Instalación y Configuración
Paso 1: Iniciar la Base de Datos PostgreSQL
Si tiene Docker instalado, ejecute:

cd C:\Users\denis\Desktop\SaaP Z\mps-erpdocker-compose up -d
Esto iniciará el contenedor de PostgreSQL con los datos de configuración y usuarios de prueba.

Paso 2: Iniciar el Backend (Spring Boot)
Abra una terminal en la carpeta del proyecto y ejecute:

bash

cd C:\Users\denis\Desktop\SaaP Z\mps-erp
mvn spring-boot:run
Espere a ver el mensaje:

text

Started MpsErpApplication in X seconds
El backend estará disponible en: http://localhost:8080

Paso 3: Iniciar el Frontend (Vue 3)
Abra otra terminal en la carpeta frontend:

bash

cd C:\Users\denis\Desktop\SaaP Z\mps-erp\frontend
npm install
npm run dev
El frontend estará disponible en: http://localhost:3000

🔑 Iniciar Sesión
Acceder al Sistema
Abra su navegador y vaya a: http://localhost:3000
Verá la pantalla de inicio de sesión
Cuentas de Usuario de Prueba
El sistema viene con 4 usuarios preconfigurados para diferentes roles:

Usuario
Contraseña
Rol
Permisos
admin	admin123	SUPER_ADMIN	Acceso total a todas las funciones
contable	contable123	ADMIN_CONTABLE	Facturación, reportes, configuración
medico	medico123	MÉDICO	Pacientes, consultas, expedientes
asistente	asistente123	ASISTENTE	Pacientes, registro de consultas

Recuperación de Contraseña
Si olvida su contraseña, contacte al administrador del sistema para restablecerla desde la base de datos.

📊 Dashboard
El Dashboard es la pantalla principal después de iniciar sesión. Muestra:

Métricas Principales
Total Ingresos del Mes: Suma de todas las facturas pagadas en el mes actual
Total Ingresos del Año: Acumulativo anual de ingresos
Total Copagos: Monto total recaudado por copagos de pacientes
Reclamaciones Pendientes: Total de facturas ARS por cobrar
Estadísticas
Total Pacientes: Número de pacientes registrados en el sistema
Total Consultas del Mes: Consultas atendidas en el mes actual
Actividad Reciente
Últimas 5 consultas registradas
Últimas 5 facturas emitidas
👥 Gestión de Pacientes
Registrar Nuevo Paciente
Haga clic en "Pacientes" en el menú lateral
Haga clic en el botón "Nuevo Paciente"
Complete el formulario con los datos del paciente:
Datos Personales
Nombre Completo: Nombre completo del paciente
Tipo de Identificación: Seleccione RNC o Cédula
Número de Identificación: Ingrese el RNC o Cédula (se valida automáticamente con Módulo 11)
Fecha de Nacimiento: Seleccione la fecha
Sexo: Seleccione Masculino o Femenino
Teléfono: Número de contacto
Email: Correo electrónico (opcional)
Dirección: Dirección física
Seguro Médico
ARS: Seleccione la aseguradora de la lista:
ARS Humano
ARS Palic Salud
ARS Universal
ARS Senasa
Otro
Número de Seguro: Número de afiliación del paciente
Tipo de Persona: Particular o Empresa
Haga clic en "Guardar"
Buscar Paciente
Use el campo de búsqueda para encontrar pacientes por:

Nombre
Número de identificación
Número de seguro
Ver Historial del Paciente
Haga clic en el icono de "ver" en la tabla de pacientes
Se mostrará el historial completo de:
Consultas médicas
Facturas emitidas
Pagos realizados
Editar Paciente
Haga clic en el icono "editar" en la tabla
Modifique los datos necesarios
Haga clic en "Actualizar"
Dar de Baja a Paciente
Haga clic en el icono "eliminar" en la tabla
Confirme la acción
El paciente se marcará como inactivo (no se eliminará completamente)
🏥 Gestión de Consultas Médicas
Registrar Nueva Consulta
Haga clic en "Consultas" en el menú lateral
Haga clic en "Nueva Consulta"
Complete el formulario:
Datos de la Consulta
Paciente: Seleccione el paciente de la lista
Tipo de Servicio: Seleccione el tipo:
CONSULTA_GENERAL
CONSULTA_ESPECIALIDAD
EXAMEN_LABORATORIO
IMAGENOLOGIA
PROCEDIMIENTO
URGENCIAS
OTRO
Diagnóstico: Descripción del diagnóstico médico
Observaciones: Notas adicionales
Monto Total: Costo total del servicio
El sistema calcula automáticamente:
Cobertura ARS: Según el porcentaje de cobertura de la aseguradora
Copago: Monto que debe pagar el paciente (Monto Total - Cobertura ARS)
Haga clic en "Guardar"
Cálculos Automáticos
text

Monto Cubierto por ARS = Monto Total × % Cobertura ARS
Copago del Paciente = Monto Total - Monto Cubierto por ARS
Ejemplo:

Monto Total: RD$2,000
Cobertura ARS: 80%
Monto Cubierto: RD$1,600
Copago: RD$400
Ver Consultas
La tabla muestra todas las consultas con:

Fecha y hora
Nombre del paciente
Tipo de servicio
Diagnóstico
Monto total
Estado (PENDIENTE, COMPLETADA, CANCELADA)
Filtrar Consultas
Use los filtros para buscar consultas por:

Rango de fechas
Tipo de servicio
Estado
Paciente específico
💰 Facturación a Pacientes
Generar Nueva Factura
Haga clic en "Facturas" en el menú lateral
Haga clic en "Nueva Factura"
Seleccione la consulta a facturar:
Los datos se cargarán automáticamente
Complete los datos de facturación:
Tipo de Pago
Paciente Particular: Factura directa al paciente
ARS: Factura para reclamación a aseguradora
Datos de la Factura
NCF: Se genera automáticamente según la normativa DGII
Subtotal: Monto antes de impuestos
ITBIS: 18% del subtotal (se calcula automáticamente)
Total: Subtotal + ITBIS
Retención ISR (si aplica)
Se aplica automáticamente cuando el total de la factura supera RD$50,000
Monto: 10% del total
Haga clic en "Generar Factura"
NCF (Número de Comprobante Fiscal)
El sistema genera NCFs automáticamente según el tipo:

Prefijo
Descripción
B01	Crédito Fiscal
B02	Gasto Menor
B03	Comprobante de Compras
B04	Regímenes Especiales
B15	Comprobante Único Ingresos
B16	Gubernamental
B17	Exportaciones

Ver Factura
Haga clic en el icono "ver" en la tabla
Se mostrará el detalle de la factura:
Datos del paciente
Detalles del servicio
Desglose de montos
NCF
Estado DGII
Descargar XML e-CF
Para obtener el archivo XML compatible con DGII:

Haga clic en "Descargar XML" en la factura
El archivo se descargará con formato: eCF-{NCF}.xml
Este archivo puede ser enviado a DGII o compartido con el paciente
Anular Factura
⚠️ Precaución: Solo se pueden anular facturas en estado PENDIENTE.

Haga clic en "Anular" en la factura
Seleccione el motivo de anulación
El sistema generará un NCF de anulación automáticamente
📋 Cumplimiento DGII
Validación de RNC/Cédula (Módulo 11)
El sistema valida automáticamente todos los RNC y Cédulas usando el algoritmo Módulo 11 de DGII.

Si el documento es inválido:

Se mostrará un mensaje de error
No se podrá guardar el registro hasta corregir
Generación de XML e-CF
El sistema genera XMLs compatibles con el formato e-CF (Comprobante Fiscal Electrónico) de DGII.

El XML incluye:

Datos del emisor (médico/empresa)
Datos del receptor (paciente/ARS)
Detalles de la transacción
Cálculos de impuestos
Firma digital (simulada)
Reportes Fiscales
Reporte Mensual de Ingresos
Total facturado en el mes
ITBIS cobrado
ISR retenido
Facturas por tipo de NCF
Reporte de Reclamaciones ARS
Facturas emitidas a ARS
Montos reclamados
Montos pagados
Saldo pendiente
📈 Reportes y Estadísticas
Dashboard en Tiempo Real
El dashboard se actualiza automáticamente y muestra:

Métricas del mes actual
Tendencias comparativas
Alertas de reclamaciones pendientes
Exportar Datos
Puede exportar datos en formato:

CSV: Compatible con Excel
PDF: Para impresión o archivo
🔧 Solución de Problemas
Problema: No puedo iniciar sesión
Soluciones:

Verifique que el backend esté corriendo en el puerto 8080
Verifique que esté usando las credenciales correctas
Limpie el cache del navegador
Verifique la consola del navegador (F12) para errores
Problema: El sistema dice "Error de conexión"
Soluciones:

Verifique que el backend esté ejecutándose
Verifique que no haya firewall bloqueando el puerto 8080
Reinicie el backend con: mvn spring-boot:run
Problema: Los cálculos de copago son incorrectos
Soluciones:

Verifique la configuración de cobertura del ARS
Asegúrese de que el paciente tenga el ARS correcto asignado
Revise el porcentaje de cobertura en la configuración del ARS
Problema: No puedo generar NCF
Soluciones:

Verifique que el NCF esté configurado en el sistema
Contacte al administrador para verificar la secuencia de NCF
Asegúrese de que los datos del paciente/ARS estén completos
Problema: La validación de RNC/Cédula falla
Soluciones:

Verifique que el número sea correcto
Asegúrese de que el tipo de identificación coincida (RNC o Cédula)
Verifique que no haya espacios ni caracteres especiales
Problema: El XML no se descarga
Soluciones:

Verifique que tenga permisos para descargar archivos
Desactive el bloqueador de pop-ups
Intente en otro navegador
📞 Soporte
Contacto
Para soporte técnico o preguntas, contacte:

Email: soporte@mps-erp.com
Teléfono: +1 (809) 555-0123
Horario: Lunes a Viernes, 8:00 AM - 5:00 PM
Recursos Adicionales
Documentación DGII: www.dgii.gov.do
Normativas e-CF: Consulte la normativa oficial de DGII
Manuales Técnicos: Disponibles en el portal del cliente
📝 Notas Importantes
Seguridad
Nunca comparta sus credenciales de acceso
Cambie su contraseña periódicamente
Cierre sesión al terminar su trabajo
Backup de Datos
El sistema realiza copias de seguridad automáticamente:

Diario: Backup incremental
Semanal: Backup completo
Los backups se almacenan en la carpeta /db/backups
Actualizaciones
El sistema notificará cuando haya actualizaciones disponibles. Siga las instrucciones para mantener su sistema actualizado.

📌 Atajos de Teclado
Acción
Atajo
Nueva Consulta	Alt + C
Nuevo Paciente	Alt + P
Nueva Factura	Alt + F
Buscar	Ctrl + B
Dashboard	Alt + D
Cerrar Sesión	Alt + L

⚖️ Licencia
MPS ERP © 2024 - Todos los derechos reservados.

Este software está diseñado para uso profesional en el sector médico de República Dominicana, cumpliendo con todas las normativas de DGII.

Versión del Sistema: 1.0.0
Última Actualización: Enero 2024
Estado del Documento: Final
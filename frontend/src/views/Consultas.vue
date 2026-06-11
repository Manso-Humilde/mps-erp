<template>
  <div class="content">
    <div class="page-header">
      <h1>Gestión de Consultas</h1>
      <Button
        label="Nueva Consulta"
        icon="pi pi-plus"
        @click="openNewDialog"
        class="btn-primary"
      />
    </div>

        <div v-if="loading" class="loading-container">
          <ProgressSpinner />
        </div>

        <div v-else class="table-container">
          <DataTable
            :value="consultas"
            stripedRows
            :paginator="true"
            :rows="10"
            class="p-datatable-sm"
          >
            <Column field="createdAt" header="Fecha" sortable>
              <template #body="slotProps">
                {{ slotProps.data.createdAt ? formatDateTime(slotProps.data.createdAt) : '' }}
              </template>
            </Column>
            <Column field="patientName" header="Paciente" sortable />
            <Column field="tipoServicio.nombre" header="Tipo Servicio" sortable>
              <template #body="slotProps">
                {{ slotProps.data.tipoServicio ? slotProps.data.tipoServicio.nombre : '' }}
              </template>
            </Column>
            <Column field="diagnostico" header="Diagnóstico" />
            <Column field="montoTotal" header="Monto Total" sortable>
              <template #body="slotProps">
                RD$ {{ slotProps.data.montoTotal ? slotProps.data.montoTotal.toFixed(2) : '0.00' }}
              </template>
            </Column>
            <Column field="copago" header="Copago" sortable>
              <template #body="slotProps">
                RD$ {{ slotProps.data.copago ? slotProps.data.copago.toFixed(2) : '0.00' }}
              </template>
            </Column>
            <Column field="status" header="Estado" sortable>
              <template #body="slotProps">
                <Tag
                  :value="slotProps.data.status"
                  :severity="getEstadoSeverity(slotProps.data.status)"
                />
              </template>
            </Column>
            <Column header="Acciones">
              <template #body="slotProps">
                <Button
                  icon="pi pi-receipt"
                  label="Facturar"
                  rounded
                  text
                  @click="generarFactura(slotProps.data)"
                />
              </template>
            </Column>
          </DataTable>
        </div>

        <Dialog
          v-model:visible="showDialog"
          header="Nueva Consulta"
          :style="{ width: '700px' }"
          :modal="true"
        >
        <form @submit.prevent="saveConsulta" class="consulta-form">
          <div class="form-row">
            <div class="form-group full-width">
              <label>Paciente *</label>
              <Dropdown
                v-model="form.patientId"
                :options="pacientes"
                optionLabel="nombreCompleto"
                optionValue="id"
                filter
                required
                placeholder="Seleccionar paciente..."
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Tipo Servicio *</label>
              <Dropdown
                v-model="form.tipoServicio"
                :options="tiposServicio"
                optionLabel="nombre"
                optionValue="nombre"
                required
              />
            </div>
            <div class="form-group">
              <label>Monto Total (RD$) *</label>
              <InputNumber
                v-model="form.montoTotal"
                :minFractionDigits="2"
                :maxFractionDigits="2"
                mode="decimal"
                required
              />
            </div>
          </div>

          <div class="form-group">
            <label>Diagnóstico *</label>
            <Textarea v-model="form.diagnostico" rows="3" required />
          </div>

          <div class="form-group">
            <label>Observaciones</label>
            <Textarea v-model="form.observaciones" rows="2" />
          </div>

          <div class="form-info">
            <div class="info-box">
              <span class="info-label">Cobertura ARS:</span>
              <span class="info-value">{{ form.porcentajeCoberturaArs }}%</span>
            </div>
            <div class="info-box">
              <span class="info-label">Cubierto por ARS:</span>
              <span class="info-value">RD$ {{ calcularCubierto().toFixed(2) }}</span>
            </div>
            <div class="info-box">
              <span class="info-label">Copago Paciente:</span>
              <span class="info-value">RD$ {{ calcularCopago().toFixed(2) }}</span>
            </div>
          </div>

          <div class="form-actions">
            <Button
              label="Cancelar"
              severity="secondary"
              @click="closeDialog"
            />
            <Button
              type="submit"
              label="Guardar"
              :loading="saving"
            />
          </div>
        </form>
      </Dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import Dropdown from 'primevue/dropdown';
import Textarea from 'primevue/textarea';
import InputNumber from 'primevue/inputnumber';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import { consultasService } from '../services/consultas';
import { pacientesService } from '../services/pacientes';
import { facturasService } from '../services/facturas';
import api from '../services/api';

const route = useRoute();
const router = useRouter();
const consultas = ref([]);
const pacientes = ref([]);
const tiposServicio = ref([]);
const loading = ref(false);
const saving = ref(false);
const showDialog = ref(false);
const appointmentId = ref(null);

const form = ref({
  patientId: null,
  tipoServicio: null,
  montoTotal: 0,
  diagnostico: '',
  observaciones: '',
  porcentajeCoberturaArs: 80
});

const openNewDialog = () => {
  resetForm();
  showDialog.value = true;
};

const loadConsultas = async () => {
  loading.value = true;
  try {
    consultas.value = await consultasService.getAll();
  } catch (error) {
    console.error('Error al cargar consultas:', error);
  } finally {
    loading.value = false;
  }
};

const loadPacientes = async () => {
  try {
    pacientes.value = await pacientesService.getAll();
  } catch (error) {
    console.error('Error al cargar pacientes:', error);
  }
};

const loadTiposServicio = async () => {
  try {
    const response = await api.get('/service-types');
    tiposServicio.value = response.data;
    console.log('Tipos de servicio cargados:', tiposServicio.value);
  } catch (error) {
    console.error('Error al cargar tipos de servicio:', error);
  }
};

const getServiceTypeIdByName = (nombre) => {
  const serviceType = tiposServicio.value.find(t => t.nombre === nombre);
  return serviceType ? serviceType.id : null;
};

const calcularCubierto = () => {
  return form.value.montoTotal * (form.value.porcentajeCoberturaArs / 100);
};

const calcularCopago = () => {
  return form.value.montoTotal - calcularCubierto();
};

const saveConsulta = async () => {
  saving.value = true;
  try {
    const serviceTypeId = getServiceTypeIdByName(form.value.tipoServicio);

    const payload = {
      patientId: form.value.patientId,
      serviceTypeId: serviceTypeId,
      montoTotal: form.value.montoTotal,
      diagnostico: form.value.diagnostico,
      observaciones: form.value.observaciones,
      porcentajeCoberturaArs: form.value.porcentajeCoberturaArs
    };

    console.log('Payload enviado:', payload);

    const consultation = await consultasService.create(payload);

    // Si viene de una cita, actualizar la cita con el ID de consulta
    if (appointmentId.value) {
      try {
        await api.put(`/appointments/${appointmentId.value}/consultation`, {
          consultationId: consultation.id
        });
      } catch (error) {
        console.error('Error al actualizar cita:', error);
      }
    }

    showDialog.value = false;
    resetForm();
    await loadConsultas();

    // Limpiar query params y redirigir
    router.replace({ query: {} });
    appointmentId.value = null;

  } catch (error) {
    console.error('Error al guardar consulta:', error);
    const errorMsg = error.response?.data?.message || 'Error al guardar consulta';
    alert(errorMsg);
  } finally {
    saving.value = false;
  }
};

const resetForm = () => {
  form.value = {
    patientId: null,
    tipoServicio: null,
    montoTotal: 0,
    diagnostico: '',
    observaciones: '',
    porcentajeCoberturaArs: 80
  };
};

const closeDialog = () => {
  showDialog.value = false;
  resetForm();
  router.replace({ query: {} });
  appointmentId.value = null;
};

const generarFactura = async (consulta) => {
  try {
    const invoice = await facturasService.createFacturaPaciente(consulta.id);
    window.open(`https://mps-erp.onrender.com/invoices/patient/${invoice.id}/pdf`, '_blank');
    await loadConsultas();
  } catch (error) {
    console.error('Error al generar factura:', error);
    alert('Error al generar factura');
  }
};

const formatDateTime = (date) => {
  return new Date(date).toLocaleString('es-DO', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const getEstadoSeverity = (estado) => {
  const severityMap = {
    'PENDIENTE': 'warn',
    'EN_PROGRESO': 'info',
    'COMPLETADA': 'success',
    'CANCELADA': 'danger'
  };
  return severityMap[estado] || 'info';
};

const preloadFromAppointment = () => {
  const query = route.query;
  if (query.patientId) {
    form.value.patientId = parseInt(query.patientId);
    form.value.diagnostico = query.motivo || '';
    if (query.serviceTypeName) {
      form.value.tipoServicio = query.serviceTypeName;
    }
    appointmentId.value = query.appointmentId ? parseInt(query.appointmentId) : null;
    showDialog.value = true;
  }
};

onMounted(() => {
  loadConsultas();
  loadPacientes();
  loadTiposServicio();
  preloadFromAppointment();
});
</script>

<style scoped>
.content {
  padding: 30px;
  flex: 1;
  background: #f4f7f6;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  color: #8B4513;
  margin: 0;
}

.btn-primary {
  background: linear-gradient(135deg, #8B4513, #A0522D);
  border: none;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #A0522D, #D2691E);
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.consulta-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.form-group.full-width {
  grid-column: span 2;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 0.9rem;
}

.form-info {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin: 15px 0;
}

.info-box {
  background: #f0f0f0;
  padding: 15px;
  border-radius: 8px;
  text-align: center;
}

.info-label {
  display: block;
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 5px;
}

.info-value {
  display: block;
  font-size: 1.1rem;
  font-weight: 700;
  color: #8B4513;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
<template>
  <div class="content">
    <div class="page-header">
      <h1>Gestión de Citas</h1>
      <Button
        label="Nueva Cita"
        icon="pi pi-plus"
        @click="openNewDialog"
        class="btn-primary"
      />
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <div class="table-scroll-wrapper">
        <DataTable
          :value="citas"
          stripedRows
          :paginator="true"
          :rows="10"
          :rowsPerPageOptions="[10, 20, 50]"
          :scrollable="true"
          scrollHeight="flex"
          v-model:filters="filters"
          filterDisplay="menu"
          class="p-datatable-sm professional-table"
          tableStyle="min-width: 1200px"
        >
          <Column field="id" header="ID" sortable style="min-width: 80px" />
          <Column field="patientName" header="Paciente" sortable style="min-width: 200px">
            <template #filter="{ filterModel, filterCallback }">
              <InputText
                v-model="filterModel.value"
                @input="filterCallback()"
                placeholder="Buscar..."
              />
            </template>
          </Column>
          <Column field="doctorName" header="Médico" sortable style="min-width: 150px" />
          <Column field="serviceTypeName" header="Servicio" sortable style="min-width: 150px" />
          <Column field="fechaHoraInicio" header="Fecha y Hora" sortable style="min-width: 180px">
            <template #body="slotProps">
              {{ formatDateTime(slotProps.data.fechaHoraInicio) }}
            </template>
          </Column>
          <Column field="status" header="Estado" sortable style="min-width: 120px">
            <template #body="slotProps">
              <Tag
                :value="getStatusLabel(slotProps.data.status)"
                :severity="getStatusSeverity(slotProps.data.status)"
              />
            </template>
          </Column>
          <Column field="motivo" header="Motivo" style="min-width: 200px" />
          <Column header="Acciones" style="min-width: 150px">
            <template #body="slotProps">
              <Button
                icon="pi pi-check"
                rounded
                text
                @click="atenderCita(slotProps.data)"
                tooltip="Atender"
                :disabled="slotProps.data.status !== 'PENDIENTE' && slotProps.data.status !== 'CONFIRMADA'"
              />
              <Button
                icon="pi pi-thumbs-down"
                rounded
                text
                @click="marcarNoAsistio(slotProps.data)"
                tooltip="No Asistió"
                :disabled="slotProps.data.status !== 'PENDIENTE' && slotProps.data.status !== 'CONFIRMADA'"
              />
              <Button
                icon="pi pi-ban"
                rounded
                text
                severity="danger"
                @click="cancelarCita(slotProps.data)"
                tooltip="Cancelar"
                :disabled="slotProps.data.status !== 'PENDIENTE' && slotProps.data.status !== 'CONFIRMADA'"
              />
            </template>
          </Column>
        </DataTable>
      </div>
    </div>

    <!-- Diálogo para Nueva Cita -->
    <Dialog
      v-model:visible="showFormDialog"
      header="Nueva Cita"
      :style="{ width: '600px' }"
      :modal="true"
    >
      <form @submit.prevent="saveCita" class="cita-form">
        <div class="form-row">
          <div class="form-group">
            <label>Paciente *</label>
            <Dropdown
              v-model="form.patientId"
              :options="pacientes"
              optionLabel="nombreCompleto"
              optionValue="id"
              placeholder="Seleccione paciente"
              required
              filter
              showClear
            />
          </div>
          <div class="form-group">
            <label>Médico</label>
            <InputText :value="doctorNombre" disabled />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Tipo de Servicio *</label>
            <Dropdown
              v-model="form.serviceTypeId"
              :options="tiposServicio"
              optionLabel="nombre"
              optionValue="id"
              placeholder="Seleccione servicio"
              required
            />
          </div>
          <div class="form-group">
            <label>Fecha y Hora *</label>
            <Calendar
              v-model="form.fechaHoraInicio"
              dateFormat="yy-mm-dd"
              showTime
              hourFormat="24"
              :minDate="new Date()"
              required
            />
          </div>
        </div>

        <div class="form-group">
          <label>Motivo</label>
          <InputText v-model="form.motivo" />
        </div>

        <div class="form-group">
          <label>Notas</label>
          <Textarea v-model="form.notas" rows="3" />
        </div>

        <div class="form-actions">
          <Button
            label="Cancelar"
            severity="secondary"
            @click="closeFormDialog"
          />
          <Button
            type="submit"
            label="Guardar"
            :loading="saving"
          />
        </div>
      </form>
    </Dialog>

    <!-- Diálogo para Confirmar Acciones -->
    <Dialog
      v-model:visible="showConfirmDialog"
      header="Confirmar Acción"
      :style="{ width: '400px' }"
      :modal="true"
    >
      <p>{{ confirmMessage }}</p>
      <template #footer>
        <Button label="No" severity="secondary" @click="showConfirmDialog = false" />
        <Button label="Sí" severity="danger" @click="confirmAction" />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import Calendar from 'primevue/calendar';
import Textarea from 'primevue/textarea';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const router = useRouter();
const authStore = useAuthStore();
const citas = ref([]);
const pacientes = ref([]);
const tiposServicio = ref([]);
const loading = ref(false);
const saving = ref(false);
const showFormDialog = ref(false);
const showConfirmDialog = ref(false);
const filters = ref({});
const confirmActionFn = ref(null);
const confirmMessage = ref('');

const doctorNombre = ref(authStore.user?.nombreCompleto || 'Médico actual');
const doctorId = ref(3); // Cambia por el ID de tu médico de la tabla doctors

const form = ref({
  patientId: null,
  serviceTypeId: null,
  fechaHoraInicio: null,
  motivo: '',
  notas: ''
});

const getStatusLabel = (status) => {
  const labels = {
    PENDIENTE: 'Pendiente',
    CONFIRMADA: 'Confirmada',
    ATENDIDA: 'Atendida',
    CANCELADA: 'Cancelada',
    NO_ASISTIO: 'No Asistió'
  };
  return labels[status] || status;
};

const getStatusSeverity = (status) => {
  const severities = {
    PENDIENTE: 'warning',
    CONFIRMADA: 'info',
    ATENDIDA: 'success',
    CANCELADA: 'danger',
    NO_ASISTIO: 'danger'
  };
  return severities[status] || 'secondary';
};

const loadCitas = async () => {
  loading.value = true;
  try {
    const now = new Date();
    const response = await api.get('/appointments', {
      params: {
        year: now.getFullYear(),
        month: now.getMonth() + 1
      }
    });
    citas.value = response.data;
  } catch (error) {
    console.error('Error al cargar citas:', error);
  } finally {
    loading.value = false;
  }
};

const loadPacientes = async () => {
  try {
    const response = await api.get('/patients');
    pacientes.value = response.data;
  } catch (error) {
    console.error('Error al cargar pacientes:', error);
  }
};

const loadTiposServicio = async () => {
  try {
    const response = await api.get('/service-types');
    tiposServicio.value = response.data;
  } catch (error) {
    console.error('Error al cargar tipos de servicio:', error);
  }
};

const openNewDialog = () => {
  resetForm();
  showFormDialog.value = true;
};

const saveCita = async () => {
  saving.value = true;
  try {
    const payload = {
      patientId: form.value.patientId,
      doctorId: doctorId.value,
      serviceTypeId: form.value.serviceTypeId,
      fechaHoraInicio: form.value.fechaHoraInicio.toISOString(),
      motivo: form.value.motivo,
      notas: form.value.notas
    };

    await api.post('/appointments', payload);

    showFormDialog.value = false;
    resetForm();
    await loadCitas();
  } catch (error) {
    console.error('Error al guardar cita:', error);
    const errorMsg = error.response?.data?.message || 'Error al guardar cita';
    alert(errorMsg);
  } finally {
    saving.value = false;
  }
};

const atenderCita = (cita) => {
  router.push({
    path: '/consultas',
    query: {
      patientId: cita.patientId,
      patientName: cita.patientName,
      serviceTypeId: cita.serviceTypeId,
      serviceTypeName: cita.serviceTypeName,
      motivo: cita.motivo,
      appointmentId: cita.id
    }
  });
};

const marcarNoAsistio = (cita) => {
  confirmMessage.value = `¿Marcar como "No Asistió" la cita de ${cita.patientName}?`;
  confirmActionFn.value = async () => {
    try {
      await api.put(`/appointments/${cita.id}/status?status=NO_ASISTIO`);
      await loadCitas();
      showConfirmDialog.value = false;
    } catch (error) {
      console.error('Error al marcar no asistió:', error);
      alert(error.response?.data?.message || 'Error al marcar no asistió');
    }
  };
  showConfirmDialog.value = true;
};

const cancelarCita = (cita) => {
  confirmMessage.value = `¿Cancelar cita de ${cita.patientName}?`;
  confirmActionFn.value = async () => {
    try {
      await api.put(`/appointments/${cita.id}/status?status=CANCELADA`);
      await loadCitas();
      showConfirmDialog.value = false;
    } catch (error) {
      console.error('Error al cancelar cita:', error);
      alert(error.response?.data?.message || 'Error al cancelar cita');
    }
  };
  showConfirmDialog.value = true;
};

const confirmAction = async () => {
  if (confirmActionFn.value) {
    await confirmActionFn.value();
  }
};

const resetForm = () => {
  form.value = {
    patientId: null,
    serviceTypeId: null,
    fechaHoraInicio: null,
    motivo: '',
    notas: ''
  };
};

const closeFormDialog = () => {
  showFormDialog.value = false;
  resetForm();
};

const formatDateTime = (date) => {
  if (!date) return 'N/A';
  return new Date(date).toLocaleString('es-DO', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

onMounted(() => {
  loadCitas();
  loadPacientes();
  loadTiposServicio();
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
  flex-wrap: wrap;
  gap: 15px;
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

.table-scroll-wrapper {
  width: 100%;
  overflow-x: auto;
  overflow-y: visible;
  border-radius: 8px;
}

.professional-table {
  width: 100%;
}

.cita-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
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

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .content {
    padding: 15px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .table-container {
    padding: 12px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
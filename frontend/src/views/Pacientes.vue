<template>
  <div class="content">
    <div class="page-header">
      <h1>Gestión de Pacientes</h1>
      <Button
        label="Nuevo Paciente"
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
          :value="pacientes"
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
          <Column field="nombreCompleto" header="Nombre Completo" sortable style="min-width: 200px">
            <template #filter="{ filterModel, filterCallback }">
              <InputText
                v-model="filterModel.value"
                @input="filterCallback()"
                placeholder="Buscar..."
              />
            </template>
          </Column>
          <Column field="numeroIdentificacion" header="Identificación" sortable style="min-width: 150px" />
          <Column field="arsNombre" header="ARS" sortable style="min-width: 150px" />
          <Column field="telefono" header="Teléfono" style="min-width: 120px" />
          <Column field="email" header="Email" style="min-width: 200px" />
          <Column field="activo" header="Estado" sortable style="min-width: 100px">
            <template #body="slotProps">
              <Tag
                :value="slotProps.data.activo ? 'Activo' : 'Inactivo'"
                :severity="slotProps.data.activo ? 'success' : 'danger'"
              />
            </template>
          </Column>
          <Column field="createdAt" header="Registrado" sortable style="min-width: 120px">
            <template #body="slotProps">
              {{ formatDate(slotProps.data.createdAt) }}
            </template>
          </Column>
          <Column header="Acciones" style="min-width: 100px">
            <template #body="slotProps">
              <Button
                icon="pi pi-eye"
                rounded
                text
                @click="viewPatient(slotProps.data)"
                tooltip="Ver detalles"
              />
              <Button
                icon="pi pi-pencil"
                rounded
                text
                @click="editPatient(slotProps.data)"
                tooltip="Editar"
              />
            </template>
          </Column>
        </DataTable>
      </div>
    </div>

    <!-- Diálogo para Nuevo/Editar Paciente -->
    <Dialog
      v-model:visible="showFormDialog"
      :header="dialogTitle"
      :style="{ width: '600px' }"
      :modal="true"
    >
      <form @submit.prevent="savePatient" class="patient-form">
        <div class="form-row">
          <div class="form-group">
            <label>Nombre Completo *</label>
            <InputText v-model="form.nombreCompleto" required />
          </div>
          <div class="form-group">
            <label>Tipo Identificación *</label>
            <Dropdown
              v-model="form.tipoIdentificacion"
              :options="tiposIdentificacion"
              optionLabel="displayName"
              optionValue="name"
              required
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Número Identificación *</label>
            <InputText v-model="form.numeroIdentificacion" required />
          </div>
          <div class="form-group">
            <label>Fecha Nacimiento</label>
            <Calendar v-model="form.fechaNacimiento" dateFormat="yy-mm-dd" />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Teléfono</label>
            <InputText v-model="form.telefono" />
          </div>
          <div class="form-group">
            <label>Email</label>
            <InputText v-model="form.email" type="email" />
          </div>
        </div>

        <div class="form-group">
          <label>ARS *</label>
          <Dropdown
            v-model="form.arsId"
            :options="arsList"
            optionLabel="nombre"
            optionValue="id"
            required
          />
        </div>

        <div class="form-group">
          <label>Número Seguro</label>
          <InputText v-model="form.numeroSeguro" />
        </div>

        <div class="form-group">
          <label>Dirección</label>
          <Textarea v-model="form.direccion" rows="3" />
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

    <!-- Diálogo para Ver Paciente (solo lectura) -->
    <Dialog
      v-model:visible="showViewDialog"
      header="Detalles del Paciente"
      :style="{ width: '600px' }"
      :modal="true"
    >
      <div class="view-container">
        <div class="view-row">
          <div class="view-label">Nombre Completo:</div>
          <div class="view-value">{{ viewPatientData.nombreCompleto }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Identificación:</div>
          <div class="view-value">{{ viewPatientData.tipoIdentificacion }} - {{ viewPatientData.numeroIdentificacion }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Fecha Nacimiento:</div>
          <div class="view-value">{{ viewPatientData.fechaNacimiento || 'N/A' }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Teléfono:</div>
          <div class="view-value">{{ viewPatientData.telefono || 'N/A' }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Email:</div>
          <div class="view-value">{{ viewPatientData.email || 'N/A' }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">ARS:</div>
          <div class="view-value">{{ viewPatientData.arsNombre || 'N/A' }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Número Seguro:</div>
          <div class="view-value">{{ viewPatientData.numeroSeguro || 'N/A' }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Dirección:</div>
          <div class="view-value">{{ viewPatientData.direccion || 'N/A' }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Estado:</div>
          <div class="view-value">
            <Tag :value="viewPatientData.activo ? 'Activo' : 'Inactivo'" :severity="viewPatientData.activo ? 'success' : 'danger'" />
          </div>
        </div>
        <div class="view-row">
          <div class="view-label">Registrado:</div>
          <div class="view-value">{{ formatDate(viewPatientData.createdAt) }}</div>
        </div>
      </div>
      <template #footer>
        <Button label="Cerrar" severity="secondary" @click="showViewDialog = false" />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
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
import { pacientesService } from '../services/pacientes';
import api from '../services/api';

const pacientes = ref([]);
const arsList = ref([]);
const loading = ref(false);
const saving = ref(false);
const showFormDialog = ref(false);
const showViewDialog = ref(false);

const isEditing = ref(false);
const editingId = ref(null);
const viewPatientData = ref({});

const filters = ref({
  global: { value: null, matchMode: 'contains' },
  nombreCompleto: { value: null, matchMode: 'contains' }
});

const tiposIdentificacion = ref([
  { name: 'RNC', displayName: 'RNC' },
  { name: 'CEDULA', displayName: 'Cédula' },
  { name: 'PASAPORTE', displayName: 'Pasaporte' }
]);

const form = ref({
  nombreCompleto: '',
  tipoIdentificacion: 'CEDULA',
  numeroIdentificacion: '',
  fechaNacimiento: null,
  telefono: '',
  email: '',
  arsId: null,
  numeroSeguro: '',
  direccion: ''
});

const dialogTitle = computed(() => isEditing.value ? 'Editar Paciente' : 'Nuevo Paciente');

const loadPacientes = async () => {
  loading.value = true;
  try {
    pacientes.value = await pacientesService.getAll();
  } catch (error) {
    console.error('Error al cargar pacientes:', error);
  } finally {
    loading.value = false;
  }
};

const loadARS = async () => {
  try {
    const response = await api.get('/ars');
    arsList.value = response.data;
  } catch (error) {
    console.error('Error al cargar ARS:', error);
  }
};

const openNewDialog = () => {
  isEditing.value = false;
  editingId.value = null;
  resetForm();
  showFormDialog.value = true;
};

const editPatient = (patient) => {
  isEditing.value = true;
  editingId.value = patient.id;
  form.value = {
    nombreCompleto: patient.nombreCompleto || '',
    tipoIdentificacion: patient.tipoIdentificacion || 'CEDULA',
    numeroIdentificacion: patient.numeroIdentificacion || '',
    fechaNacimiento: patient.fechaNacimiento ? new Date(patient.fechaNacimiento) : null,
    telefono: patient.telefono || '',
    email: patient.email || '',
    arsId: patient.arsId || null,
    numeroSeguro: patient.numeroSeguro || '',
    direccion: patient.direccion || ''
  };
  showFormDialog.value = true;
};

const viewPatient = (patient) => {
  viewPatientData.value = patient;
  showViewDialog.value = true;
};

const savePatient = async () => {
  saving.value = true;
  try {
    const payload = {
      ...form.value,
      arsId: form.value.arsId ? parseInt(form.value.arsId) : 1
    };

    if (isEditing.value && editingId.value) {
      await pacientesService.update(editingId.value, payload);
    } else {
      await pacientesService.create(payload);
    }

    showFormDialog.value = false;
    resetForm();
    await loadPacientes();
  } catch (error) {
    console.error('Error al guardar paciente:', error);
    const errorMsg = error.response?.data?.message || 'Error al guardar paciente';
    alert(errorMsg);
  } finally {
    saving.value = false;
  }
};

const resetForm = () => {
  form.value = {
    nombreCompleto: '',
    tipoIdentificacion: 'CEDULA',
    numeroIdentificacion: '',
    fechaNacimiento: null,
    telefono: '',
    email: '',
    arsId: null,
    numeroSeguro: '',
    direccion: ''
  };
};

const closeFormDialog = () => {
  showFormDialog.value = false;
  resetForm();
};

const formatDate = (date) => {
  if (!date) return 'N/A';
  return new Date(date).toLocaleDateString('es-DO', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

onMounted(() => {
  loadPacientes();
  loadARS();
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

.patient-form {
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

/* Vista de detalles */
.view-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.view-row {
  display: flex;
  border-bottom: 1px solid #e5e7eb;
  padding-bottom: 8px;
}

.view-label {
  width: 140px;
  font-weight: 600;
  color: #666;
}

.view-value {
  flex: 1;
  color: #333;
}

/* Responsive */
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

  .view-row {
    flex-direction: column;
  }

  .view-label {
    width: 100%;
    margin-bottom: 4px;
  }
}
</style>

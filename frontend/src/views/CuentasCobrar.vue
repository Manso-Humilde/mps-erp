<template>
  <div class="content">
    <div class="page-header">
      <h1>Cuentas por Cobrar</h1>
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <DataTable
        :value="receivables"
        stripedRows
        :paginator="true"
        :rows="10"
        v-model:selection="selectedReceivable"
        selectionMode="single"
        @rowSelect="onRowSelect"
      >
        <Column field="id" header="ID" sortable />
        <Column field="tipo" header="Tipo" sortable />
        <Column field="terceroNombre" header="Cliente" sortable />
        <Column field="facturaTipo" header="Factura" sortable />
        <Column field="monto" header="Monto" sortable>
          <template #body="{ data }">
            RD$ {{ formatNumber(data.monto) }}
          </template>
        </Column>
        <Column field="saldoPendiente" header="Saldo Pendiente" sortable>
          <template #body="{ data }">
            RD$ {{ formatNumber(data.saldoPendiente) }}
          </template>
        </Column>
        <Column field="fechaEmision" header="Emisión" sortable />
        <Column field="fechaVencimiento" header="Vencimiento" sortable />
        <Column field="estado" header="Estado" sortable>
          <template #body="{ data }">
            <Tag :value="data.estado" :severity="getEstadoSeverity(data.estado)" />
          </template>
        </Column>
        <Column header="Acciones">
          <template #body="{ data }">
            <Button
              icon="pi pi-dollar"
              rounded
              text
              @click="abrirModalPago(data)"
              :disabled="data.estado === 'PAGADO'"
              tooltip="Registrar Pago"
            />
          </template>
        </Column>
      </DataTable>
    </div>

    <!-- Modal para registrar pago -->
    <Dialog
      v-model:visible="showPagoDialog"
      header="Registrar Pago"
      :style="{ width: '450px' }"
      :modal="true"
    >
      <form @submit.prevent="registrarPago" class="pago-form">
        <div class="form-group">
          <label>Cuenta ID</label>
          <InputText :value="selectedReceivable?.id" disabled />
        </div>
        <div class="form-group">
          <label>Cliente</label>
          <InputText :value="selectedReceivable?.terceroNombre" disabled />
        </div>
        <div class="form-group">
          <label>Saldo Pendiente</label>
          <InputText :value="'RD$ ' + formatNumber(selectedReceivable?.saldoPendiente)" disabled />
        </div>
        <div class="form-group">
          <label>Monto a Pagar *</label>
          <InputNumber
            v-model="pago.monto"
            :max="selectedReceivable?.saldoPendiente"
            :minFractionDigits="2"
            required
          />
        </div>
        <div class="form-group">
          <label>Método de Pago *</label>
          <Dropdown
            v-model="pago.metodoPago"
            :options="metodosPago"
            optionLabel="label"
            optionValue="value"
            placeholder="Seleccione"
            required
          />
        </div>
        <div class="form-group">
          <label>Comprobante (opcional)</label>
          <InputText v-model="pago.referenciaComprobante" />
        </div>
        <div class="form-group">
          <label>Observaciones</label>
          <Textarea v-model="pago.observaciones" rows="2" />
        </div>

        <div class="form-actions">
          <Button label="Cancelar" severity="secondary" @click="showPagoDialog = false" />
          <Button type="submit" label="Registrar Pago" :loading="saving" />
        </div>
      </form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import Textarea from 'primevue/textarea';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const receivables = ref([]);
const selectedReceivable = ref(null);
const loading = ref(false);
const saving = ref(false);
const showPagoDialog = ref(false);

const pago = ref({
  monto: null,
  metodoPago: null,
  referenciaComprobante: '',
  observaciones: ''
});

const metodosPago = ref([
  { label: 'Efectivo', value: 'EFECTIVO' },
  { label: 'Transferencia', value: 'TRANSFERENCIA' },
  { label: 'Cheque', value: 'CHEQUE' }
]);

const formatNumber = (value) => {
  return value?.toFixed(2) || '0.00';
};

const getEstadoSeverity = (estado) => {
  const map = {
    'PENDIENTE': 'danger',
    'PARCIAL': 'warning',
    'PAGADO': 'success'
  };
  return map[estado] || 'info';
};

const loadReceivables = async () => {
  loading.value = true;
  try {
    const response = await api.get('/accounting/receivables/pendientes');
    // Enriquecer con nombres
    for (const item of response.data) {
      if (item.tipo === 'PACIENTE') {
        const paciente = await api.get(`/patients/${item.terceroId}`);
        item.terceroNombre = paciente.data.nombreCompleto;
      } else if (item.tipo === 'ARS') {
        const ars = await api.get(`/ars/${item.terceroId}`);
        item.terceroNombre = ars.data.nombre;
      }
    }
    receivables.value = response.data;
  } catch (error) {
    console.error('Error al cargar cuentas por cobrar:', error);
  } finally {
    loading.value = false;
  }
};

const onRowSelect = (event) => {
  selectedReceivable.value = event.data;
};

const abrirModalPago = (receivable) => {
  selectedReceivable.value = receivable;
  pago.value = {
    monto: receivable.saldoPendiente,
    metodoPago: null,
    referenciaComprobante: '',
    observaciones: ''
  };
  showPagoDialog.value = true;
};

const registrarPago = async () => {
  saving.value = true;
  try {

    console.log('Datos a enviar:', {
      receivableId: selectedReceivable.value.id,
      monto: pago.value.monto,
      metodoPago: pago.value.metodoPago,
      referenciaComprobante: pago.value.referenciaComprobante,
      observaciones: pago.value.observaciones
    });

    await api.post('/accounting/payments', {
      receivableId: selectedReceivable.value.id,
      monto: pago.value.monto,
      metodoPago: pago.value.metodoPago,
      referenciaComprobante: pago.value.referenciaComprobante,
      observaciones: pago.value.observaciones
    });

    showPagoDialog.value = false;
    await loadReceivables();
  } catch (error) {
    console.error('Error al registrar pago:', error);
    alert(error.response?.data?.message || 'Error al registrar pago');
  } finally {
    saving.value = false;
  }
};

onMounted(() => {
  loadReceivables();
});
</script>

<style scoped>
.content {
  padding: 30px;
  background: #f4f7f6;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  color: #8B4513;
  margin: 0;
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.pago-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-group label {
  font-weight: 600;
  font-size: 0.9rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}
</style>
<template>
  <div class="content">
    <div class="page-header">
      <h1>Cuentas por Pagar</h1>
      <Button label="Nueva Cuenta" icon="pi pi-plus" @click="abrirModalNuevo" class="btn-primary" />
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <DataTable :value="payables" stripedRows :paginator="true" :rows="10" v-model:selection="selectedPayable" selectionMode="single" @rowSelect="onRowSelect">
        <Column field="id" header="ID" sortable />
        <Column field="proveedorNombre" header="Proveedor" sortable />
        <Column field="documentoTipo" header="Tipo" sortable />
        <Column field="monto" header="Monto" sortable>
          <template #body="{ data }"> RD$ {{ formatNumber(data.monto) }} </template>
        </Column>
        <Column field="saldoPendiente" header="Saldo Pendiente" sortable>
          <template #body="{ data }"> RD$ {{ formatNumber(data.saldoPendiente) }} </template>
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
            <Button icon="pi pi-dollar" rounded text @click="abrirModalPago(data)" :disabled="data.estado === 'PAGADO'" tooltip="Registrar Pago" />
          </template>
        </Column>
      </DataTable>
    </div>

    <!-- Modal Nueva Cuenta por Pagar -->
    <Dialog v-model:visible="showNuevoDialog" header="Nueva Cuenta por Pagar" :style="{ width: '450px' }" :modal="true">
      <form @submit.prevent="crearPayable" class="pago-form">
        <div class="form-group">
          <label>Proveedor *</label>
          <InputText v-model="nuevoPayable.proveedorNombre" required />
        </div>
        <div class="form-group">
          <label>Documento</label>
          <InputText v-model="nuevoPayable.documento" placeholder="Factura #" />
        </div>
        <div class="form-group">
          <label>Monto *</label>
          <InputNumber v-model="nuevoPayable.monto" :minFractionDigits="2" required />
        </div>
        <div class="form-group">
          <label>Fecha Vencimiento *</label>
          <Calendar v-model="nuevoPayable.fechaVencimiento" dateFormat="yy-mm-dd" required />
        </div>
        <div class="form-group">
          <label>Descripción</label>
          <Textarea v-model="nuevoPayable.descripcion" rows="2" />
        </div>
        <div class="form-actions">
          <Button label="Cancelar" severity="secondary" @click="showNuevoDialog = false" />
          <Button type="submit" label="Guardar" :loading="saving" />
        </div>
      </form>
    </Dialog>

    <!-- Modal Registrar Pago -->
    <Dialog v-model:visible="showPagoDialog" header="Registrar Pago Proveedor" :style="{ width: '400px' }" :modal="true">
      <form @submit.prevent="registrarPago" class="pago-form">
        <div class="form-group">
          <label>Proveedor</label>
          <InputText :value="selectedPayable?.proveedorNombre" disabled />
        </div>
        <div class="form-group">
          <label>Saldo Pendiente</label>
          <InputText :value="'RD$ ' + formatNumber(selectedPayable?.saldoPendiente)" disabled />
        </div>
        <div class="form-group">
          <label>Monto a Pagar *</label>
          <InputNumber v-model="pago.monto" :max="selectedPayable?.saldoPendiente" :minFractionDigits="2" required />
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
          <label>Comprobante</label>
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
import Calendar from 'primevue/calendar';
import Textarea from 'primevue/textarea';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const payables = ref([]);
const selectedPayable = ref(null);
const loading = ref(false);
const saving = ref(false);
const showNuevoDialog = ref(false);
const showPagoDialog = ref(false);

const nuevoPayable = ref({
  proveedorNombre: '',
  documento: '',
  monto: null,
  fechaVencimiento: null,
  descripcion: ''
});

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
  const map = { 'PENDIENTE': 'danger', 'PARCIAL': 'warning', 'PAGADO': 'success' };
  return map[estado] || 'info';
};

const loadPayables = async () => {
  loading.value = true;
  try {
    const response = await api.get('/accounting/payables/pendientes');
    payables.value = response.data;
  } catch (error) {
    console.error('Error al cargar cuentas por pagar:', error);
  } finally {
    loading.value = false;
  }
};

const onRowSelect = (event) => {
  selectedPayable.value = event.data;
};

const abrirModalNuevo = () => {
  nuevoPayable.value = { proveedorNombre: '', documento: '', monto: null, fechaVencimiento: null, descripcion: '' };
  showNuevoDialog.value = true;
};

const crearPayable = async () => {
  saving.value = true;
  try {
    await api.post('/accounting/payables', {
      proveedorId: 0,  // ← ID temporal
      proveedorNombre: nuevoPayable.value.proveedorNombre,
      documento: nuevoPayable.value.documento,
      monto: nuevoPayable.value.monto,
      fechaVencimiento: nuevoPayable.value.fechaVencimiento,
      descripcion: nuevoPayable.value.descripcion
    });
    showNuevoDialog.value = false;
    await loadPayables();
  } catch (error) {
    console.error('Error al crear:', error);
    alert(error.response?.data?.message || 'Error al crear');
  } finally {
    saving.value = false;
  }
};

const abrirModalPago = (payable) => {
  selectedPayable.value = payable;
  pago.value = { monto: payable.saldoPendiente, metodoPago: null, referenciaComprobante: '', observaciones: '' };
  showPagoDialog.value = true;
};

const registrarPago = async () => {
  saving.value = true;
  try {
    await api.post('/accounting/payables/pagos', {
      payableId: selectedPayable.value.id,
      monto: pago.value.monto,
      metodoPago: pago.value.metodoPago,
      referenciaComprobante: pago.value.referenciaComprobante,
      observaciones: pago.value.observaciones
    });
    showPagoDialog.value = false;
    await loadPayables();
  } catch (error) {
    console.error('Error al registrar pago:', error);
    alert(error.response?.data?.message || 'Error al registrar pago');
  } finally {
    saving.value = false;
  }
};

onMounted(() => {
  loadPayables();
});
</script>

<style scoped>
.content { padding: 30px; background: #f4f7f6; min-height: 100vh; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.page-header h1 { color: #8B4513; margin: 0; }
.btn-primary { background: linear-gradient(135deg, #8B4513, #A0522D); border: none; }
.table-container { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.loading-container { display: flex; justify-content: center; align-items: center; min-height: 400px; }
.pago-form { display: flex; flex-direction: column; gap: 15px; }
.form-group { display: flex; flex-direction: column; gap: 5px; }
.form-group label { font-weight: 600; font-size: 0.9rem; }
.form-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 10px; }
</style>
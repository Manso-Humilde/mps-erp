<template>
  <div class="content">
    <div class="page-header">
      <h1>Historial de Cobros</h1>
    </div>

    <div class="filters">
      <div class="filter-group">
        <label>Fecha Inicio</label>
        <Calendar v-model="fechaInicio" dateFormat="yy-mm-dd" />
      </div>
      <div class="filter-group">
        <label>Fecha Fin</label>
        <Calendar v-model="fechaFin" dateFormat="yy-mm-dd" />
      </div>
      <div class="filter-group">
        <label>Tipo</label>
        <Dropdown v-model="tipoFiltro" :options="tiposFiltro" optionLabel="label" optionValue="value" placeholder="Todos" clearable />
      </div>
      <Button label="Buscar" icon="pi pi-search" @click="loadData" />
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <DataTable :value="payments" stripedRows :paginator="true" :rows="15">
        <Column field="fecha" header="Fecha" sortable>
          <template #body="{ data }"> {{ formatDate(data.fecha) }} </template>
        </Column>
        <Column field="cliente" header="Cliente" sortable />
        <Column field="monto" header="Monto" sortable>
          <template #body="{ data }"> RD$ {{ formatNumber(data.monto) }} </template>
        </Column>
        <Column field="metodoPago" header="Método" sortable />
        <Column field="referenciaComprobante" header="Comprobante" />
        <Column field="observaciones" header="Observaciones" />
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Calendar from 'primevue/calendar';
import Dropdown from 'primevue/dropdown';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const payments = ref([]);
const loading = ref(false);
const fechaInicio = ref(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
const fechaFin = ref(new Date());
const tipoFiltro = ref(null);

const tiposFiltro = ref([
  { label: 'Todos', value: null },
  { label: 'Efectivo', value: 'EFECTIVO' },
  { label: 'Transferencia', value: 'TRANSFERENCIA' },
  { label: 'Cheque', value: 'CHEQUE' }
]);

const formatDate = (date) => new Date(date).toLocaleDateString('es-DO');
const formatNumber = (value) => value?.toFixed(2) || '0.00';

const loadData = async () => {
  loading.value = true;
  try {
    const inicio = fechaInicio.value.toISOString().split('T')[0];
    const fin = fechaFin.value.toISOString().split('T')[0];
    let url = `/api/accounting/payments/cobros?inicio=${inicio}&fin=${fin}`;
    if (tipoFiltro.value) {
      url += `&metodoPago=${tipoFiltro.value}`;
    }
    const response = await api.get(url);
    payments.value = response.data;
  } catch (error) {
    console.error('Error al cargar historial:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.content { padding: 30px; background: #f4f7f6; min-height: 100vh; }
.page-header { margin-bottom: 30px; }
.page-header h1 { color: #8B4513; margin: 0; }
.filters { display: flex; gap: 15px; align-items: flex-end; background: white; padding: 20px; border-radius: 12px; margin-bottom: 20px; flex-wrap: wrap; }
.filter-group { display: flex; flex-direction: column; gap: 5px; }
.filter-group label { font-size: 0.85rem; font-weight: 600; }
.table-container { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.loading-container { display: flex; justify-content: center; align-items: center; min-height: 400px; }
@media (max-width: 768px) { .filters { flex-direction: column; align-items: stretch; } }
</style>
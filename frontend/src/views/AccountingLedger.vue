<template>
  <div class="content">
    <div class="page-header">
      <h1>Libro Diario</h1>
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
      <Button label="Buscar" icon="pi pi-search" @click="loadLedger" />
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <DataTable :value="entries" stripedRows :paginator="true" :rows="20">
        <Column field="fecha" header="Fecha" sortable>
          <template #body="{ data }">
            {{ formatDate(data.fecha) }}
          </template>
        </Column>
        <Column field="tipo" header="Tipo" sortable />
        <Column field="categoria" header="Categoría" sortable />
        <Column field="descripcion" header="Descripción" />
        <Column field="monto" header="Monto" sortable>
          <template #body="{ data }">
            RD$ {{ formatNumber(data.monto) }}
          </template>
        </Column>
        <Column field="referenciaTipo" header="Referencia" />
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
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const entries = ref([]);
const loading = ref(false);
const fechaInicio = ref(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
const fechaFin = ref(new Date());

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('es-DO');
};

const formatNumber = (value) => {
  return value?.toFixed(2) || '0.00';
};

const loadLedger = async () => {
  loading.value = true;
  try {
    const inicio = fechaInicio.value.toISOString().split('T')[0];
    const fin = fechaFin.value.toISOString().split('T')[0];
    const response = await api.get(`/api/accounting/ledger?inicio=${inicio}&fin=${fin}`);
    entries.value = response.data;
  } catch (error) {
    console.error('Error al cargar libro diario:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadLedger();
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

.filters {
  display: flex;
  gap: 15px;
  align-items: flex-end;
  margin-bottom: 20px;
  background: white;
  padding: 20px;
  border-radius: 12px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-group label {
  font-weight: 600;
  color: #333;
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
</style>
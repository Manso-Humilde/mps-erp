<template>
  <div class="content">
    <div class="page-header">
      <h1>Antigüedad de Saldos</h1>
      <Button label="Exportar" icon="pi pi-download" @click="exportar" class="btn-primary" />
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <DataTable :value="items" stripedRows :paginator="true" :rows="20">
        <Column field="tipo" header="Tipo" sortable />
        <Column field="terceroNombre" header="Cliente" sortable />
        <Column field="monto" header="Monto Total" sortable>
          <template #body="{ data }"> RD$ {{ formatNumber(data.monto) }} </template>
        </Column>
        <Column field="saldoPendiente" header="Saldo Pendiente" sortable>
          <template #body="{ data }"> RD$ {{ formatNumber(data.saldoPendiente) }} </template>
        </Column>
        <Column field="rangoDias" header="Antigüedad" sortable>
          <template #body="{ data }">
            <Tag :value="data.rangoDias" :severity="getSeveridad(data.rangoDias)" />
          </template>
        </Column>
        <Column field="diasVencidos" header="Días Vencidos" sortable />
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const items = ref([]);
const loading = ref(false);

const formatNumber = (value) => {
  return value?.toFixed(2) || '0.00';
};

const getSeveridad = (rango) => {
  if (rango === '0-30 días') return 'success';
  if (rango === '31-60 días') return 'warning';
  if (rango === '61-90 días') return 'danger';
  return 'danger';
};

const loadData = async () => {
  loading.value = true;
  try {
    const response = await api.get('/api/accounting/receivables/antiguedad');
    items.value = response.data;
  } catch (error) {
    console.error('Error al cargar datos:', error);
  } finally {
    loading.value = false;
  }
};

const exportar = () => {
  // TODO: Exportar a Excel/PDF
  alert('Exportación en desarrollo');
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.content {
  padding: 30px;
  background: #f4f7f6;
  min-height: 100vh;
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
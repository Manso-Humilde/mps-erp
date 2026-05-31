<template>
  <div class="content">
    <div class="page-header">
      <h1>Balance General</h1>
      <Button label="Actualizar" icon="pi pi-refresh" @click="loadData" class="btn-primary" />
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="cards-container">
      <div class="card activos">
        <div class="card-title">ACTIVOS</div>
        <div class="card-monto">RD$ {{ formatNumber(activos) }}</div>
      </div>
      <div class="card pasivos">
        <div class="card-title">PASIVOS</div>
        <div class="card-monto">RD$ {{ formatNumber(pasivos) }}</div>
      </div>
      <div class="card patrimonio">
        <div class="card-title">PATRIMONIO</div>
        <div class="card-monto">RD$ {{ formatNumber(patrimonio) }}</div>
      </div>
      <div class="card resultado">
        <div class="card-title">RESULTADO</div>
        <div class="card-monto">RD$ {{ formatNumber(activos - pasivos) }}</div>
      </div>
    </div>

    <div class="table-container">
      <h3>Detalle de Cuentas</h3>
      <DataTable :value="items" stripedRows>
        <Column field="codigo" header="Código" />
        <Column field="cuenta" header="Cuenta" />
        <Column field="saldo" header="Saldo">
          <template #body="{ data }"> RD$ {{ formatNumber(data.saldo) }} </template>
        </Column>
        <Column field="tipo" header="Tipo" />
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const items = ref([]);
const loading = ref(false);
const activos = ref(0);
const pasivos = ref(0);
const patrimonio = ref(0);

const formatNumber = (value) => value?.toFixed(2) || '0.00';

const loadData = async () => {
  loading.value = true;
  try {
    const response = await api.get('/api/accounting/balance-general');
    items.value = response.data;

    for (const item of response.data) {
      if (item.tipo === 'ACTIVO') activos.value = item.saldo;
      if (item.tipo === 'PASIVO') pasivos.value = item.saldo;
      if (item.tipo === 'PATRIMONIO') patrimonio.value = item.saldo;
    }
  } catch (error) {
    console.error('Error al cargar balance:', error);
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
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.page-header h1 { color: #8B4513; margin: 0; }
.btn-primary { background: linear-gradient(135deg, #8B4513, #A0522D); border: none; }
.cards-container { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 30px; }
.card { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center; }
.card-title { font-size: 1rem; color: #666; margin-bottom: 10px; }
.card-monto { font-size: 1.5rem; font-weight: bold; }
.activos .card-monto { color: #22c55e; }
.pasivos .card-monto { color: #ef4444; }
.patrimonio .card-monto { color: #3b82f6; }
.resultado .card-monto { color: #8B4513; }
.table-container { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.table-container h3 { margin: 0 0 15px 0; color: #333; }
.loading-container { display: flex; justify-content: center; align-items: center; min-height: 400px; }
</style>
<template>
  <div class="content">
    <div class="page-header">
      <h1>Estado de Resultados</h1>
      <div class="filters">
        <div class="filter-group">
          <label>Fecha Inicio</label>
          <Calendar v-model="fechaInicio" dateFormat="yy-mm-dd" />
        </div>
        <div class="filter-group">
          <label>Fecha Fin</label>
          <Calendar v-model="fechaFin" dateFormat="yy-mm-dd" />
        </div>
        <Button label="Consultar" icon="pi pi-search" @click="loadData" class="btn-primary" />
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="cards-container">
      <div class="card ingresos">
        <div class="card-title">INGRESOS TOTALES</div>
        <div class="card-monto">RD$ {{ formatNumber(ingresos) }}</div>
      </div>
      <div class="card gastos">
        <div class="card-title">GASTOS TOTALES</div>
        <div class="card-monto">RD$ {{ formatNumber(gastos) }}</div>
      </div>
      <div class="card resultado">
        <div class="card-title">UTILIDAD / PÉRDIDA</div>
        <div class="card-monto" :class="{ 'utilidad': utilidad >= 0, 'perdida': utilidad < 0 }">
          RD$ {{ formatNumber(utilidad) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Button from 'primevue/button';
import Calendar from 'primevue/calendar';
import ProgressSpinner from 'primevue/progressspinner';
import api from '../services/api';

const loading = ref(false);
const ingresos = ref(0);
const gastos = ref(0);
const utilidad = ref(0);
const fechaInicio = ref(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
const fechaFin = ref(new Date());

const formatNumber = (value) => value?.toFixed(2) || '0.00';

const loadData = async () => {
  loading.value = true;
  try {
    const inicio = fechaInicio.value.toISOString().split('T')[0];
    const fin = fechaFin.value.toISOString().split('T')[0];
    const response = await api.get(`/api/accounting/estado-resultados?inicio=${inicio}&fin=${fin}`);

    for (const item of response.data) {
      if (item.tipo === 'INGRESO') ingresos.value = item.monto;
      if (item.tipo === 'GASTO') gastos.value = item.monto;
      if (item.tipo === 'RESULTADO') utilidad.value = item.monto;
    }
  } catch (error) {
    console.error('Error al cargar estado de resultados:', error);
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
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; flex-wrap: wrap; gap: 15px; }
.page-header h1 { color: #8B4513; margin: 0; }
.filters { display: flex; gap: 15px; align-items: flex-end; }
.filter-group { display: flex; flex-direction: column; gap: 5px; }
.filter-group label { font-size: 0.85rem; font-weight: 600; }
.btn-primary { background: linear-gradient(135deg, #8B4513, #A0522D); border: none; }
.cards-container { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 30px; }
.card { background: white; border-radius: 12px; padding: 25px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center; }
.card-title { font-size: 1rem; color: #666; margin-bottom: 10px; }
.card-monto { font-size: 1.8rem; font-weight: bold; }
.ingresos .card-monto { color: #22c55e; }
.gastos .card-monto { color: #ef4444; }
.utilidad { color: #22c55e; }
.perdida { color: #ef4444; }
.loading-container { display: flex; justify-content: center; align-items: center; min-height: 400px; }
@media (max-width: 768px) {
  .cards-container { grid-template-columns: 1fr; }
  .filters { flex-direction: column; align-items: stretch; }
}
</style>
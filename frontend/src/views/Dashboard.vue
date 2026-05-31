<template>
  <div class="dashboard-content">
    <h1>Dashboard</h1>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="dashboard-cards">
      <div class="metric-card">
        <div class="metric-icon ingresos">
          <i class="pi pi-wallet"></i>
        </div>
        <div class="metric-info">
          <p class="metric-label">Ingreso Mensual</p>
          <p class="metric-value">RD$ {{ formatNumber(dashboard.ingresoMensual) }}</p>
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-icon retencion">
          <i class="pi pi-percentage"></i>
        </div>
        <div class="metric-info">
          <p class="metric-label">Retención ISR</p>
          <p class="metric-value">RD$ {{ formatNumber(dashboard.retencionTotal) }}</p>
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-icon cobrado">
          <i class="pi pi-check-circle"></i>
        </div>
        <div class="metric-info">
          <p class="metric-label">Monto Cobrado</p>
          <p class="metric-value">RD$ {{ formatNumber(dashboard.montoCobrado) }}</p>
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-icon pendiente">
          <i class="pi pi-clock"></i>
        </div>
        <div class="metric-info">
          <p class="metric-label">Monto Pendiente</p>
          <p class="metric-value">RD$ {{ formatNumber(dashboard.montoPendiente) }}</p>
        </div>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <i class="pi pi-users"></i>
        <span>{{ dashboard.totalConsultas }} Consultas</span>
      </div>
      <div class="stat-card">
        <i class="pi pi-file-invoice"></i>
        <span>{{ dashboard.totalFacturasPendientes }} Facturas Pendientes</span>
      </div>
    </div>

    <div class="recent-sections">
      <div class="recent-card">
        <h2>Últimas Consultas</h2>
        <DataTable :value="dashboard.ultimasConsultas" stripedRows>
          <Column field="fechaConsulta" header="Fecha">
            <template #body="slotProps">
              {{ formatDate(slotProps.data.fechaConsulta) }}
            </template>
          </Column>
          <Column field="nombrePaciente" header="Paciente" />
          <Column field="tipoServicio" header="Tipo">
            <template #body="slotProps">
              {{ slotProps.data.tipoServicio ? slotProps.data.tipoServicio.replace('_', ' ') : '' }}
            </template>
          </Column>
          <Column field="montoTotal" header="Monto">
            <template #body="slotProps">
              RD$ {{ slotProps.data.montoTotal ? slotProps.data.montoTotal.toFixed(2) : '0.00' }}
            </template>
          </Column>
        </DataTable>
      </div>

      <div class="recent-card">
        <h2>Últimas Facturas</h2>
        <DataTable :value="dashboard.ultimasFacturas" stripedRows>
          <Column field="fechaEmision" header="Fecha">
            <template #body="slotProps">
              {{ formatDate(slotProps.data.fechaEmision) }}
            </template>
          </Column>
          <Column field="ncf" header="NCF" />
          <Column field="estadoDgii" header="Estado DGII">
            <template #body="slotProps">
              <Tag
                :value="slotProps.data.estadoDgii"
                :severity="getStatusSeverity(slotProps.data.estadoDgii)"
              />
            </template>
          </Column>
          <Column field="montoTotal" header="Monto">
            <template #body="slotProps">
              RD$ {{ slotProps.data.montoTotal.toFixed(2) }}
            </template>
          </Column>
        </DataTable>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import { facturasService } from '../services/facturas';

const loading = ref(true);
const dashboard = ref({
  ingresoMensual: 0,
  retencionTotal: 0,
  montoCobrado: 0,
  montoPendiente: 0,
  totalConsultas: 0,
  totalFacturasPendientes: 0,
  ultimasConsultas: [],
  ultimasFacturas: []
});

const loadDashboard = async () => {
  loading.value = true;
  try {
    dashboard.value = await facturasService.getDashboard();
  } catch (error) {
    console.error('Error al cargar dashboard:', error);
  } finally {
    loading.value = false;
  }
};

const formatNumber = (num) => {
  return (num || 0).toLocaleString('es-DO', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
};

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('es-DO', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const getStatusSeverity = (status) => {
  const severityMap = {
    'PENDIENTE': 'warn',
    'APROBADA': 'success',
    'RECHAZADA': 'danger'
  };
  return severityMap[status] || 'info';
};

onMounted(() => {
  loadDashboard();
});
</script>

<style scoped>
.dashboard-content {
  padding: 30px;
  flex: 1;
  background-color: #f4f7f6;
}

.dashboard-content h1 {
  color: #8B4513;
  margin-bottom: 30px;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.dashboard-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.metric-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: transform 0.3s ease;
}

.metric-card:hover {
  transform: translateY(-5px);
}

.metric-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
}

.metric-icon.ingresos {
  background: linear-gradient(135deg, #8B4513, #A0522D);
}

.metric-icon.retencion {
  background: linear-gradient(135deg, #A0522D, #D2691E);
}

.metric-icon.cobrado {
  background: linear-gradient(135deg, #28a745, #20c997);
}

.metric-icon.pendiente {
  background: linear-gradient(135deg, #ffc107, #fd7e14);
}

.metric-info {
  flex: 1;
}

.metric-label {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 5px;
}

.metric-value {
  color: #333;
  font-size: 1.8rem;
  font-weight: 700;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-card i {
  font-size: 2rem;
  color: #8B4513;
}

.stat-card span {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
}

.recent-sections {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.recent-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.recent-card h2 {
  color: #8B4513;
  margin-bottom: 20px;
  font-size: 1.2rem;
}
</style>
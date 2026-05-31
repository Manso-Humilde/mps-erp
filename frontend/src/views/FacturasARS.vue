<template>
  <div class="content">
    <div class="page-header">
      <h1>Facturas a ARS</h1>
      <div class="filters">
        <!-- Selector de mes/año -->
        <div class="report-filters">
          <Calendar
            v-model="reportDate"
            view="month"
            dateFormat="mm/yy"
            placeholder="Seleccionar mes"
            showIcon
          />
          <Button
            label="Generar Reporte"
            icon="pi pi-file-pdf"
            @click="generarReporteMensual"
            :loading="generating"
            class="btn-generate"
          />
        </div>

        <Dropdown
          v-model="filtroEstado"
          :options="estadosDGII"
          placeholder="Filtrar por Estado"
          showClear
        />
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <ProgressSpinner />
    </div>

    <div v-else class="table-container">
      <div class="table-scroll-wrapper">
        <DataTable
          :value="facturasFiltradas"
          stripedRows
          :paginator="true"
          :rows="10"
          :rowsPerPageOptions="[10, 20, 50]"
          :scrollable="true"
          scrollHeight="flex"
          class="p-datatable-sm professional-table"
          tableStyle="min-width: 1200px"
        >
          <Column field="ncf" header="NCF" sortable style="min-width: 150px" />
          <Column field="arsNombre" header="ARS" sortable style="min-width: 150px" />
          <Column field="periodo" header="Periodo" sortable style="min-width: 100px" />
          <Column field="cantidadConsultas" header="Consultas" sortable style="min-width: 100px" />
          <Column field="montoBruto" header="Monto Bruto" sortable style="min-width: 120px">
            <template #body="slotProps">
              RD$ {{ slotProps.data.montoBruto.toFixed(2) }}
            </template>
          </Column>
          <Column field="retencionIsr" header="Retención ISR" sortable style="min-width: 120px">
            <template #body="slotProps">
              RD$ {{ (slotProps.data.retencionIsr || 0).toFixed(2) }}
            </template>
          </Column>
          <Column field="montoNeto" header="Monto Neto" sortable style="min-width: 120px">
            <template #body="slotProps">
              RD$ {{ slotProps.data.montoNeto.toFixed(2) }}
            </template>
          </Column>
          <Column field="fechaEmision" header="Fecha Emisión" sortable style="min-width: 120px">
            <template #body="slotProps">
              {{ formatDate(slotProps.data.fechaEmision) }}
            </template>
          </Column>
          <Column field="estadoDgii" header="Estado DGII" sortable style="min-width: 120px">
            <template #body="slotProps">
              <Tag
                :value="slotProps.data.estadoDgii"
                :severity="getStatusSeverity(slotProps.data.estadoDgii)"
              />
            </template>
          </Column>
          <Column header="Acciones" style="min-width: 100px">
            <template #body="slotProps">
              <Button
                icon="pi pi-eye"
                rounded
                text
                @click="verFactura(slotProps.data)"
                tooltip="Ver detalles"
              />
              <Button
                icon="pi pi-download"
                rounded
                text
                @click="descargarPDF(slotProps.data.id)"
                tooltip="Descargar PDF"
              />
            </template>
          </Column>
        </DataTable>
      </div>
    </div>

    <!-- Diálogo para ver detalles -->
    <Dialog
      v-model:visible="showDetailDialog"
      header="Detalles de Factura ARS"
      :style="{ width: '600px' }"
      :modal="true"
    >
      <div class="view-container" v-if="selectedFactura">
        <div class="view-row">
          <div class="view-label">NCF:</div>
          <div class="view-value">{{ selectedFactura.ncf }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">ARS:</div>
          <div class="view-value">{{ selectedFactura.arsNombre }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Periodo:</div>
          <div class="view-value">{{ selectedFactura.periodo }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Cantidad Consultas:</div>
          <div class="view-value">{{ selectedFactura.cantidadConsultas }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Monto Bruto:</div>
          <div class="view-value">RD$ {{ selectedFactura.montoBruto.toFixed(2) }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Retención ISR:</div>
          <div class="view-value">RD$ {{ (selectedFactura.retencionIsr || 0).toFixed(2) }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Monto Neto:</div>
          <div class="view-value">RD$ {{ selectedFactura.montoNeto.toFixed(2) }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Fecha Emisión:</div>
          <div class="view-value">{{ formatDate(selectedFactura.fechaEmision) }}</div>
        </div>
        <div class="view-row">
          <div class="view-label">Estado DGII:</div>
          <div class="view-value">
            <Tag :value="selectedFactura.estadoDgii" :severity="getStatusSeverity(selectedFactura.estadoDgii)" />
          </div>
        </div>
        <div class="view-row" v-if="selectedFactura.mensajeDgii">
          <div class="view-label">Mensaje DGII:</div>
          <div class="view-value">{{ selectedFactura.mensajeDgii }}</div>
        </div>
      </div>
      <template #footer>
        <Button label="Cerrar" severity="secondary" @click="showDetailDialog = false" />
        <Button label="Descargar PDF" icon="pi pi-download" @click="descargarPDF(selectedFactura.id)" />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dropdown from 'primevue/dropdown';
import Calendar from 'primevue/calendar';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import Dialog from 'primevue/dialog';
import { facturasService } from '../services/facturas';

const facturas = ref([]);
const loading = ref(false);
const generating = ref(false);
const filtroEstado = ref(null);
const reportDate = ref(new Date());
const showDetailDialog = ref(false);
const selectedFactura = ref(null);

const estadosDGII = ref([
  { label: 'Pendiente', value: 'PENDIENTE' },
  { label: 'Aprobada', value: 'APROBADA' },
  { label: 'Rechazada', value: 'RECHAZADA' },
  { label: 'Procesando', value: 'PROCESANDO' }
]);

const facturasFiltradas = computed(() => {
  if (!filtroEstado.value) return facturas.value;
  return facturas.value.filter(f => f.estadoDgii === filtroEstado.value.value);
});

const loadFacturas = async () => {
  loading.value = true;
  try {
    facturas.value = await facturasService.getARS();
  } catch (error) {
    console.error('Error al cargar facturas:', error);
  } finally {
    loading.value = false;
  }
};

const verFactura = (factura) => {
  selectedFactura.value = factura;
  showDetailDialog.value = true;
};

const descargarPDF = (id) => {
  window.open(`http://localhost:8080/api/invoices/ars/${id}/pdf`, '_blank');
};

const formatDate = (date) => {
  if (!date) return 'N/A';
  return new Date(date).toLocaleDateString('es-DO', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const generarReporteMensual = async () => {
  generating.value = true;
  try {
    const year = reportDate.value.getFullYear();
    const month = reportDate.value.getMonth() + 1;
    const facturasGeneradas = await facturasService.generarReporteARS(year, month);
    await loadFacturas();

    const cantidad = facturasGeneradas ? facturasGeneradas.length : 0;
    alert(`Reporte generado correctamente. Se crearon ${cantidad} facturas.`);
  } catch (error) {
    console.error('Error al generar reporte:', error);
    alert('Error al generar reporte');
  } finally {
    generating.value = false;
  }
};

const getStatusSeverity = (status) => {
  const severityMap = {
    'PENDIENTE': 'warn',
    'APROBADA': 'success',
    'RECHAZADA': 'danger',
    'PROCESANDO': 'info'
  };
  return severityMap[status] || 'info';
};

onMounted(() => {
  loadFacturas();
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

.filters {
  display: flex;
  gap: 15px;
  align-items: center;
  flex-wrap: wrap;
}

.report-filters {
  display: flex;
  gap: 10px;
  align-items: center;
  background: white;
  padding: 8px 15px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.btn-generate {
  background: linear-gradient(135deg, #8B4513, #A0522D);
  border: none;
}

.btn-generate:hover {
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

  .filters {
    width: 100%;
    flex-direction: column;
  }

  .report-filters {
    width: 100%;
  }

  .table-container {
    padding: 12px;
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
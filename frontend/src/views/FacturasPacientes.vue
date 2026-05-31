<template>
  <div class="content">
    <div class="page-header">
      <h1>Facturas a Pacientes</h1>
      <div class="filters">
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
          <DataTable
            :value="facturasFiltradas"
            stripedRows
            :paginator="true"
            :rows="10"
            class="p-datatable-sm"
          >
            <Column field="ncf" header="NCF" sortable />
            <Column field="fechaEmision" header="Fecha Emisión" sortable>
              <template #body="slotProps">
                {{ formatDate(slotProps.data.fechaEmision) }}
              </template>
            </Column>
            <Column field="nombrePaciente" header="Paciente" sortable />
            <Column field="montoTotal" header="Monto Total" sortable>
              <template #body="slotProps">
                RD$ {{ slotProps.data.montoTotal.toFixed(2) }}
              </template>
            </Column>
            <Column field="tipoPagador" header="Pagador" sortable />
            <Column field="estadoDgii" header="Estado DGII" sortable>
              <template #body="slotProps">
                <Tag
                  :value="slotProps.data.estadoDgii"
                  :severity="getStatusSeverity(slotProps.data.estadoDgii)"
                />
              </template>
            </Column>
            <Column field="mensajeDgii" header="Mensaje DGII" />
            <Column header="Acciones">
              <template #body="slotProps">
                <Button
                  icon="pi pi-eye"
                  rounded
                  text
                  label="Ver XML"
                  @click="verXML(slotProps.data)"
                />
              </template>
            </Column>
          </DataTable>
        </div>

        <Dialog
          v-model:visible="showXMLDialog"
          header="XML e-CF"
          :style="{ width: '800px' }"
          :modal="true"
        >
        <pre class="xml-content">{{ xmlContent }}</pre>
      </Dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dropdown from 'primevue/dropdown';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import Dialog from 'primevue/dialog';
import { facturasService } from '../services/facturas';

const facturas = ref([]);
const loading = ref(false);
const filtroEstado = ref(null);
const showXMLDialog = ref(false);
const xmlContent = ref('');

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
    facturas.value = await facturasService.getPacientes();
  } catch (error) {
    console.error('Error al cargar facturas:', error);
  } finally {
    loading.value = false;
  }
};

const verXML = (factura) => {
  xmlContent.value = factura.xmlEf || 'No disponible';
  showXMLDialog.value = true;
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
}

.page-header h1 {
  color: #8B4513;
  margin: 0;
}

.filters {
  display: flex;
  gap: 10px;
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

.xml-content {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  overflow-x: auto;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: 'Courier New', monospace;
  font-size: 0.85rem;
  max-height: 500px;
  overflow-y: auto;
}
</style>
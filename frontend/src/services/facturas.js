import api from './api';

export const facturasService = {
  getPacientes() {
    return api.get('/api/invoices/patient').then(res => res.data);
  },

  createFacturaPaciente(consultationId) {
    return api.post(`/api/invoices/patient/${consultationId}`).then(res => res.data);
  },

  getARS() {
    return api.get('/api/invoices/ars').then(res => res.data);
  },

  generateARSInvoices(periodo) {
    return api.post('/api/invoices/ars/generate', { periodo }).then(res => res.data);
  },

  async generarReporteARS(year, month) {
    const response = await api.post(`/api/invoices/ars/generar-reporte?year=${year}&month=${month}`);
    return response.data;
  }, // ← Agrega esta coma

  getDashboard(mes) {
    const params = mes ? `?mes=${mes}` : '';
    return api.get(`/api/dashboard${params}`).then(res => res.data);
  }
};
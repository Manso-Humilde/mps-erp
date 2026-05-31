import api from './api';

export const consultasService = {
  async getAll() {
    return api.get('/consultations').then(res => res.data);
  },

  getById(id) {
    return api.get(`/api/consultations/${id}`).then(res => res.data);
  },

  create(data) {
    return api.post('/consultations', data).then(res => res.data);
  }
};
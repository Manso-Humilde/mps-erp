import api from './api';

export const pacientesService = {
  getAll() {
    return api.get('/patients').then(res => res.data);
  },

  getById(id) {
    return api.get(`/api/patients/${id}`).then(res => res.data);
  },

  create(data) {
    return api.post('/patients', data).then(res => res.data);
  },

  update(id, data) {
    return api.put(`/api/patients/${id}`, data).then(res => res.data);
  }
};
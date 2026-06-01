import api from './api';

export const pacientesService = {
  getAll() {
    return api.get('/patients').then(res => res.data);
  },

  getById(id) {
    return api.get(`/patients/${id}`).then(res => res.data);
  },

  create(data) {
    return api.post('/patients', data).then(res => res.data);
  },

  update(id, data) {
    return api.put(`/patients/${id}`, data).then(res => res.data);
  }
};
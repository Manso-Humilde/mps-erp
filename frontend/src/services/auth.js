import api from './api'

export default {
  /**
   * Iniciar sesión
   */
  async login(credentials) {
    try {
      console.log('Enviando login a:', '/api/auth/login');
      const response = await api.post('/api/auth/login', credentials);
      console.log('Respuesta del servidor:', response);
      console.log('response.data:', response.data);

      if (response.data) {
        console.log('Guardando usuario en localStorage');
        localStorage.setItem('user', JSON.stringify(response.data));
      }

      return response
    } catch (error) {
      console.error('Error en login:', error);
      throw error
    }
  },

  /**
   * Verificar token (ya no necesario, la cookie se envía automática)
   */
  verifyToken() {
    return api.get('/api/auth/verify')
  },

  /**
   * Cerrar sesión
   */
  async logout() {
      try {
        await api.post('/api/auth/logout')
        localStorage.removeItem('user')
        localStorage.removeItem('token')
      } catch (error) {
        throw error
      }
  }
}
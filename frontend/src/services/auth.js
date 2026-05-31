import api from './api'

export default {
  async login(credentials) {
    try {
      // Quita el /api de la ruta
      const response = await api.post('/auth/login', credentials)
      if (response.data) {
        localStorage.setItem('user', JSON.stringify(response.data))
      }
      return response
    } catch (error) {
      console.error('Error en login:', error)
      throw error
    }
  },

  verifyToken() {
    // Quita el /api de la ruta
    return api.get('/auth/verify')
  },

  async logout() {
    try {
      // Quita el /api de la ruta
      await api.post('/auth/logout')
      localStorage.removeItem('user')
      localStorage.removeItem('token')
    } catch (error) {
      throw error
    }
  }
}
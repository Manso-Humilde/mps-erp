import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  withCredentials: true,  // ← Enviar cookies automáticamente
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor - ya no necesitas enviar token manualmente
api.interceptors.request.use(
  (config) => {
    // Ya no enviamos token manual, la cookie httpOnly se envía automáticamente
    // Solo mantenemos para compatibilidad si aún hay tokens en localStorage
    //const token = localStorage.getItem('token')
    //if (token) {
     // config.headers.Authorization = `Bearer ${token}`
   // }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api
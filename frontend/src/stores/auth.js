import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../services/api'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)
  const loading = ref(false)

  const isAuthenticated = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === 'SUPER_ADMIN' || user.value?.role === 'ADMIN_CONTABLE')
  const isMedico = computed(() => user.value?.role === 'MEDICO')

  function setUser(userData) {
    user.value = userData
    localStorage.setItem('user', JSON.stringify(userData))
  }

  function setToken(tokenValue) {
    token.value = tokenValue
    localStorage.setItem('token', tokenValue)
  }

  function clearAuth() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  async function login(credentials) {
    loading.value = true;
    try {
      const response = await api.post('/auth/login', credentials);
      if (response.data) {
        setUser(response.data);
      }
      return response.data;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    } finally {
      loading.value = false;
    }
  }

  async function checkAuth() {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')

    if (storedToken && storedUser) {
      token.value = storedToken
      user.value = JSON.parse(storedUser)
    }
  }

  function logout() {
    clearAuth()
  }

  return {
    user,
    token,
    loading,
    isAuthenticated,
    isAdmin,
    isMedico,
    setUser,
    setToken,
    clearAuth,
    login,
    checkAuth,
    logout
  }
})
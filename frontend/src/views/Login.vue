<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-container">
          <div class="logo-circle">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="50" height="50">
              <circle cx="32" cy="32" r="30" fill="#8B4513"/>
              <text x="32" y="40" font-size="24" text-anchor="middle" fill="white" font-weight="bold">MPS</text>
            </svg>
          </div>
        </div>
        <h1 class="title">MPS ERP</h1>
        <p class="subtitle">Sistema de Gestión Médica</p>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="input-group">
          <i class="pi pi-envelope input-icon"></i>
          <input
            v-model="email"
            type="email"
            placeholder="Correo electrónico"
            required
            class="login-input"
          />
        </div>

        <div class="input-group">
          <i class="pi pi-lock input-icon"></i>
          <input
            v-model="password"
            type="password"
            placeholder="Contraseña"
            required
            class="login-input"
          />
        </div>

        <button type="submit" :disabled="loading" class="login-btn">
          <i v-if="loading" class="pi pi-spin pi-spinner"></i>
          <span v-else>Iniciar Sesión</span>
        </button>

        <div v-if="error" class="error-message">
          <i class="pi pi-exclamation-triangle"></i>
          {{ error }}
        </div>
      </form>

      <div class="login-footer">
        <p>© 2025 MPS ERP - Todos los derechos reservados</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const router = useRouter();
const authStore = useAuthStore();

const email = ref('test@test');
const password = ref('test');
const loading = ref(false);
const error = ref('');

const handleLogin = async () => {
  loading.value = true;
  error.value = '';

  try {
    // Enviar como objeto JSON, no como parámetros sueltos
    await authStore.login({
      email: email.value,
      password: password.value
    });
    // Verificar que el usuario se guardó
        console.log('Usuario después de login:', authStore.user);
    router.push('/dashboard');
  } catch (err) {
    console.error('Error detallado:', err);
    error.value = err.response?.data?.message || 'Error al iniciar sesión';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5e6d3 0%, #e8d5c4 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 24px;
  padding: 40px 32px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 40px rgba(139, 69, 19, 0.15);
  transition: transform 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-circle {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #8B4513, #D2691E);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(139, 69, 19, 0.3);
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #8B4513;
  margin: 0 0 8px 0;
}

.subtitle {
  font-size: 14px;
  color: #A0522D;
  margin: 0;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  color: #CD853F;
  font-size: 18px;
}

.login-input {
  width: 100%;
  padding: 14px 16px 14px 48px;
  border: 2px solid #e8d5c4;
  border-radius: 12px;
  font-size: 15px;
  transition: all 0.3s ease;
  outline: none;
  background: #fefaf5;
}

.login-input:focus {
  border-color: #D2691E;
  box-shadow: 0 0 0 3px rgba(210, 105, 30, 0.1);
}

.login-btn {
  background: linear-gradient(135deg, #8B4513, #D2691E);
  color: white;
  border: none;
  padding: 14px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #A0522D, #CD853F);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(139, 69, 19, 0.3);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.error-message {
  background: #fee2e2;
  color: #dc2626;
  padding: 12px;
  border-radius: 10px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.login-footer {
  text-align: center;
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid #e8d5c4;
}

.login-footer p {
  font-size: 12px;
  color: #A0522D;
  margin: 0;
}

/* Responsive */
@media (max-width: 480px) {
  .login-card {
    padding: 28px 20px;
  }

  .title {
    font-size: 24px;
  }

  .login-input {
    padding: 12px 12px 12px 44px;
  }
}
</style>
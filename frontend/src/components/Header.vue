<template>
  <header class="header">
    <div class="header-left">
      <button @click="toggleSidebar" class="menu-toggle">
        <i class="pi pi-bars"></i>
      </button>
      <h1 class="header-title">{{ currentPageTitle }}</h1>
    </div>

    <div class="header-right">
      <div class="header-actions">
        <Button
          icon="pi pi-bell"
          rounded
          text
          severity="secondary"
          class="notification-btn"
          v-badge.danger="5"
        />

        <Button
          icon="pi pi-cog"
          rounded
          text
          severity="secondary"
        />
      </div>

      <div class="user-dropdown">
        <div class="user-avatar">
          <span class="avatar-initials">{{ getInitials(authStore.user?.nombreCompleto) }}</span>
        </div>
        <div class="user-info">
          <p class="user-name">{{ authStore.user?.nombreCompleto }}</p>
          <p class="user-role">{{ formatRole(authStore.user?.role) }}</p>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import Button from 'primevue/button';
import BadgeDirective from 'primevue/badgedirective';

const route = useRoute();
const authStore = useAuthStore();

const pageTitleMap = {
  'Login': 'Inicio de Sesión',
  'Dashboard': 'Dashboard',
  'Pacientes': 'Gestión de Pacientes',
  'Consultas': 'Gestión de Consultas',
  'FacturasPacientes': 'Facturas a Pacientes',
  'FacturasARS': 'Facturas a ARS',
  'Configuracion': 'Configuración'
};

const currentPageTitle = computed(() => {
  return pageTitleMap[route.name] || 'MPS ERP';
});

const getInitials = (nombre) => {
  if (!nombre) return '?';
  const partes = nombre.trim().split(' ');
  if (partes.length >= 2) {
    return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
  }
  return nombre.substring(0, 2).toUpperCase();
};

const formatRole = (role) => {
  const roleMap = {
    'SUPER_ADMIN': 'Super Admin',
    'ADMIN_CONTABLE': 'Admin Contable',
    'MEDICO': 'Médico',
    'ASISTENTE': 'Asistente'
  };
  return roleMap[role] || role;
};

const toggleSidebar = () => {
  document.querySelector('.sidebar').classList.toggle('collapsed');
};
</script>

<style scoped>
.header {
  height: 70px;
  background: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  position: sticky;
  top: 0;
  z-index: 90;
}

.sidebar.collapsed ~ .main-content > .header {
  left: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.menu-toggle {
  background: none;
  border: none;
  font-size: 1.3rem;
  color: #8B4513;
  cursor: pointer;
  padding: 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.menu-toggle:hover {
  background: #f5f5f5;
}

.header-title {
  color: #333;
  font-size: 1.3rem;
  font-weight: 700;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 30px;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.notification-btn {
  color: #8B4513;
}

.notification-btn:hover {
  background: #f5f5f5;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 15px;
  cursor: pointer;
  padding: 8px 15px;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background: #f5f5f5;
}

.user-avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: linear-gradient(135deg, #8B4513, #D2691E);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 1.1rem;
}

.avatar-initials {
  text-transform: uppercase;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 0.95rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.user-role {
  font-size: 0.8rem;
  color: #666;
  margin: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .header {
    left: 0;
    padding: 0 15px;
  }

  .header-title {
    font-size: 1rem;
  }

  .user-info {
    display: none;
  }
}
</style>
<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <i class="pi pi-hospital logo-icon"></i>
      <span class="logo-text">MPS ERP</span>
    </div>

    <nav class="sidebar-nav">
      <router-link
        v-for="item in menuItems"
        :key="item.name"
        :to="item.path"
        class="nav-item"
        active-class="active"
      >
        <i :class="item.icon"></i>
        <span>{{ item.label }}</span>
      </router-link>
    </nav>

    <div class="sidebar-footer">
      <div class="user-info">
        <i class="pi pi-user"></i>
        <div class="user-details">
          <p class="user-name">{{ authStore.user?.nombreCompleto }}</p>
          <p class="user-role">{{ formatRole(authStore.user?.role) }}</p>
        </div>
      </div>

      <button @click="logout" class="logout-btn">
        <i class="pi pi-sign-out"></i>
        <span>Cerrar Sesión</span>
      </button>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const router = useRouter();
const authStore = useAuthStore();

onMounted(() => {
  authStore.checkAuth();
});

const menuItems = computed(() => {
  // Leer directamente desde localStorage para asegurar
  const userStr = localStorage.getItem('user');
  const user = userStr ? JSON.parse(userStr) : null;
  const role = user?.role || authStore.user?.role;

  console.log('Role detectado:', role); // ← Para depurar

  const items = [];

  items.push({ name: 'Dashboard', label: 'Dashboard', path: '/', icon: 'pi pi-home' });
  items.push({ name: 'Pacientes', label: 'Pacientes', path: '/pacientes', icon: 'pi pi-users' });
  items.push({ name: 'Citas', label: 'Citas', path: '/citas', icon: 'pi pi-clock' });

  // Consultas - solo MEDICO, ADMIN_CONTABLE, SUPER_ADMIN
  if (role === 'SUPER_ADMIN' || role === 'ADMIN_CONTABLE' || role === 'MEDICO') {
    items.push({ name: 'Consultas', label: 'Consultas', path: '/consultas', icon: 'pi pi-calendar' });
  }

  items.push({ name: 'FacturasPacientes', label: 'Facturas Pacientes', path: '/facturas-pacientes', icon: 'pi pi-file-invoice' });

  // Facturas ARS - solo ADMIN_CONTABLE, SUPER_ADMIN
  if (role === 'SUPER_ADMIN' || role === 'ADMIN_CONTABLE') {
    items.push({ name: 'FacturasARS', label: 'Facturas ARS', path: '/facturas-ars', icon: 'pi pi-file-check' });
  }

  // Contabilidad - solo SUPER_ADMIN, ADMIN_CONTABLE
  if (role === 'SUPER_ADMIN' || role === 'ADMIN_CONTABLE') {
    items.push({ name: 'Contabilidad', label: 'Libro Diario', path: '/contabilidad/libro-diario', icon: 'pi pi-book' });
  }

  if (role === 'SUPER_ADMIN' || role === 'ADMIN_CONTABLE') {
    items.push({ name: 'CuentasCobrar', label: 'Cuentas por Cobrar', path: '/contabilidad/cuentas-cobrar', icon: 'pi pi-dollar' });
  }

  // Configuración - solo ADMIN_CONTABLE, SUPER_ADMIN
  if (role === 'SUPER_ADMIN' || role === 'ADMIN_CONTABLE') {
    items.push({ name: 'Configuracion', label: 'Configuración', path: '/configuracion', icon: 'pi pi-cog' });
  }

  console.log('Items generados:', items.map(i => i.label));
  return items;
});

const formatRole = (role) => {
  const roleMap = {
    'SUPER_ADMIN': 'Super Admin',
    'ADMIN_CONTABLE': 'Admin Contable',
    'MEDICO': 'Médico',
    'ASISTENTE': 'Asistente'
  };
  return roleMap[role] || role;
};

const logout = () => {
  authStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.sidebar {
  width: 280px;
  min-height: 100vh;
  overflow-y: auto;
  background: linear-gradient(180deg, #8B4513 0%, #A0522D 100%);
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
}

.sidebar-header {
  padding: 25px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 2rem;
  color: #D2691E;
}

.logo-text {
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 5px;
  overflow-y: auto;
}

.sidebar-nav::-webkit-scrollbar {
  width: 6px;
}

.sidebar-nav::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.sidebar-nav::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
}


.sidebar-footer {
  padding: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  background: linear-gradient(180deg, #8B4513 0%, #A0522D 100%);
  flex-shrink: 0;  /* ← Evita que el footer se encoja */
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 25px;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border-left-color: #D2691E;
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  border-left-color: #CD853F;
}

.nav-item i {
  font-size: 1.2rem;
  width: 25px;
}

.nav-item span {
  font-weight: 500;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
}

.user-info i {
  font-size: 1.5rem;
  color: #CD853F;
}

.user-details {
  flex: 1;
}

.user-name {
  color: white;
  font-weight: 600;
  margin: 0 0 3px 0;
  font-size: 0.95rem;
}

.user-role {
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
  font-size: 0.85rem;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  color: white;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
}

.logout-btn i {
  font-size: 1.1rem;
}
</style>
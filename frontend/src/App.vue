<template>
  <Toast />
  <div class="mps-layout" v-if="$route.path !== '/login'">
    <aside class="sidebar">
      <div class="logo">
        <span class="logo-text">MPS ERP</span>
      </div>
      <nav class="menu">
        <router-link to="/dashboard" class="menu-item" active-class="active">
          <i class="pi pi-home"></i>
          <span>Dashboard</span>
        </router-link>
        <router-link to="/pacientes" class="menu-item" active-class="active">
          <i class="pi pi-users"></i>
          <span>Pacientes</span>
        </router-link>
        <router-link to="/citas" class="menu-item" active-class="active">
          <i class="pi pi-clock"></i>
          <span>Citas</span>
        </router-link>
        <router-link to="/consultas" class="menu-item" active-class="active">
          <i class="pi pi-calendar"></i>
          <span>Consultas</span>
        </router-link>
        <router-link to="/facturas-pacientes" class="menu-item" active-class="active">
          <i class="pi pi-file-pdf"></i>
          <span>Facturación e-CF</span>
        </router-link>
        <router-link to="/facturas-ars" class="menu-item" active-class="active">
          <i class="pi pi-building"></i>
          <span>Facturación ARS</span>
        </router-link>

        <!-- Contabilidad con submenú -->
        <div class="menu-item has-submenu" @click.stop="toggleSubmenu('contabilidad')">
          <i class="pi pi-book"></i>
          <span>Contabilidad</span>
          <i class="pi pi-chevron-down" :class="{ 'rotated': submenuOpen.contabilidad }"></i>
        </div>
        <div v-show="submenuOpen.contabilidad" class="submenu">
          <router-link to="/contabilidad/libro-diario" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-book"></i>
            <span>Libro Diario</span>
          </router-link>
          <router-link to="/contabilidad/cuentas-cobrar" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-dollar"></i>
            <span>Cuentas por Cobrar</span>
          </router-link>
          <router-link to="/contabilidad/antiguedad-saldos" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-chart-line"></i>
            <span>Antigüedad Saldos</span>
          </router-link>
          <router-link to="/contabilidad/cuentas-pagar" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-credit-card"></i>
            <span>Cuentas por Pagar</span>
          </router-link>
          <router-link to="/contabilidad/balance-general" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-chart-bar"></i>
            <span>Balance General</span>
          </router-link>
          <router-link to="/contabilidad/estado-resultados" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-chart-line"></i>
            <span>Estado Resultados</span>
          </router-link>
          <router-link to="/contabilidad/historial-cobros" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-history"></i>
            <span>Historial Cobros</span>
          </router-link>
          <router-link to="/contabilidad/historial-pagos" class="submenu-item" @click="submenuOpen.contabilidad = false">
            <i class="pi pi-history"></i>
            <span>Historial Pagos</span>
          </router-link>
        </div>

        <router-link to="/configuracion" class="menu-item" active-class="active">
          <i class="pi pi-cog"></i>
          <span>Configuración</span>
        </router-link>
      </nav>

      <div class="logout-btn" @click="handleLogout">
        <i class="pi pi-power-off"></i>
        <span>Cerrar Sesión</span>
      </div>
    </aside>

    <main class="main-content">
      <header class="topbar">
        <div class="search-box">
          <i class="pi pi-search"></i>
          <input type="text" placeholder="Buscar..." />
        </div>
        <div class="user-info">
          <div class="avatar">
            <i class="pi pi-heart"></i>
          </div>
          <div class="user-details">
            <div class="username">Dr. {{ userInitials }}</div>
            <div class="user-role">{{ userRole }}</div>
          </div>
        </div>
      </header>

      <div class="dashboard-content">
        <router-view />
      </div>
    </main>
  </div>
  <div v-else>
    <router-view />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useAuthStore } from './stores/auth';
import { onMounted, onUnmounted, nextTick } from 'vue';
import { computed, ref, watch } from 'vue';

const router = useRouter();
const authStore = useAuthStore();

// Estado para submenús
const submenuOpen = ref({
  contabilidad: false
});

const toggleSubmenu = (menu) => {
  submenuOpen.value[menu] = !submenuOpen.value[menu];
};

// Cerrar submenús al hacer clic fuera
const handleClickOutside = (event) => {
  const sidebar = document.querySelector('.sidebar');
  if (sidebar && !sidebar.contains(event.target)) {
    submenuOpen.value.contabilidad = false;
  }
};

// Abrir submenú automáticamente si la ruta actual está dentro
watch(() => router.currentRoute.value.path, (newPath) => {
  if (newPath.includes('/contabilidad')) {
    submenuOpen.value.contabilidad = true;
  }
}, { immediate: true });

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};

// Computed para iniciales del nombre
const userInitials = computed(() => {
  const nombre = authStore.user?.nombreCompleto || '';
  if (nombre.includes(' ')) {
    const partes = nombre.split(' ');
    return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
  }
  return nombre.substring(0, 2).toUpperCase() || 'DR';
});

// Computed para rol
const userRole = computed(() => {
  const role = authStore.user?.role;
  const roleMap = {
    'SUPER_ADMIN': 'Super Administrador',
    'ADMIN_CONTABLE': 'Admin Contable',
    'MEDICO': 'Médico',
    'ASISTENTE': 'Asistente'
  };
  return roleMap[role] || role || 'Médico';
});

// Sincronización de scroll horizontal para todas las tablas
let scrollTopObserver = null;

const initScrollSync = () => {
  const tableWrappers = document.querySelectorAll('.p-datatable-wrapper');

  tableWrappers.forEach(tableWrapper => {
    let scrollTop = tableWrapper.parentElement?.querySelector('.scroll-top-sync');

    if (!scrollTop && tableWrapper.parentElement) {
      scrollTop = document.createElement('div');
      scrollTop.className = 'scroll-top-sync';

      const spacer = document.createElement('div');
      spacer.className = 'scroll-spacer';
      spacer.style.height = '1px';
      scrollTop.appendChild(spacer);

      tableWrapper.parentElement.insertBefore(scrollTop, tableWrapper);

      scrollTop.addEventListener('scroll', (e) => {
        tableWrapper.scrollLeft = e.target.scrollLeft;
      });

      tableWrapper.addEventListener('scroll', (e) => {
        if (scrollTop) scrollTop.scrollLeft = e.target.scrollLeft;
      });

      const updateSpacerWidth = () => {
        const table = tableWrapper.querySelector('.p-datatable-table');
        if (table && spacer) {
          const scrollWidth = table.scrollWidth;
          spacer.style.width = scrollWidth + 'px';
        }
      };

      updateSpacerWidth();

      const resizeObserver = new ResizeObserver(() => updateSpacerWidth());
      resizeObserver.observe(tableWrapper);
    }
  });
};

onMounted(() => {
  authStore.checkAuth();
  document.addEventListener('click', handleClickOutside);

  nextTick(() => {
    initScrollSync();
  });

  scrollTopObserver = new MutationObserver(() => {
    nextTick(() => initScrollSync());
  });
  scrollTopObserver.observe(document.body, { childList: true, subtree: true });
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
  if (scrollTopObserver) scrollTopObserver.disconnect();
});
</script>

<style>
:root {
  --primary-amber: #d97706;
  --primary-brown: #78350f;
  --bg-dark: #1e1b1e;
  --bg-card: #ffffff;
}

body {
  margin: 0;
  font-family: 'Inter', sans-serif;
  background-color: #f3f4f6;
  color: #1f2937;
}

.mps-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 260px;
  background-color: var(--primary-brown);
  color: white;
  padding: 2rem 1rem;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  align-self: flex-start;
  min-height: 100vh;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 800;
  color: #fbbf24;
  margin-bottom: 3rem;
  display: block;
  text-align: center;
}

.menu {
  flex: 1;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s;
  color: #fed7aa;
  text-decoration: none;
  margin-bottom: 0.5rem;
}

.menu-item:hover, .menu-item.active {
  background-color: rgba(251, 191, 36, 0.2);
  color: white;
}

.menu-item.has-submenu {
  justify-content: space-between;
}

.menu-item.has-submenu i:last-child {
  transition: transform 0.3s ease;
}

.menu-item.has-submenu i.rotated {
  transform: rotate(180deg);
}

.submenu {
  padding-left: 2rem;
  margin-bottom: 0.5rem;
}

.submenu-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s;
  color: #fed7aa;
  text-decoration: none;
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}

.submenu-item:hover {
  background-color: rgba(251, 191, 36, 0.2);
  color: white;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  color: #fca5a5;
  cursor: pointer;
  border-top: 1px solid rgba(255,255,255,0.1);
  margin-top: auto;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 100vh;
}

.topbar {
  height: 70px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  border-bottom: 1px solid #e5e7eb;
}

.dashboard-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

/* Scroll horizontal para tablas */
.p-datatable-wrapper {
  overflow-x: auto !important;
}

.p-datatable-table {
  min-width: 100%;
  width: max-content;
}

/* Scroll horizontal para dropdowns */
.p-dropdown-panel .p-dropdown-items-wrapper {
  overflow-x: auto !important;
  max-width: 90vw;
}

.p-dropdown-panel .p-dropdown-item {
  white-space: nowrap;
}

/* Scroll horizontal para diálogos */
.p-dialog .p-dialog-content {
  overflow-x: auto !important;
}

.p-dialog .p-datatable-wrapper {
  overflow-x: auto !important;
}

/* Scroll superior sincronizado */
.scroll-top-sync {
  overflow-x: auto;
  overflow-y: hidden;
  height: 16px;
  margin-bottom: 8px;
}

.scroll-spacer {
  height: 1px;
}

/* Scrollbar personalizada */
.scroll-top-sync::-webkit-scrollbar {
  height: 8px;
}

.scroll-top-sync::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.scroll-top-sync::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.scroll-top-sync::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Ocultar scroll superior en móviles */
@media (max-width: 768px) {
  .scroll-top-sync {
    display: none;
  }
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
  line-height: 1.3;
}

.username {
  font-weight: 600;
  color: #333;
  display: block;
  font-size: 0.9rem;
}

.user-role {
  font-size: 0.70rem;
  color: #666;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #8B4513, #D2691E);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
  flex-shrink: 0;
}

/* Responsive - Tablets */
@media (max-width: 1024px) {
  .sidebar {
      width: 80px;
      padding: 1rem 0.5rem;
      min-height: 0;           /* ← AGREGA: por consistencia */
    }

    /* Ocultar contabilidad submenu en sidebar colapsado */
    .submenu {
      display: none;           /* ← AGREGA: submenu no cabe en 80px */
    }

    .menu-item.has-submenu i:last-child {
      display: none;           /* ← Ocultar chevron */
    }

  .logo-text {
    font-size: 0;
    margin-bottom: 1rem;
  }

  .logo-text:before {
    content: "M";
    font-size: 1.5rem;
    font-weight: 800;
    display: block;
    text-align: center;
  }

  .menu-item span,
  .logout-btn span,
  .submenu-item span {
    display: none;
  }

  .menu-item i,
  .logout-btn i,
  .submenu-item i {
    font-size: 1.5rem;
    margin: 0 auto;
  }

  .menu-item {
    justify-content: center;
    padding: 0.75rem;
  }

  .submenu-item {
    justify-content: center;
    padding: 0.75rem;
  }

  .submenu {
    padding-left: 0;
  }

  .logout-btn {
    justify-content: center;
  }

  .topbar {
    padding: 0 1rem;
  }

  .search-box input {
    width: 150px;
  }

  .dashboard-content {
    padding: 1rem;
  }
}

/* Responsive - Móviles */
@media (max-width: 768px) {
  .mps-layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    flex-direction: row;
    padding: 0.5rem;
    height: auto;
    min-height: 0;
    position: sticky;
    top: 0;
    z-index: 100;
  }

  .logo {
      display: none;
    }

  .menu {
      display: flex;
      flex: 1;
      justify-content: flex-start;     /* ← CAMBIA de space-around a flex-start */
      margin: 0;
      overflow-x: auto;               /* ← AGREGA: scroll horizontal si desborda */
      gap: 0.5rem;                    /* ← AGREGA: espacio entre items */
      -webkit-overflow-scrolling: touch;
    }

    /* Ocultar scrollbar en mobile */
    .menu::-webkit-scrollbar {
      display: none;
    }

   .menu-item {
      flex-direction: column;
      align-items: center;
      padding: 0.5rem;
      font-size: 0.7rem;
      flex-shrink: 0;           /* ← AGREGA: items no se encogen */
      min-width: 55px;          /* ← AGREGA: ancho mínimo para cada item */
    }

  .menu-item i {
    font-size: 1.2rem;
  }

  .submenu {
    display: none;
  }

 .logout-btn {
     flex-direction: column;
     align-items: center;
     padding: 0.5rem;
     margin-top: 0;
     border-top: none;
     flex-shrink: 0;
   }

  .logout-btn span {
    font-size: 0.7rem;
  }

  .topbar {
    height: auto;
    padding: 0.5rem;
    flex-wrap: wrap;
    gap: 0.5rem;
  }

  .search-box {
    order: 1;
    width: 100%;
  }

  .search-box input {
    width: 100%;
  }

  .user-info {
    order: 0;
  }

  .dashboard-content {
    padding: 0.75rem;
  }

  .user-details {
    display: none;
  }

  .avatar {
    width: 35px;
    height: 35px;
    font-size: 1rem;
  }
}

/* Ajustes para móviles muy pequeños */
@media (max-width: 480px) {
  .menu-item span {
    display: none;
  }

  .menu-item i {
    font-size: 1rem;
  }

  .logout-btn span {
    display: none;
  }
}
</style>

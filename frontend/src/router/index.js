import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Pacientes from '../views/Pacientes.vue'
import Consultas from '../views/Consultas.vue'
import Citas from '../views/Citas.vue'
import FacturasPacientes from '../views/FacturasPacientes.vue'
import FacturasARS from '../views/FacturasARS.vue'
import Configuracion from '../views/Configuracion.vue'
import AccountingLedger from '../views/AccountingLedger.vue';
import CuentasCobrar from '../views/CuentasCobrar.vue';
import AntiguedadSaldos from '../views/AntiguedadSaldos.vue';
import CuentasPagar from '../views/CuentasPagar.vue';
import BalanceGeneral from '../views/BalanceGeneral.vue';
import EstadoResultados from '../views/EstadoResultados.vue';
import HistorialCobros from '../views/HistorialCobros.vue';
import HistorialPagos from '../views/HistorialPagos.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/pacientes',
    name: 'Pacientes',
    component: Pacientes,
    meta: { requiresAuth: true }
  },
  {
    path: '/consultas',
    name: 'Consultas',
    component: Consultas,
    meta: { requiresAuth: true }
  },
  {
    path: '/citas',
    name: 'Citas',
    component: Citas,
    meta: { requiresAuth: true }
  },
  {
    path: '/facturas-pacientes',
    name: 'FacturasPacientes',
    component: FacturasPacientes,
    meta: { requiresAuth: true }
  },
  {
    path: '/facturas-ars',
    name: 'FacturasARS',
    component: FacturasARS,
    meta: { requiresAuth: true, requiresSuperAdmin: true }  // ← Agregado
  },
  {
    path: '/contabilidad/libro-diario',
    name: 'Contabilidad',
    component: AccountingLedger,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  },
  {
    path: '/contabilidad/cuentas-cobrar',
    name: 'CuentasCobrar',
    component: CuentasCobrar,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  },

  {
    path: '/contabilidad/antiguedad-saldos',
    name: 'AntiguedadSaldos',
    component: AntiguedadSaldos,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  },
  {
    path: '/contabilidad/cuentas-pagar',
    name: 'CuentasPagar',
    component: CuentasPagar,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  },
  {
    path: '/contabilidad/balance-general',
    name: 'BalanceGeneral',
    component: BalanceGeneral,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  },
  {
    path: '/contabilidad/estado-resultados',
    name: 'EstadoResultados',
    component: EstadoResultados,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  },
    {
      path: '/contabilidad/historial-cobros',
      name: 'HistorialCobros',
      component: HistorialCobros,
      meta: { requiresAuth: true, requiresSuperAdmin: true }
    },
    {
      path: '/contabilidad/historial-pagos',
      name: 'HistorialPagos',
      component: HistorialPagos,
      meta: { requiresAuth: true, requiresSuperAdmin: true }
    },
  {
    path: '/configuracion',
    name: 'Configuracion',
    component: Configuracion,
    meta: { requiresAuth: true, requiresSuperAdmin: true }  // ← Agregado
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();

  // Cargar usuario desde localStorage si existe
  await authStore.checkAuth();

  // Verificar autenticación
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login');
    return;
  }

  // Verificar si la ruta requiere SUPER_ADMIN
  if (to.meta.requiresSuperAdmin && authStore.user?.role !== 'SUPER_ADMIN') {
    next('/dashboard');
    return;
  }

  // Redirigir si ya está logueado e intenta ir al login
  if (to.path === '/login' && authStore.isAuthenticated) {
    next('/dashboard');
    return;
  }

  next();
});

export default router
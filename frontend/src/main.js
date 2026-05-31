import { createApp } from 'vue';
import { createPinia } from 'pinia';
import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import Aura from '@primevue/themes/aura';
import App from './App.vue';
import router from './router';
import './style.css';
import 'primeicons/primeicons.css';

// PrimeVue
import Card from 'primevue/card';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import Toast from 'primevue/toast';

// Store
import { useAuthStore } from './stores/auth';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);
app.use(ToastService);
app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: 'system',
            cssLayer: false
        }
    }
});

// Componentes globales
app.component('Card', Card);
app.component('InputText', InputText);
app.component('Password', Password);
app.component('Button', Button);
app.component('Toast', Toast);

// ✅ Cargar auth ANTES de montar la app (sin top-level await)
const authStore = useAuthStore();
authStore.checkAuth().then(() => {
    // ✅ Montar app solo después de cargar auth
    app.mount('#app');
}).catch((error) => {
    console.error('Error loading auth:', error);
    // Aún así montar la app para no dejar la pantalla en blanco
    app.mount('#app');
});
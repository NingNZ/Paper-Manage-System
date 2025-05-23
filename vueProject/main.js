import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import Particles from "particles.vue3";
import 'element-plus/dist/index.css'
import router from './route'
const app = createApp(App)
app.use(ElementPlus)
app.use(Particles)
app.use(router)
app.mount('#app')
import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from './router'
import 'virtual:uno.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import Carousel from 'vue-carousel';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


const app = createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(createPinia()).use(router).use(ElementPlus).use(Carousel).mount('#app');

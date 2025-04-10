import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as icons from '@element-plus/icons-vue'

const app = createApp(App)

// 注册Element Plus图标
Object.keys(icons).forEach(key => {
    app.component(key, icons[key])
})

app.use(router)
app.use(ElementPlus)

app.mount('#app')
import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/login/index.vue'
import LayoutView from '@/views/layout/index.vue'
import HallView from '@/views/hall/index.vue'
import MovieView from '@/views/movie/index.vue'
import UserView from '@/views/user/index.vue'
import ScreeningView from '@/views/screening/index.vue'
import LogView from '@/views/log/index.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: '',
            component: LayoutView,
            redirect: '/hall', //重定向对于根路径的访问
            children: [
                {path: '/hall', name: 'hall', component: HallView},
                {path: '/movie', name: 'movie', component: MovieView},
                {path: '/user', name: 'user', component: UserView},
                {path: '/screening', name: 'screening', component: ScreeningView},
                {path: '/log', name: 'log', component: LogView}
            ]
        },
        {path: '/login', name: 'login', component: LoginView}
    ]
})

export default router
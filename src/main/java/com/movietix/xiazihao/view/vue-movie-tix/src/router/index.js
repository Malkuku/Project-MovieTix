import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/Login/index.vue'
import LayoutView from '@/views/Layout/index.vue'
import HallView from '@/views/Hall/index.vue'
import MovieView from '@/views/Movie/index.vue'
import UserView from '@/views/User/index.vue'
import ScreeningView from '@/views/Screening/index.vue'
import LogView from '@/views/Log/index.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: '',
            component: LayoutView,
            redirect: '/hall', //重定向对于根路径的访问
            children: [
                {path: '/hall', name: 'index', component: HallView},
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
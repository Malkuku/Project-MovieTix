import {createRouter, createWebHistory} from 'vue-router'
import LoginView from '@/views/login/index.vue'
import LayoutView from '@/views/layout/index.vue'
import HallView from '@/views/hall/index.vue'
import MovieView from '@/views/movie/index.vue'
import UserView from '@/views/user/index.vue'
import ScreeningView from '@/views/screening/index.vue'
import LogView from '@/views/log/index.vue'
import OrderView from '@/views/order/index.vue'
import RegisterView from '@/views/register/index.vue'
import ProfileView from '@/views/user_profile/index.vue'
import HomeView from '@/views/homeView/index.vue'
import { useUserStore } from '@/stores/user';


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'root',
            component: HomeView,
            redirect: '/home'
        },
        {
            path: '/layout',
            name: 'layout',
            component: LayoutView,
            redirect: '/hall',
            meta: { requiresAuth: true }, // 添加元数据标记需要认证
            children: [
                { path: '/hall', name: 'hall', component: HallView },
                { path: '/movie', name: 'movie', component: MovieView },
                { path: '/user', name: 'user', component: UserView },
                { path: '/screening', name: 'screening', component: ScreeningView },
                { path: '/log', name: 'log', component: LogView },
                { path: '/order', name: 'order', component: OrderView }
            ]
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
            meta: { guestOnly: true } // 添加元数据标记仅游客可访问
        },
        {
            path: '/register',
            name: 'register',
            component: RegisterView,
            meta: { guestOnly: true }
        },
        {
            path: '/profile',
            name: 'profile',
            component: ProfileView,
            meta: { requiresAuth: true }
        },
        { path: '/home', name: 'home', component: HomeView }
    ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    const isAuthenticated = userStore.isAuthenticated()

    // 检查需要认证的路由
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!isAuthenticated) {
            next({
                path: '/login',
                query: { redirect: to.fullPath } // 保存目标路径以便登录后重定向
            })
            return
        }
    }

    // 检查仅限游客访问的路由
    if (to.matched.some(record => record.meta.guestOnly)) {
        if (isAuthenticated) {
            next('/') // 已登录用户访问登录/注册页时重定向到首页
            return
        }
    }

    next()
})

export default router


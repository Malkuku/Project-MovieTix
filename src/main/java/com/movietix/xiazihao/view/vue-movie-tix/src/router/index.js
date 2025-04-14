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
import { useUserStore } from '@/store/user';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: '',
            component: HomeView,
            redirect: '/home'
        },
        {
            path: '/layout',
            name: 'layout',
            component: LayoutView,
            redirect: '/hall',
            children:[
                {path: '/hall', name: 'hall', component: HallView},
                {path: '/movie', name: 'movie', component: MovieView},
                {path: '/user', name: 'user', component: UserView},
                {path: '/screening', name: 'screening', component: ScreeningView},
                {path: '/log', name: 'log', component: LogView},
                {path: '/order', name: 'order', component: OrderView}
            ]
        },
        {path: '/login', name: 'login', component: LoginView},
        {path: '/register', name: 'register', component: RegisterView},
        {path: '/profile', name: 'profile', component: ProfileView},
        {path: '/home', name: 'home', component: HomeView}
    ]
})

//路由护卫
router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    if (to.meta.requiresAuth && !userStore.isAuthenticated()) {
        next('/login');
    } else if ((to.name === 'login' || to.name === 'register') && userStore.isAuthenticated()) {
        next('/');
    } else {
        next();
    }
});

export default router



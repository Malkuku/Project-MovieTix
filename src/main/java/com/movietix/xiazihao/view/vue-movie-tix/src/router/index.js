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
import RefundView from '@/views/refund/index.vue'
import UserOrderView from '@/views/user_order/index.vue'
import ErrorView from '@/views/error/index.vue'
import PlaceHolderView from '@/views/placeHolder/index.vue'
import {useUserStore} from '@/stores/user';


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
            meta: { requiresAuth: true, requiresAdmin: true }, // 添加元数据标记需要认证
            children: [
                { path: '/hall', name: 'hall', component: HallView },
                { path: '/movie', name: 'movie', component: MovieView },
                { path: '/user', name: 'user', component: UserView },
                { path: '/screening', name: 'screening', component: ScreeningView },
                { path: '/log', name: 'log', component: LogView },
                { path: '/order', name: 'order', component: OrderView },
                { path: '/refund', name: 'refund', component: RefundView},
                { path:'/movie/category', name: 'category', component: PlaceHolderView,meta: { requiresDev: true }},
                { path:'/screening/schedule', name: 'schedule', component: PlaceHolderView,meta: { requiresDev: true }},
                { path:'/finance/daily', name: 'daily', component: PlaceHolderView,meta: { requiresDev: true }},
                { path:'/finance/monthly', name: 'monthly', component: PlaceHolderView,meta: { requiresDev: true }},
                { path:'/system/log', name: 'log', component: PlaceHolderView,meta: { requiresDev: true }},
                { path:'/system/setting', name: 'setting', component: PlaceHolderView,meta: { requiresDev: true }},
                { path:'/movie/:movieId', name: 'movie_detail', component: PlaceHolderView,meta: { requiresDev: true }},
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
        {
            path: '/user_order',
            name: 'user_order',
            component: UserOrderView,
            meta: { requiresAuth: true }
        },
        { path: '/home', name: 'home', component: HomeView },
        { path: '/error', name: 'error', component: ErrorView}
    ]
})


// 路由守卫 //TODO配置对管理员入口的验证
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

    //检查需要管理员权限
    if(to.matched.some(record => record.meta.requiresAdmin)){
        if(!userStore.adminToken){
            next({
                path: '/error',
                query: {
                    errorMessage: '您没有管理员权限，无法访问此页面',
                    redirectTo: '/'
                }
            })
        }
    }

    // 检查需要开发者权限 //TODO 暂时需要手动关闭
    if (to.matched.some(record => record.meta.requiresDev)) {
        if (1 === 1) {
            next({
                path: '/error',
                query: {
                    errorMessage: '噢，非常抱歉，此界面还在开发中😣',
                    redirectTo: '/'
                }
            })
        }
    }
    next()
})

export default router


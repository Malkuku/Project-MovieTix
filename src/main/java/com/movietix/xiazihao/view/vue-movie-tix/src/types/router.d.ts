import 'vue-router'

declare module 'vue-router' {
    interface RouteMeta {
        //路由元字段类型
        requiresAuth?: boolean //需要登录
        guestOnly?: boolean //仅限游客
        requiresAdmin?: boolean //需要管理员权限
        requiresDev?: boolean //开发中
        title?: string
    }
}
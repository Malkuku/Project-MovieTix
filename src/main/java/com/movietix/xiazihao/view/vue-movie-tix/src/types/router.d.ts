import 'vue-router'

declare module 'vue-router' {
    interface RouteMeta {
        //路由元字段类型
        requiresAuth?: boolean //?可选属性
        guestOnly?: boolean
        title?: string
    }
}
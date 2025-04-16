<script setup>
import {
  EditPen,
  Film,
  HomeFilled,
  List,
  Money,
  Promotion,
  SwitchButton,
  Tools,
  User,
  VideoCamera
} from '@element-plus/icons-vue'
import router from "@/router";

// 手动路由方法
const goToHome = () => {
  router.push('/home');
};
</script>

<template>
  <div class="common-layout">
    <el-container class="layout-container">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-content">
          <div class="logo">
            <span class="title">🎬 影院管理系统</span>
            <span class="subtitle">专业影院运营平台</span>
          </div>
          <div class="right_tool">
            <el-dropdown>
              <span class="user-actions">
                <el-avatar :size="32" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
                <span class="username">管理员</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-icon><EditPen /></el-icon> 修改密码
                  </el-dropdown-item>
                  <el-dropdown-item divided>
                    <el-icon><SwitchButton /></el-icon> 退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-container>
        <!-- 左侧菜单栏 -->
        <el-aside width="220px" class="aside">
          <el-menu
              router
              active-text-color="#ffd04b"
              background-color="#2c3e50"
              class="sidebar-menu"
              text-color="#b8c7ce"
              :default-active="$route.path"
          >
            <!-- 首页 -->
            <el-menu-item index="/home">
              <el-icon><HomeFilled /></el-icon>
              <span>回到首页</span>
            </el-menu-item>

            <!-- 影厅管理 -->
            <el-menu-item index="/hall">
              <el-icon><Promotion /></el-icon>
              <span>影厅管理</span>
            </el-menu-item>

            <!-- 影片管理 -->
            <el-sub-menu index="film-management">
              <template #title>
                <el-icon><Film /></el-icon>
                <span>影片管理</span>
              </template>
              <el-menu-item index="/movie">全部影片</el-menu-item>
              <el-menu-item index="/movie/category">影片分类</el-menu-item>
            </el-sub-menu>

            <!-- 排片管理 -->
            <el-sub-menu index="screening-management">
              <template #title>
                <el-icon><VideoCamera /></el-icon>
                <span>排片管理</span>
              </template>
              <el-menu-item index="/screening">排片列表</el-menu-item>
              <el-menu-item index="/screening/schedule">排片计划</el-menu-item>
            </el-sub-menu>

            <!-- 订单管理 -->
            <el-sub-menu index="order-management">
              <template #title>
                <el-icon><List /></el-icon>
                <span>订单管理</span>
              </template>
              <el-menu-item index="/order">全部订单</el-menu-item>
              <el-menu-item index="/refund">退款申请</el-menu-item>
            </el-sub-menu>

            <!-- 用户管理 -->
            <el-menu-item index="/user">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>

            <!-- 财务管理 -->
            <el-sub-menu index="finance-management">
              <template #title>
                <el-icon><Money /></el-icon>
                <span>财务管理</span>
              </template>
              <el-menu-item index="/finance/daily">日报表</el-menu-item>
              <el-menu-item index="/finance/monthly">月报表</el-menu-item>
            </el-sub-menu>

            <!-- 系统管理 -->
            <el-sub-menu index="system-management">
              <template #title>
                <el-icon><Tools /></el-icon>
                <span>系统管理</span>
              </template>
              <el-menu-item index="/system/log">系统日志</el-menu-item>
              <el-menu-item index="/system/setting">系统设置</el-menu-item>
              <el-menu-item index="/system/account">账号管理</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>

        <!-- 主内容区 -->
        <el-main class="main-content">
          <div class="content-container">
            <!-- 面包屑导航 -->
            <div class="breadcrumb-container">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item v-for="(item, index) in $route.matched" :key="index">
                  {{ item.meta.title || item.name }}
                </el-breadcrumb-item>
              </el-breadcrumb>
            </div>

            <!-- 页面内容 -->
            <router-view v-slot="{ Component }">
              <transition name="fade" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.header {
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 64px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
}

.title {
  color: white;
  font-size: 22px;
  font-weight: 700;
  margin-right: 10px;
}

.subtitle {
  color: rgba(255, 255, 255, 0.8);
  font-size: 16px;
}

.right_tool {
  display: flex;
  align-items: center;
}

.user-actions {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
}

.username {
  margin-left: 10px;
  font-weight: 500;
}

.aside {
  background-color: #2c3e50;
  transition: width 0.3s;
}

.sidebar-menu {
  border-right: none;
  height: calc(100vh - 64px);
  overflow-y: auto;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 220px;
}

.main-content {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}

.content-container {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  min-height: calc(100vh - 104px);
}

.breadcrumb-container {
  margin-bottom: 20px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .aside {
    width: 64px !important;
  }

  .title {
    font-size: 18px;
  }

  .subtitle {
    display: none;
  }

  .username {
    display: none;
  }

  .breadcrumb-container {
    display: none;
  }
}
</style>
<template>
  <div class="app-container">
    <!-- 侧边栏 -->
    <el-container>
      <el-aside width="200px">
        <el-menu
            router
            :default-active="route.path"
            class="el-menu-vertical"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
        >
          <div class="logo-container">
            <h2>影院管理系统</h2>
          </div>
          <el-menu-item index="/hall">
            <el-icon><icon-menu /></el-icon>
            <span>放映厅管理</span>
          </el-menu-item>
          <el-menu-item index="/movie">
            <el-icon><video-play /></el-icon>
            <span>电影管理</span>
          </el-menu-item>
          <el-menu-item index="/screening">
            <el-icon><Calendar /></el-icon>
            <span>排片管理</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><user /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/log">
            <el-icon><document /></el-icon>
            <span>日志管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <el-header>
          <div class="header-right">
            <el-dropdown>
              <span class="el-dropdown-link">
                <el-avatar :size="30" :src="userAvatar" />
                <span style="margin-left: 8px">{{ username }}</span>
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main>
          <!-- 面包屑导航 -->
          <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
          </el-breadcrumb>

          <!-- 路由视图 -->
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Menu as IconMenu,
  VideoPlay,
  Calendar,
  User,
  Document,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 模拟用户数据
const username = ref('管理员')
const userAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')

// 获取当前路由名称用于面包屑
const currentRouteName = computed(() => {
  const routeMap = {
    'hall': '放映厅管理',
    'movie': '电影管理',
    'user': '用户管理',
    'screening': '排片管理',
    'log': '日志管理'
  }
  return routeMap[route.name] || '首页'
})

// 退出登录
const handleLogout = () => {
  // 这里应该调用退出登录的API
  router.push('/login')
}
</script>

<style scoped>
.app-container {
  height: 100vh;
  overflow: hidden;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #545c64;
  color: #fff;
}

.el-menu {
  border-right: none;
}

.el-header {
  background-color: #fff;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  background-color: #434a50;
}

.breadcrumb {
  margin-bottom: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
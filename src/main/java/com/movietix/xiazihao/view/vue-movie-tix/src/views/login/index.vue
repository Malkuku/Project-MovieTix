<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">用户登录</h2>
      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              show-password
              prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              class="login-btn"
              @click="handleLogin"
              :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <span>还没有账号？</span>
        <el-link type="primary" @click="toRegister">立即注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginApi } from '@/api/user_work';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

const loginForm = ref({
  username: '',
  password: ''
});

const loginRules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '长度在4到20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度不能少于8位', trigger: 'blur' }
  ]
});

const loginFormRef = ref();
const loading = ref(false);

const handleLogin = async () => {
  if (!loginFormRef.value) return;

  try {
    await loginFormRef.value.validate();
    loading.value = true;

    const { username, password } = loginForm.value;
    const result = await loginApi(username, password);

    if (result.code === 1) {
      ElMessage.success('登录成功');
      userStore.setUserInfo(result.data);
      router.push('/');
    } else {
      ElMessage.error(result.msg || '登录失败');
    }
  } catch (error) {
    console.error('登录出错:', error);
  } finally {
    loading.value = false;
  }
};

const toRegister = () => {
  router.push('/register');
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #409EFF;
}

.login-btn {
  width: 100%;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}
</style>
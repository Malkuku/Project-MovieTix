<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          @keyup.enter="handleRegister"
      >
        <el-form-item prop="username">
          <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名(4-20位字符)"
              prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码(至少8位，包含字母和数字)"
              show-password
              prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              show-password
              prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              class="register-btn"
              @click="handleRegister"
              :loading="loading"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-footer">
        <span>已有账号？</span>
        <el-link type="primary" @click="toLogin">立即登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { registerApi } from '@/api/user_work';
import { ElMessage } from 'element-plus';

const router = useRouter();

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
});

const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入密码'));
  } else if (value.length < 8) {
    callback(new Error('密码长度不能少于8位'));
  } else if (!/[a-zA-Z]/.test(value) || !/\d/.test(value)) {
    callback(new Error('密码必须包含字母和数字'));
  } else {
    callback();
  }
};

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const registerRules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '长度在4到20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
});

const registerFormRef = ref();
const loading = ref(false);

const handleRegister = async () => {
  if (!registerFormRef.value) return;

  try {
    await registerFormRef.value.validate();
    loading.value = true;

    const { username, password } = registerForm.value;
    const result = await registerApi(username, password);

    if (result.code === 1) {
      ElMessage.success('注册成功');
      router.push('/login');
    } else {
      ElMessage.error(result.msg || '注册失败');
    }
  } catch (error) {
    console.error('注册出错:', error);
  } finally {
    loading.value = false;
  }
};

const toLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.register-card {
  width: 400px;
  padding: 20px;
}

.register-title {
  text-align: center;
  margin-bottom: 30px;
  color: #409EFF;
}

.register-btn {
  width: 100%;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}
</style>
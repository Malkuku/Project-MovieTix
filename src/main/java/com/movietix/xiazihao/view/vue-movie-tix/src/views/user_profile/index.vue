<template>
  <div class="user-profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button type="primary" @click="handleEdit">编辑</el-button>
        </div>
      </template>

      <el-form
          ref="profileFormRef"
          :model="profileForm"
          :rules="profileRules"
          label-width="100px"
          :disabled="!isEditing"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="profileForm.userId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="profileForm.username" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="profileForm.nickname" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="profileForm.realName" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="profileForm.gender" placeholder="请选择性别">
                <el-option label="未知" :value="0" />
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker
                  v-model="profileForm.birthday"
                  type="Date"
                  placeholder="选择日期"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="profileForm.phone" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="profileForm.email" />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="profileForm.idCard" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="省份" prop="province">
              <el-input v-model="profileForm.province" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input v-model="profileForm.city" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="个人签名" prop="signature">
          <el-input
              v-model="profileForm.signature"
              type="textarea"
              :rows="3"
              placeholder="请输入您的个性签名"
          />
        </el-form-item>

        <el-form-item label="头像" prop="avatar">
          <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="profileForm.avatar" :src="profileForm.avatar" class="avatar"  alt="加载失败"/>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item v-if="isEditing">
          <el-button type="primary" @click="handleSave">保存</el-button>
          <el-button @click="cancelEdit">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="30%">
      <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-width="100px"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
              v-model="passwordForm.oldPassword"
              type="password"
              show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="passwordForm.newPassword"
              type="password"
              show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="passwordForm.confirmPassword"
              type="password"
              show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updatePassword">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 充值对话框 -->
    <el-dialog v-model="rechargeDialogVisible" title="账户充值" width="30%">
      <el-form
          ref="rechargeFormRef"
          :model="rechargeForm"
          :rules="rechargeRules"
          label-width="100px"
      >
        <el-form-item label="充值金额" prop="amount">
          <el-input-number
              v-model="rechargeForm.amount"
              :min="1"
              :max="10000"
              :precision="2"
              :step="100"
          />
          <span style="margin-left: 10px;">元</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rechargeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRecharge">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <div class="action-buttons">
      <el-button type="primary" @click="showPasswordDialog">修改密码</el-button>
      <el-button type="success" @click="showRechargeDialog">账户充值</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage} from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import {
  getUserProfileApi,
  updateUserProfileApi,
  updatePasswordApi,
  rechargeApi
} from '@/api/user_work';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

const profileForm = ref({
  userId: null,
  username: null,
  nickname: null,
  realName: null,
  idCard: null,
  phone: null,
  gender: 0,
  email: null,
  birthday: null,
  avatar: null,
  city: null,
  province: null,
  signature: null
});

const profileRules = ref({
  nickname: [
    { max: 20, message: '昵称长度不能超过20个字符', trigger: 'blur' }
  ],
  realName: [
    { max: 20, message: '真实姓名长度不能超过20个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  idCard: [
    { pattern: /^\d{17}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  ]
});

const isEditing = ref(false);
const originalProfile = ref({});

const profileFormRef = ref();

const handleEdit = () => {
  isEditing.value = true;
  originalProfile.value = { ...profileForm.value };
};

const cancelEdit = () => {
  isEditing.value = false;
  profileForm.value = { ...originalProfile.value };
};

const handleSave = async () => {
  if (!profileFormRef.value) return;

  try {
    await profileFormRef.value.validate();
    const result = await updateUserProfileApi(profileForm.value);

    if (result.code === 1) {
      ElMessage.success('保存成功');
      await userStore.updateUserProfile();
      isEditing.value = false;
      await fetchUserProfile();
    } else {
      ElMessage.error(result.msg || '保存失败');
    }
  } catch (error) {
    console.error('保存出错:', error);
  }
};

const handleAvatarSuccess = (response) => {
  profileForm.value.avatar = response.data.url;
};

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!');
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!');
  }
  return isJPG && isLt2M;
};

// 修改密码相关
const passwordDialogVisible = ref(false);
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

const validateNewPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入新密码'));
  } else if (value.length < 8) {
    callback(new Error('密码长度不能少于8位'));
  } else if (!/[a-zA-Z]/.test(value) || !/\d/.test(value)) {
    callback(new Error('密码必须包含字母和数字'));
  } else {
    callback();
  }
};

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.value.newPassword) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const passwordRules = ref({
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, validator: validateNewPassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
});

const passwordFormRef = ref();

const showPasswordDialog = () => {
  passwordDialogVisible.value = true;
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields();
  }
};

const updatePassword = async () => {
  if (!passwordFormRef.value) return;

  try {
    await passwordFormRef.value.validate();
    const { oldPassword, newPassword } = passwordForm.value;
    const result = await updatePasswordApi(
        profileForm.value.userId,
        oldPassword,
        newPassword
    );

    if (result.code === 1) {
      ElMessage.success('密码修改成功');
      passwordDialogVisible.value = false;
    } else {
      ElMessage.error(result.msg || '密码修改失败');
    }
  } catch (error) {
    console.error('修改密码出错:', error);
  }
};

// 充值相关
const rechargeDialogVisible = ref(false);
const rechargeForm = ref({
  balance: 100
});

const rechargeRules = ref({
  amount: [
    { required: true, message: '请输入充值金额', trigger: 'blur' },
    { type: 'number', min: 1, max: 10000, message: '金额必须在1-10000元之间', trigger: 'blur' }
  ]
});

const rechargeFormRef = ref();

const showRechargeDialog = () => {
  rechargeDialogVisible.value = true;
  rechargeForm.value.amount = 100;
};

const handleRecharge = async () => {
  if (!rechargeFormRef.value) return;

  try {
    await rechargeFormRef.value.validate();
    const result = await rechargeApi(
        profileForm.value.userId,
        rechargeForm.value.balance
    );
    if (result.code === 1) {
      ElMessage.success('充值成功');
      await userStore.updateUserProfile();
      rechargeDialogVisible.value = false;
      await fetchUserProfile();
    } else {
      ElMessage.error(result.msg || '充值失败');
    }
  } catch (error) {
    console.error('充值出错:', error);
  }
};

// 获取用户信息
const fetchUserProfile = async () => {
  try {
    const result = await getUserProfileApi(userStore.id);
    if (result.code === 1) {
      profileForm.value = {
        ...result.data,
        id: result.data.id
      };
    } else {
      ElMessage.error(result.msg || '获取用户信息失败');
    }
  } catch (error) {
    console.error('获取用户信息出错:', error);
  }
};

onMounted(() => {
  fetchUserProfile();
});
</script>

<style scoped>
.user-profile-container {
  padding: 20px;
}

.profile-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}

.action-buttons {
  text-align: center;
  margin-top: 20px;
}
</style>
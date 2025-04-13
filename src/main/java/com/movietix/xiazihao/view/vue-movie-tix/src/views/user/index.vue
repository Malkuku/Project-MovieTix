<script setup>
import {onMounted, ref} from 'vue';
import {
  addUserApi,
  deleteUsersApi,
  queryUserByIdApi,
  queryUsersApi,
  updateBalanceApi,
  updatePasswordApi,
  updateUserStatusApi
} from '@/api/user';
import {ElMessage, ElMessageBox} from 'element-plus';
import dayjs from 'dayjs';

// 钩子函数
onMounted(() => {
  search();
});

// 查询参数
const queryParams = ref({
  username: null,
  isAdmin: null,
  isBlocked: null,
  createdAtFrom: null,
  createdAtTo: null,
  page: 1,
  pageSize: 10
});

// 分页数据
const pagination = ref({
  total: 0,
  currentPage: 1,
  pageSize: 10
});

// 用户列表
const userList = ref([]);

// 查询用户
const search = async () => {
  // 处理空字符串参数
  const params = {
    ...queryParams.value,
    isAdmin: queryParams.value.isAdmin !== '' ? queryParams.value.isAdmin : undefined,
    isBlocked: queryParams.value.isBlocked !== '' ? queryParams.value.isBlocked : undefined,
  };

  const result = await queryUsersApi(params);
  if (result.code) {
    userList.value = result.data.list;
    pagination.value.total = result.data.total;
  }
};

// 分页变化
const handleCurrentChange = (val) => {
  queryParams.value.page = val;
  search();
};

// 每页条数变化
const handleSizeChange = (val) => {
  queryParams.value.pageSize = val;
  search();
};

// Dialog对话框
const dialogFormVisible = ref(false);
const dialogTitle = ref('');
const currentUser = ref({
  username: '',
  passwordHash: '',
  balance: 0
});

// 表单引用
const userFormRef = ref();

// 表单校验规则
const rules = ref({
  username: [
    { required: true, message: '用户名是必填项', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应在3-20位', trigger: 'blur' }
  ],
  passwordHash: [
    { required: true, message: '密码是必填项', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20位', trigger: 'blur' }
  ],
  balance: [
    { required: true, message: '余额是必填项', trigger: 'blur' },
    { type: 'number', min: 0, message: '余额不能小于0', trigger: 'blur' }
  ]
});

// 新增用户
const addUser = () => {
  dialogTitle.value = '新增用户';
  currentUser.value = {
    username: '',
    passwordHash: '',
    balance: 0
  };
  dialogFormVisible.value = true;

  if (userFormRef.value) {
    userFormRef.value.resetFields();
  }
};

// 编辑用户
const editUser = async (id) => {
  dialogTitle.value = '编辑用户';
  const result = await queryUserByIdApi(id);
  if (result.code) {
    currentUser.value = result.data;
    dialogFormVisible.value = true;
  }

  if (userFormRef.value) {
    userFormRef.value.resetFields();
  }
};

// 修改密码对话框
const passwordDialogVisible = ref(false);
const passwordForm = ref({
  id: '',
  passwordHash: ''
});

// 打开修改密码对话框
const openPasswordDialog = (user) => {
  passwordForm.value = {
    id: user.id,
    passwordHash: ''
  };
  passwordDialogVisible.value = true;
};

// 修改余额对话框
const balanceDialogVisible = ref(false);
const balanceForm = ref({
  id: '',
  balance: 0
});

// 打开修改余额对话框
const openBalanceDialog = (user) => {
  balanceForm.value = {
    id: user.id,
    balance: user.balance
  };
  balanceDialogVisible.value = true;
};

// 保存用户
const saveUser = async () => {
  if (!userFormRef.value) return;

  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      let result;
      if (currentUser.value.id) {
        // 更新用户信息
        result = await addUserApi(currentUser.value);
      } else {
        // 新增用户
        result = await addUserApi(currentUser.value);
      }

      if (result.code) {
        ElMessage.success('操作成功');
        dialogFormVisible.value = false;
        search();
      } else {
        ElMessage.error(result.msg);
      }
    }
  });
};

// 修改密码
const updatePassword = async () => {
  const result = await updatePasswordApi(passwordForm.value);
  if (result.code) {
    ElMessage.success('密码修改成功');
    passwordDialogVisible.value = false;
  } else {
    ElMessage.error(result.msg);
  }
};

// 修改余额
const updateBalance = async () => {
  const result = await updateBalanceApi(balanceForm.value);
  if (result.code) {
    ElMessage.success('余额修改成功');
    balanceDialogVisible.value = false;
    search();
  } else {
    ElMessage.error(result.msg);
  }
};

// 批量删除用户
const batchDelete = async () => {
  const selectedIds = selection.value.map(item => item.id).join(',');
  if (!selectedIds) {
    ElMessage.warning('请选择要删除的用户');
    return;
  }

  ElMessageBox.confirm('确定要删除选中的用户吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteUsersApi(selectedIds);
    if (result.code) {
      ElMessage.success('删除成功');
      search();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};

// 批量修改状态
const batchUpdateStatus = (status) => {
  const selectedIds = selection.value.map(item => item.id).join(',');
  if (!selectedIds) {
    ElMessage.warning('请选择要操作的用户');
    return;
  }

  const action = status === 1 ? '拉黑' : '解封';
  ElMessageBox.confirm(`确定要${action}选中的用户吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await updateUserStatusApi(selectedIds, status);
    if (result.code) {
      ElMessage.success(`${action}成功`);
      search();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    ElMessage.info(`已取消${action}操作`);
  });
};

// 表格多选
const selection = ref([]);
const handleSelectionChange = (val) => {
  selection.value = val;
};

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss');
};

// 重置查询
const resetQuery = () => {
  queryParams.value = {
    username: null,
    isAdmin: null,
    isBlocked: null,
    createdAtFrom: null,
    createdAtTo: null,
    page: 1,
    pageSize: 10
  };
  search();
};
</script>

<template>
  <h1>用户管理</h1>

  <!-- 查询表单 -->
  <div class="container">
    <el-card>
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>

        <el-form-item label="用户类型">
          <el-select v-model="queryParams.isAdmin" placeholder="请选择" clearable>
            <el-option label="普通用户" :value="0" />
            <el-option label="管理员" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item label="用户状态">
          <el-select v-model="queryParams.isBlocked" placeholder="请选择" clearable>
            <el-option label="正常" :value="0" />
            <el-option label="已拉黑" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item label="创建时间">
          <el-date-picker
              v-model="queryParams.createdAtFrom"
              type="datetime"
              placeholder="开始时间"
              value-format="YYYY-MM-DDTHH:mm:ss"
          />
          <span style="margin: 0 10px">至</span>
          <el-date-picker
              v-model="queryParams.createdAtTo"
              type="datetime"
              placeholder="结束时间"
              value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>

  <!-- 操作按钮 -->
  <div class="container">
    <el-button type="primary" @click="addUser">新增用户</el-button>
    <el-button type="danger" @click="batchDelete" :disabled="selection.length === 0">批量删除</el-button>
    <el-button type="warning" @click="batchUpdateStatus(1)" :disabled="selection.length === 0">批量拉黑</el-button>
    <el-button type="success" @click="batchUpdateStatus(0)" :disabled="selection.length === 0">批量解封</el-button>
  </div>

  <!-- 用户表格 -->
  <div class="container">
    <el-table
        :data="userList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="80" align="center" />

      <el-table-column prop="username" label="用户名" width="180" align="center" />
      <el-table-column label="用户类型" width="120" align="center">
        <template #default="{row}">
          <el-tag :type="row.isAdmin ? 'danger' : ''">
            {{ row.isAdmin ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="用户状态" width="120" align="center">
        <template #default="{row}">
          <el-tag :type="row.isBlocked ? 'danger' : 'success'">
            {{ row.isBlocked ? '已拉黑' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="balance" label="余额" width="120" align="center">
        <template #default="{row}">
          {{ row.balance.toFixed(2) }}
        </template>
      </el-table-column>

      <el-table-column prop="createdAt" label="创建时间" width="200" align="center">
        <template #default="{row}">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>

      <el-table-column prop="updatedAt" label="更新时间" width="200" align="center">
        <template #default="{row}">
          {{ formatTime(row.updatedAt) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="280">
        <template #default="{row}">
          <el-button type="primary" size="small" @click="editUser(row.id)">编辑</el-button>
          <el-button type="warning" size="small" @click="openPasswordDialog(row)">改密</el-button>
          <el-button type="info" size="small" @click="openBalanceDialog(row)">调额</el-button>
          <el-button
              type="danger"
              size="small"
              @click="batchDelete([row.id])"
              :disabled="row.isAdmin"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>

  <!-- 新增/编辑用户对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="dialogTitle" width="500px">
    <el-form :model="currentUser" :rules="rules" ref="userFormRef">
      <el-form-item label="用户名" prop="username" label-width="80px">
        <el-input v-model="currentUser.username" :disabled="!!currentUser.id" />
      </el-form-item>

      <el-form-item label="密码" prop="passwordHash" label-width="80px" v-if="!currentUser.id">
        <el-input v-model="currentUser.passwordHash" type="password" show-password />
      </el-form-item>

      <el-form-item label="余额" prop="balance" label-width="80px">
        <el-input-number v-model="currentUser.balance" :precision="2" :step="0.1" :min="0" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 修改密码对话框 -->
  <el-dialog v-model="passwordDialogVisible" title="修改密码" width="500px">
    <el-form :model="passwordForm">
      <el-form-item label="新密码" label-width="80px">
        <el-input
            v-model="passwordForm.passwordHash"
            type="password"
            show-password
            placeholder="请输入新密码"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 修改余额对话框 -->
  <el-dialog v-model="balanceDialogVisible" title="修改余额" width="500px">
    <el-form :model="balanceForm">
      <el-form-item label="新余额" label-width="80px">
        <el-input-number
            v-model="balanceForm.balance"
            :precision="2"
            :step="0.1"
            :min="0"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="balanceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateBalance">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 15px 0;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
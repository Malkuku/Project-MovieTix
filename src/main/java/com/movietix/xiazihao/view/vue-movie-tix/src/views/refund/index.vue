<script setup>
import { ref, onMounted } from 'vue';
import { queryRefundsApi, createRefundApi, processRefundApi } from '@/api/refund';
import { ElMessage, ElMessageBox } from 'element-plus';

// 状态枚举
const statusOptions = [
  { label: '待处理', value: 0 },
  { label: '已同意', value: 1 },
  { label: '已拒绝', value: 2 }
];

// 表格数据
const tableData = ref([]);
const total = ref(0);
const loading = ref(false);

// 查询参数
const queryParams = ref({
  page: 1,
  pageSize: 10,
  orderId: undefined,
  userId: undefined,
  status: undefined
});

// 分页查询
const search = async () => {
  try {
    loading.value = true;
    const result = await queryRefundsApi(queryParams.value);
    if (result.code) {
      tableData.value = result.data.list;
      total.value = result.data.total;
    }
  } finally {
    loading.value = false;
  }
};

// 重置查询
const resetQuery = () => {
  queryParams.value = {
    page: 1,
    pageSize: 10,
    orderId: undefined,
    userId: undefined,
    status: undefined
  };
  search();
};

// 处理分页变化
const handleSizeChange = (val) => {
  queryParams.value.pageSize = val;
  search();
};

const handleCurrentChange = (val) => {
  queryParams.value.page = val;
  search();
};

// 对话框相关
const dialogVisible = ref(false);
const dialogTitle = ref('');
const formRef = ref();
const form = ref({
  orderId: undefined,
  userId: undefined,
  reason: '',
  refundAmount: undefined
});

// 表单规则
const rules = ref({
  orderId: [{ required: true, message: '订单ID不能为空', trigger: 'blur' }],
  userId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
  reason: [
    { required: true, message: '退票原因不能为空', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在5到200个字符', trigger: 'blur' }
  ],
  refundAmount: [
    { required: true, message: '退款金额不能为空', trigger: 'blur' },
    { type: 'number', min: 0, message: '退款金额必须大于0', trigger: 'blur' }
  ]
});

// 打开创建对话框
const handleCreate = () => {
  dialogTitle.value = '创建退票申请';
  form.value = {
    orderId: undefined,
    userId: undefined,
    reason: '',
    refundAmount: undefined
  };
  dialogVisible.value = true;
};

// 提交表单
const submitForm = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      const result = await createRefundApi(form.value);
      if (result.code) {
        ElMessage.success('创建成功');
        dialogVisible.value = false;
        search();
      } else {
        ElMessage.error(result.msg || '创建失败');
      }
    }
  });
};

// 处理退票申请
const handleProcess = (status) => {
  const selectedIds = tableData.value
      .filter(item => item.status === 0 && selection.value.includes(item.id))
      .map(item => item.id);

  if (selectedIds.length === 0) {
    ElMessage.warning('请选择待处理的退票申请');
    return;
  }

  ElMessageBox.confirm(
      `确定要${status === 1 ? '同意' : '拒绝'}选中的${selectedIds.length}条退票申请吗?`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(async () => {
    const result = await processRefundApi({
      ids: selectedIds.join(','),
      adminId: 1, // 实际项目中应该从登录信息中获取
      status: status
    });

    if (result.code) {
      ElMessage.success('处理成功');
      search();
    } else {
      ElMessage.error(result.msg || '处理失败');
    }
  }).catch(() => {
    ElMessage.info('已取消操作');
  });
};

// 表格多选
const selection = ref([]);
const handleSelectionChange = (val) => {
  selection.value = val.map(item => item.id);
};

// 初始化加载
onMounted(() => {
  search();
});
</script>

<template>
  <div class="app-container">
    <el-card>
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="订单ID">
          <el-input v-model="queryParams.orderId" placeholder="订单ID" clearable />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="queryParams.userId" placeholder="用户ID" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="状态" clearable>
            <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div style="margin-bottom: 20px;">
        <el-button type="primary" @click="handleCreate">创建退票申请</el-button>
        <el-button type="success" @click="handleProcess(1)">批量同意</el-button>
        <el-button type="danger" @click="handleProcess(2)">批量拒绝</el-button>
      </div>

      <!-- 表格 -->
      <el-table
          v-loading="loading"
          :data="tableData"
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderId" label="订单ID" width="120" />
        <el-table-column prop="userId" label="用户ID" width="120" />
        <el-table-column prop="reason" label="退票原因" />
        <el-table-column prop="refundAmount" label="退款金额" width="120">
          <template #default="{row}">
            {{ row.refundAmount ? `¥${row.refundAmount.toFixed(2)}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{row}">
            <el-tag :type="row.status === 0 ? 'warning' : row.status === 1 ? 'success' : 'danger'">
              {{ statusOptions.find(item => item.value === row.status)?.label || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column prop="updatedAt" label="更新时间" width="180" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="queryParams.page"
            v-model:page-size="queryParams.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 创建对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model.number="form.orderId" placeholder="请输入订单ID" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model.number="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="退票原因" prop="reason">
          <el-input
              v-model="form.reason"
              type="textarea"
              :rows="3"
              placeholder="请输入退票原因"
          />
        </el-form-item>
        <el-form-item label="退款金额" prop="refundAmount">
          <el-input-number
              v-model.number="form.refundAmount"
              :precision="2"
              :min="0"
              placeholder="请输入退款金额"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
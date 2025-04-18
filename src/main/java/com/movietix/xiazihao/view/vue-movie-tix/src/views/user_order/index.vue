<script setup>
import { ref, onMounted } from 'vue';
import {createRefundApi, getOrdersApi , queryRefundsByUserIdApi,cancelOrderApi} from '@/api/user_work';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '@/stores/user';
import {formatDateTime} from "@/utils/date";

const userStore = useUserStore();

// 加载状态
const loading = ref({
  orders: false,
  refunds: false,
  submit: false
});

// 钩子函数
onMounted(() => {
  if (userStore.isAuthenticated()) {
    fetchMyOrders();
    fetchMyRefunds();
  }
});

// 查询我的订单列表
const orderList = ref([]);
const searchParams = ref({
  movieTitle: '',
  hallName: '',
  status: 1, // 默认查询已支付的订单
  dateRange: [] // 新增时间范围选择
});

// 日期快捷选项
const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setDate(start.getDate() - 7);
      return [start, end];
    }
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setMonth(start.getMonth() - 1);
      return [start, end];
    }
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setMonth(start.getMonth() - 3);
      return [start, end];
    }
  }
];

const fetchMyOrders = async () => {
  try {
    loading.value.orders = true;

    // 处理日期范围参数
    const params = {
      id: userStore.id,
      movieTitle: searchParams.value.movieTitle,
      hallName: searchParams.value.hallName,
      status: searchParams.value.status,
      startTime: null,
      endTime: null
    };

    // 如果有选择日期范围，则添加startTime参数
    if (searchParams.value.dateRange && searchParams.value.dateRange.length === 2) {
      const startDate = new Date(searchParams.value.dateRange[0]);
      const endDate = new Date(searchParams.value.dateRange[1]);
      params.startTime = formatDateTime(startDate);
      params.endTime = formatDateTime(endDate);
    }

    const result = await getOrdersApi(params);
    if (result.code) {
      orderList.value = result.data;
    }
  } catch (error) {
    ElMessage.error('获取订单列表失败');
  } finally {
    loading.value.orders = false;
  }
};

// 查询我的退票申请列表
const refundList = ref([]);
const fetchMyRefunds = async () => {
  try {
    loading.value.refunds = true;
    const result = await queryRefundsByUserIdApi(userStore.id);
    if (result.code === 1) {
      refundList.value = result.data;

    }
  } catch (error) {
    ElMessage.error('获取退票列表失败');
  } finally {
    loading.value.refunds = false;
  }
};

// 退票对话框
const dialogVisible = ref(false);
const formTitle = ref('申请退票');
const refundForm = ref({
  orderId: null,
  userId: userStore.id,
  reason: '',
  refundAmount: 0
});
const refundFormRef = ref();

// 打开退票对话框
const openRefundDialog = (order) => {
  if (!userStore.isAuthenticated()) {
    ElMessage.warning('请先登录');
    return;
  }
  formTitle.value = `申请退票 - 订单号: ${order.orderNo}`;
  refundForm.value = {
    orderId: order.orderId,
    userId: userStore.id,
    reason: '',
    refundAmount: order.totalAmount
  };
  console.log('Refund Form:', refundForm.value); // 添加调试信息，检查 refundForm.value 对象
  dialogVisible.value = true;

  if (refundFormRef.value) {
    refundFormRef.value.resetFields();
  }
};

// 提交退票申请
const submitRefund = async () => {
  if (!refundFormRef.value) return;

  try {
    loading.value.submit = true;
    await refundFormRef.value.validate();
    const result = await createRefundApi(refundForm.value);
    if (result.code) {
      ElMessage.success('退票申请已提交');
      dialogVisible.value = false;
      await fetchMyOrders();
      await fetchMyRefunds();
    } else {
      ElMessage.error(result.msg || '提交失败');
    }
  } catch (error) {
    if (error instanceof Error) {
      ElMessage.error('表单验证不通过');
    }
  } finally {
    loading.value.submit = false;
  }
};

// 表单验证规则
const rules = ref({
  reason: [
    { required: true, message: '请填写退票原因', trigger: 'blur' },
    { min: 5, max: 100, message: '退票原因长度应在5-100个字符之间', trigger: 'blur' }
  ],
  refundAmount: [
    { required: true, message: '请填写退款金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '退款金额必须大于0', trigger: 'blur' }
  ]
});

// 格式化状态
const formatStatus = (status) => {
  const statusMap = {
    0: '待处理',
    1: '已退款',
    2: '已拒绝'
  };
  return statusMap[status] || '未知状态';
};

// 格式化时间
const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleString();
};

// 格式化订单状态
const formatOrderStatus = (status) => {
  const statusMap = {
    0: '待支付',
    1: '已支付',
    2: '已取消',
    3: '已完成',
    4: '已退款'
  };
  return statusMap[status] || '未知状态';
};

// 取消订单
const cancelOrder = async (orderId) => {
  try {
    await ElMessageBox.confirm('确定要取消此订单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
    await cancelOrderApi(orderId);
    ElMessage.success('订单已取消');
    await fetchMyOrders();
  } catch (error) {
    ElMessage.info('已取消操作');
  }
};

// 判断订单是否在退票申请中且为待处理状态
const isRefundPending = (orderId) => {
  return refundList.value.some(refund => refund.orderId === orderId && refund.status === 0);
};

// 重置搜索条件
const resetSearch = () => {
  searchParams.value = {
    movieTitle: '',
    hallName: '',
    status: 1,
    dateRange: []
  };
  fetchMyOrders();
};
</script>

<template>
  <div class="my-orders-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的电影订单</span>
          <div class="search-box">
            <el-input
                v-model="searchParams.movieTitle"
                placeholder="电影名称"
                clearable
                style="width: 150px"
                @clear="fetchMyOrders"
                @keyup.enter="fetchMyOrders"
            />
            <el-input
                v-model="searchParams.hallName"
                placeholder="影厅名称"
                clearable
                style="width: 150px"
                @clear="fetchMyOrders"
                @keyup.enter="fetchMyOrders"
            />
            <el-date-picker
                v-model="searchParams.dateRange"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :shortcuts="shortcuts"
                value-format="YYYY-MM-DD"
                style="width: 280px"
                @change="fetchMyOrders"
            />
            <el-select
                v-model="searchParams.status"
                placeholder="订单状态"
                style="width: 120px"
                @change="fetchMyOrders"
            >
              <el-option label="全部" :value="null" />
              <el-option label="待支付" :value="0" />
              <el-option label="已支付" :value="1" />
              <el-option label="已取消" :value="2" />
              <el-option label="已完成" :value="3" />
              <el-option label="已退款" :value="4" />
            </el-select>
            <el-button type="primary" @click="fetchMyOrders">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </div>
        </div>
      </template>

      <el-table
          :data="orderList"
          border
          style="width: 100%"
          v-loading="loading.orders"
          empty-text="暂无订单数据"
      >
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="movieTitle" label="电影名称" />
        <el-table-column prop="hallName" label="影厅" width="120" />
        <el-table-column prop="startTime" label="放映时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column label="座位" width="180">
          <template #default="{ row }">
            <el-tag
                v-for="seat in row.seats"
                :key="seat.id"
                size="small"
                style="margin-right: 5px; margin-bottom: 5px"
            >
              {{ seat.seatNo }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="{
              0: 'warning',
              1: 'success',
              2: 'info',
              3: '',
              4: 'danger'
            }[row.status]">
              {{ formatOrderStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
                v-if="row.status === 0"
                type="danger"
                size="small"
                @click="cancelOrder(row.orderId)"
            >
              取消订单
            </el-button>
            <el-button
                v-if="row.status === 1"
                type="danger"
                size="small"
                :disabled="!userStore.isAuthenticated() || isRefundPending(row.orderId)"
                @click="openRefundDialog(row)"
            >
              {{ isRefundPending(row.orderId) ? '正在处理' : '申请退票' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>我的退票申请</span>
          <el-button
              type="primary"
              size="small"
              :loading="loading.refunds"
              @click="fetchMyRefunds"
          >
            刷新
          </el-button>
        </div>
      </template>

      <el-table
          :data="refundList"
          border
          style="width: 100%"
          v-loading="loading.refunds"
          empty-text="暂无退票申请"
      >
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="reason" label="退票原因" />
        <el-table-column prop="refundAmount" label="退款金额" width="120">
          <template #default="{ row }">
            ¥{{ row.refundAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="{
              0: 'warning',
              1: 'success',
              2: 'danger'
            }[row.status]">
              {{ formatStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="申请时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.updatedAt) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 退票申请对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="formTitle"
        width="50%"
        :close-on-click-modal="false"
    >
      <el-form
          ref="refundFormRef"
          :model="refundForm"
          :rules="rules"
          label-width="120px"
      >
        <el-form-item label="订单号">
          <el-input :value="formTitle.split(':')[1]?.trim()" disabled />
        </el-form-item>
        <el-form-item label="退票原因" prop="reason">
          <el-input
              v-model="refundForm.reason"
              type="textarea"
              :rows="3"
              placeholder="请输入退票原因"
              show-word-limit
              maxlength="100"
          />
        </el-form-item>
        <el-form-item label="退款金额">
          <el-input
              :value="refundForm.refundAmount ? refundForm.refundAmount + '元' : ''"
              disabled
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button
              type="primary"
              @click="submitRefund"
              :loading="loading.submit"
          >
            提交申请
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.my-orders-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-box {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
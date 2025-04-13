<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="searchForm" :inline="true" class="search-form">
      <el-form-item label="订单编号">
        <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
      </el-form-item>
      <el-form-item label="用户ID">
        <el-input v-model.number="searchForm.userId" placeholder="请输入用户ID" clearable />
      </el-form-item>
      <el-form-item label="场次ID">
        <el-input v-model.number="searchForm.screeningId" placeholder="请输入场次ID" clearable />
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="待支付" :value="0" />
          <el-option label="已支付" :value="1" />
          <el-option label="已取消" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已退款" :value="4" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 订单表格 -->
    <el-table :data="orderList" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" width="180" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="screeningId" label="场次ID" width="100" />
      <el-table-column prop="totalAmount" label="总金额" width="120">
        <template #default="{row}">
          {{ row.totalAmount }}元
        </template>
      </el-table-column>
      <el-table-column prop="seatCount" label="座位数" width="100" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{row}">
          <el-tag :type="getStatusTagType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="contactPhone" label="联系电话" width="150" />
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="{row}">
          {{ formatDateTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="viewDetail(row.id)">详情</el-button>
          <el-button
              size="small"
              type="danger"
              @click="cancelOrder(row.id)"
              :disabled="row.status !== 0"
          >
            取消
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />

    <!-- 订单详情对话框 -->
    <el-dialog
        v-model="detailDialogVisible"
        :title="`订单详情 - ${currentOrder.orderNo || ''}`"
        width="80%"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ currentOrder.userId }}</el-descriptions-item>
        <el-descriptions-item label="场次ID">{{ currentOrder.screeningId }}</el-descriptions-item>
        <el-descriptions-item label="总金额">{{ currentOrder.totalAmount }}元</el-descriptions-item>
        <el-descriptions-item label="座位数">{{ currentOrder.seatCount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentOrder.status)">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(currentOrder.createdAt) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDateTime(currentOrder.updatedAt) }}</el-descriptions-item>
      </el-descriptions>

      <h3 style="margin-top: 20px;">座位信息</h3>
      <el-table :data="currentOrder.seats" border style="width: 100%; margin-top: 10px;">
        <el-table-column prop="seatNo" label="座位号" width="120" />
        <el-table-column prop="row" label="行号" width="100" />
        <el-table-column prop="col" label="列号" width="100" />
      </el-table>

      <h3 style="margin-top: 20px;">支付记录</h3>
      <el-table :data="paymentList" border style="width: 100%; margin-top: 10px;">
        <el-table-column prop="transactionId" label="交易号" width="200" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{row}">
            {{ row.amount }}元
          </template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="支付方式" width="120">
          <template #default="{row}">
            {{ getPaymentMethodText(row.paymentMethod) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{row}">
            <el-tag :type="getPaymentStatusTagType(row.status)">
              {{ getPaymentStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间" width="180">
          <template #default="{row}">
            {{ row.payTime ? formatDateTime(row.payTime) : '-' }}
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {cancelOrderApi, queryOrderByIdApi, queryOrdersApi, queryPaymentsApi} from '@/api/order';
import {ElMessage, ElMessageBox} from 'element-plus';
import {formatDateTime} from '@/utils/date';

// 钩子函数
onMounted(() => {
  search();
});

// 搜索表单
const searchForm = ref({
  orderNo: '',
  userId: null,
  screeningId: null,
  status: null,
  minAmount: null,
  maxAmount: null,
  startDate: '',
  endDate: ''
});

// 分页
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});

// 订单列表
const orderList = ref([]);

// 搜索方法
const search = async () => {
  const params = {
    ...searchForm.value,
    page: pagination.value.current,
    pageSize: pagination.value.size
  };

  // 过滤掉空值参数
  Object.keys(params).forEach(key => {
    if (params[key] === '' || params[key] === null || params[key] === undefined) {
      delete params[key];
    }
  });

  const result = await queryOrdersApi(params);
  if (result.code) {
    orderList.value = result.data.list;
    pagination.value.total = result.data.total;
  }
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    orderNo: '',
    userId: null,
    screeningId: null,
    status: null,
    minAmount: null,
    maxAmount: null,
    startDate: '',
    endDate: ''
  };
  pagination.value.current = 1;
  search();
};

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.value.size = val;
  search();
};

// 当前页变化
const handleCurrentChange = (val) => {
  pagination.value.current = val;
  search();
};

// 处理搜索
const handleSearch = () => {
  pagination.value.current = 1;
  search();
};

// 订单状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待支付',
    1: '已支付',
    2: '已取消',
    3: '已完成',
    4: '已退款'
  };
  return statusMap[status] || '未知状态';
};

// 订单状态标签类型
const getStatusTagType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'success',
    2: 'info',
    3: '',
    4: 'danger'
  };
  return typeMap[status] || '';
};

// 取消订单
const cancelOrder = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    const result = await cancelOrderApi(id);
    if (result.code) {
      ElMessage.success('订单已取消');
      search();
    } else {
      ElMessage.error(result.msg || '取消订单失败');
    }
  } catch (error) {
    ElMessage.info('已取消操作');
  }
};

// 订单详情对话框
const detailDialogVisible = ref(false);
const currentOrder = ref({
  id: null,
  orderNo: '',
  userId: null,
  screeningId: null,
  totalAmount: 0,
  seatCount: 0,
  status: 0,
  contactPhone: '',
  createdAt: '',
  updatedAt: '',
  seats: []
});

// 支付记录列表
const paymentList = ref([]);

// 查看详情
const viewDetail = async (id) => {
  const result = await queryOrderByIdApi(id);
  if (result.code) {
    currentOrder.value = result.data;

    // 查询支付记录
    const paymentResult = await queryPaymentsApi({ orderId: id });
    if (paymentResult.code) {
      paymentList.value = paymentResult.data.list;
    }

    detailDialogVisible.value = true;
  }
};

// 支付方式文本
const getPaymentMethodText = (method) => {
  const methodMap = {
    alipay: '支付宝',
    wechat: '微信支付',
    unionpay: '银联支付'
  };
  return methodMap[method] || method;
};

// 支付状态文本
const getPaymentStatusText = (status) => {
  const statusMap = {
    0: '未支付',
    1: '成功',
    2: '失败'
  };
  return statusMap[status] || '未知状态';
};

// 支付状态标签类型
const getPaymentStatusTagType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  };
  return typeMap[status] || '';
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
</style>
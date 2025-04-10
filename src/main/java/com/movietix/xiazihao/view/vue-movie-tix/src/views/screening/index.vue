<script setup>
import { ref, onMounted, reactive } from 'vue';
import {
  queryScreeningsApi,
  deleteScreeningsApi,
  addScreeningApi,
  updateScreeningApi,
  updateStatusApi,
  queryScreeningByIdApi
} from '@/api/screening';
import { ElMessage, ElMessageBox } from 'element-plus';

// 钩子函数
onMounted(() => {
  search();
});

// 查询条件
const queryParams = reactive({
  movieId: null,
  hallId: null,
  movieTitle: null,
  hallName: null,
  startTimeFrom: null,
  startTimeTo: null,
  minPrice: null,
  maxPrice: null,
  minSeats: null,
  status: null,
  page: 1,
  pageSize: 10
});

// 分页信息
const pagination = ref({
  total: 0,
  currentPage: 1,
  pageSize: 10
});

// 场次列表
const screeningList = ref([]);

// 查询方法
const search = async () => {
  const params = {
    ...queryParams,
    page: pagination.value.currentPage,
    pageSize: pagination.value.pageSize
  };

  const result = await queryScreeningsApi(params);
  if(result.code){
    screeningList.value = result.data.list;
    pagination.value.total = result.data.total;
  }
}

// 重置查询条件
const resetQuery = () => {
  Object.keys(queryParams).forEach(key => {
    if (key === 'page' || key === 'pageSize') {
      queryParams[key] = 1;
    } else {
      queryParams[key] = null;
    }
  });
  pagination.value.currentPage = 1;
  search();
}

// Dialog对话框
const dialogFormVisible = ref(false);
const formTitle = ref('');
const screening = ref({
  movieId: null,
  hallId: null,
  startTime: '',
  price: null,
  status: 1
});

// 表单引用
const screeningFormRef = ref();

// 新增场次
const addScreening = () => {
  dialogFormVisible.value = true;
  formTitle.value = '新增放映场次';
  screening.value = {
    movieId: null,
    hallId: null,
    startTime: '',
    price: null,
    status: 1
  };

  // 重置表单校验
  if (screeningFormRef.value){
    screeningFormRef.value.resetFields();
  }
}

// 保存场次
const save = async () => {
  if(!screeningFormRef.value) return;

  screeningFormRef.value.validate(async (valid) => {
    if(valid){
      let result;
      if(screening.value.id){ // 修改
        result = await updateScreeningApi(screening.value);
      } else { // 新增
        result = await addScreeningApi(screening.value);
      }

      if(result.code){
        ElMessage.success('操作成功');
        dialogFormVisible.value = false;
        search();
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error('表单校验不通过');
    }
  });
}

// 表单校验规则
const rules = ref({
  movieId: [
    { required: true, message: '请选择电影', trigger: 'blur' }
  ],
  hallId: [
    { required: true, message: '请选择放映厅', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入票价', trigger: 'blur' },
    { type: 'number', min: 0, message: '票价必须大于0', trigger: 'blur' }
  ]
});

// 编辑场次
const edit = async (id) => {
  formTitle.value = '修改放映场次';
  // 重置表单校验
  if (screeningFormRef.value){
    screeningFormRef.value.resetFields();
  }

  const result = await queryScreeningByIdApi(id);
  if(result.code){
    dialogFormVisible.value = true;
    screening.value = result.data;
  }
}

// 批量删除
const batchDelete = () => {
  const selectedIds = selection.value.map(item => item.id).join(',');
  if(!selectedIds){
    ElMessage.warning('请选择要删除的场次');
    return;
  }

  ElMessageBox.confirm('确定要删除选中的场次吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteScreeningsApi(selectedIds);
    if(result.code){
      ElMessage.success('删除成功');
      search();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
}

// 多选处理
const selection = ref([]);
const handleSelectionChange = (val) => {
  selection.value = val;
}

// 批量更新状态
const batchUpdateStatus = (status) => {
  const selectedIds = selection.value.map(item => item.id).join(',');
  if(!selectedIds){
    ElMessage.warning('请选择要操作的场次');
    return;
  }

  const action = status === 1 ? '恢复' : '取消';
  ElMessageBox.confirm(`确定要${action}选中的场次吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await updateStatusApi(selectedIds, status);
    if(result.code){
      ElMessage.success('操作成功');
      search();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    ElMessage.info('已取消操作');
  });
}

// 分页处理
const handleSizeChange = (val) => {
  pagination.value.pageSize = val;
  search();
}

const handleCurrentChange = (val) => {
  pagination.value.currentPage = val;
  search();
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '';
  return new Date(time).toLocaleString();
}

// 状态文本
const statusText = (status) => {
  return status === 1 ? '正常' : '已取消';
}

// 状态样式
const statusStyle = (status) => {
  return status === 1 ? 'color: green' : 'color: red';
}
</script>

<template>
  <h1>放映场次管理</h1>

  <!-- 查询条件 -->
  <div class="container">
    <el-form :inline="true" :model="queryParams" class="demo-form-inline">
      <el-form-item label="电影名称">
        <el-input v-model="queryParams.movieTitle" placeholder="电影名称" clearable />
      </el-form-item>
      <el-form-item label="放映厅">
        <el-input v-model="queryParams.hallName" placeholder="放映厅名称" clearable />
      </el-form-item>
      <el-form-item label="开始时间从">
        <el-date-picker
            v-model="queryParams.startTimeFrom"
            type="datetime"
            placeholder="选择开始时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="到">
        <el-date-picker
            v-model="queryParams.startTimeTo"
            type="datetime"
            placeholder="选择结束时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="正常" :value="1" />
          <el-option label="已取消" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

  <!-- 操作按钮 -->
  <div class="container">
    <el-button type="primary" @click="addScreening"> + 新增场次</el-button>
    <el-button type="danger" @click="batchDelete" :disabled="!selection.length">批量删除</el-button>
    <el-button type="warning" @click="batchUpdateStatus(0)" :disabled="!selection.length">批量取消</el-button>
    <el-button type="success" @click="batchUpdateStatus(1)" :disabled="!selection.length">批量恢复</el-button>
  </div>

  <!-- 表格 -->
  <div class="container">
    <el-table
        :data="screeningList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="80" align="center" />
      <el-table-column prop="movieTitle" label="电影名称" width="200" align="center" />
      <el-table-column prop="hallName" label="放映厅" width="150" align="center" />
      <el-table-column label="放映时间" width="300" align="center">
        <template #default="{row}">
          <div>开始: {{ formatTime(row.startTime) }}</div>
          <div>结束: {{ formatTime(row.endTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="票价(元)" width="120" align="center" />
      <el-table-column label="座位情况" width="150" align="center">
        <template #default="{row}">
          <div>总座位: {{ row.totalSeats }}</div>
          <div>剩余: {{ row.remainingSeats }}</div>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{row}">
          <span :style="statusStyle(row.status)">{{ statusText(row.status) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="最后更新时间" width="180" align="center">
        <template #default="{row}">
          {{ formatTime(row.updatedAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="batchDelete([scope.row.id].join(','))">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 分页 -->
  <div class="container">
    <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>

  <!-- Dialog对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="screening" :rules="rules" ref="screeningFormRef">
      <el-form-item label="电影ID" label-width="80px" prop="movieId">
        <el-input v-model.number="screening.movieId" />
      </el-form-item>
      <el-form-item label="放映厅ID" label-width="80px" prop="hallId">
        <el-input v-model.number="screening.hallId" />
      </el-form-item>
      <el-form-item label="开始时间" label-width="80px" prop="startTime">
        <el-date-picker
            v-model="screening.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="票价(元)" label-width="80px" prop="price">
        <el-input v-model.number="screening.price" />
      </el-form-item>
      <el-form-item label="状态" label-width="80px" v-if="screening.id">
        <el-select v-model="screening.status" placeholder="状态">
          <el-option label="正常" :value="1" />
          <el-option label="已取消" :value="0" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 15px 0px;
}
</style>
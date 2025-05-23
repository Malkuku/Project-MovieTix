<script setup>
import {onMounted, reactive, ref} from 'vue';
import {
  addScreeningApi,
  deleteScreeningsApi,
  queryScreeningByIdApi,
  queryScreeningsApi,
  updateScreeningApi,
  updateStatusApi
} from '@/api/screening';
import {ElMessage, ElMessageBox} from 'element-plus';
import {queryHallsApi} from "@/api/hall";
import {queryMoviesApi} from "@/api/movie";



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
        await search();
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
    { required: true, message: '请选择电影', trigger: 'change' }
  ],
  hallId: [
    { required: true, message: '请选择放映厅', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入票价', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === null || value === undefined || value === '') {
          return callback(new Error('请输入票价'))
        }
        if (isNaN(value)) {
          return callback(new Error('请输入有效数字'))
        }
        if (value <= 0) {
          return callback(new Error('票价必须大于0'))
        }
        // 验证小数点后最多两位
        if (String(value).split('.')[1]?.length > 2) {
          return callback(new Error('最多保留两位小数'))
        }
        callback()
      },
      trigger: 'blur'
    }
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
      await search();
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
      await search();
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

// 电影和放映厅下拉选项
const movieOptions = ref([])
const hallOptions = ref([])

// 加载电影选项
const loadMovieOptions = async (query = '') => {
  const result = await queryMoviesApi({
    title: query,
    status: 1, // 只查询上映中的电影
    pageSize: 50 // 适当限制数量
  })
  if (result.code) {
    movieOptions.value = result.data.list.map(item => ({
      value: item.id,
      label: `${item.title} (${item.releaseDate})`
    }))
  }
}

// 加载放映厅选项
const loadHallOptions = async (query = '') => {
  const result = await queryHallsApi({
    name: query,
    status: 1, // 只查询启用的放映厅
    pageSize: 50 // 适当限制数量
  })
  if (result.code) {
    hallOptions.value = result.data.list.map(item => ({
      value: item.id,
      label: `${item.name} (${item.capacity}座)`
    }))
  }
}

const handlePriceInput = (e) => {
  // 允许: 数字、小数点、退格、删除、Tab、左右 箭头
  const allowedKeys = ['Backspace', 'Delete', 'Tab', 'ArrowLeft', 'ArrowRight']
  const isNumber = /[0-9]/.test(e.key)
  const isDecimal = e.key === '.' && !e.target.value.includes('.')

  if (!isNumber && !isDecimal && !allowedKeys.includes(e.key)) {
    e.preventDefault()
  }
}

// 钩子函数
onMounted(() => {
  loadMovieOptions()
  loadHallOptions()
  search();
});

</script>

<template>
  <h1>放映场次管理</h1>

  <!-- 查询条件 -->
  <div class="container">
    <el-form :inline="true" :model="queryParams" class="demo-form-inline">
      <el-form-item label="电影名称">
        <el-select
            v-model="queryParams.movieId"
            filterable
            remote
            clearable
            placeholder="请选择电影"
            :remote-method="loadMovieOptions"
            :loading="false"
        >
          <el-option
              v-for="item in movieOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="放映厅">
        <el-select
            v-model="queryParams.hallId"
            filterable
            remote
            clearable
            placeholder="请选择放映厅"
            :remote-method="loadHallOptions"
            :loading="false"
        >
          <el-option
              v-for="item in hallOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
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
      <el-form-item label="电影" label-width="80px" prop="movieId">
        <el-select
            v-model="screening.movieId"
            filterable
            remote
            clearable
            placeholder="请选择电影"
            :remote-method="loadMovieOptions"
            :loading="false"
            style="width: 100%"
        >
          <el-option
              v-for="item in movieOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="放映厅" label-width="80px" prop="hallId">
        <el-select
            v-model="screening.hallId"
            filterable
            remote
            clearable
            placeholder="请选择放映厅"
            :remote-method="loadHallOptions"
            :loading="false"
            style="width: 100%"
        >
          <el-option
              v-for="item in hallOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
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
      <el-input
          v-model.number="screening.price"
          type="number"
          step="0.01"
          min="0"
          @keydown="handlePriceInput"
      />
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
  margin: 15px 0;
}
</style>
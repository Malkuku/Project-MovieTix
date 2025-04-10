<script setup>
import { ref, onMounted, reactive } from 'vue';
import { queryHallsApi, deleteHallsApi, addHallApi, updateHallApi, queryHallByIdApi } from '@/api/hall';
import { ElMessage, ElMessageBox } from 'element-plus';

// 钩子函数
onMounted(() => {
  search();
});

// 查询参数
const queryParams = reactive({
  name: '',
  minCapacity: null,
  maxCapacity: null,
  minRows: null,
  minCols: null,
  facility: '',
  status: null,
  page: 1,
  pageSize: 10
});

// 分页查询
const hallList = ref([]);
const total = ref(0);
const search = async () => {
  const result = await queryHallsApi(queryParams);
  if(result.code){
    hallList.value = result.data.list;
    total.value = result.data.total;
  }
};

// 重置查询条件
const resetQuery = () => {
  Object.assign(queryParams, {
    name: null,
    minCapacity: null,
    maxCapacity: null,
    minRows: null,
    minCols: null,
    facility: null,
    status: null,
    page: 1,
    pageSize: 10
  });
  search();
};

// Dialog对话框
const dialogFormVisible = ref(false);
const formTitle = ref('');
const hall = ref({
  name: null,
  capacity: null,
  rows: null,
  cols: null,
  facilities: '{}',
  status: 1
});
const hallFormRef = ref();

// 新增放映厅
const addHall = () => {
  dialogFormVisible.value = true;
  formTitle.value = '新增放映厅';
  hall.value = {
    name: null,
    capacity: null,
    rows: null,
    cols: null,
    facilities: '{}',
    status: 1
  };

  if (hallFormRef.value) {
    hallFormRef.value.resetFields();
  }
};

// 保存放映厅
const save = async () => {
  if(!hallFormRef.value) return;
  hallFormRef.value.validate(async (valid) => {
    if(valid){
      let result;
      if(hall.value.id){ // 修改
        result = await updateHallApi(hall.value);
      }else{ // 新增
        result = await addHallApi(hall.value);
      }

      if(result.code){
        ElMessage.success('操作成功');
        dialogFormVisible.value = false;
        search();
      }else{
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error('表单校验不通过');
    }
  });
};

// 表单校验规则
const rules = ref({
  name: [
    { required: true, message: '放映厅名称是必填项', trigger: 'blur' },
    { min: 2, max: 20, message: '放映厅名称长度应在2-20位之间', trigger: 'blur' }
  ],
  capacity: [
    { required: true, message: '座位容量是必填项', trigger: 'blur' },
    { type: 'number', min: 10, message: '座位容量最小为10', trigger: 'blur' }
  ],
  rows: [
    { required: true, message: '行数是必填项', trigger: 'blur' },
    { type: 'number', min: 5, message: '行数最小为5', trigger: 'blur' }
  ],
  cols: [
    { required: true, message: '列数是必填项', trigger: 'blur' },
    { type: 'number', min: 5, message: '列数最小为5', trigger: 'blur' }
  ]
});

// 编辑
const edit = async (id) => {
  formTitle.value = '修改放映厅';
  if (hallFormRef.value) {
    hallFormRef.value.resetFields();
  }

  const result = await queryHallByIdApi(id);
  if(result.code){
    dialogFormVisible.value = true;
    hall.value = result.data;
  }
};

// 删除
const delByIds = async (ids) => {
  ElMessageBox.confirm('您确认删除选中的放映厅吗?', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteHallsApi(ids);
    if(result.code){
      ElMessage.success('删除成功');
      search();
    }else{
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    ElMessage.info('您已取消删除');
  });
};

// 处理分页变化
const handleSizeChange = (val) => {
  queryParams.pageSize = val;
  search();
};

const handleCurrentChange = (val) => {
  queryParams.page = val;
  search();
};


// 解析设施JSON为更友好的格式
const parseFacilities = (facilities) => {
  try {
    const obj = JSON.parse(facilities);
    const items = [];

    // 屏幕类型
    if (obj.screen_type) {
      items.push(`屏幕: ${obj.screen_type}`);
    }

    // 音响系统
    if (obj.sound_system) {
      items.push(`音响: ${obj.sound_system}`);
    }

    // 其他设施
    if (obj.recliner_seats) {
      items.push('可调节座椅');
    }
    if (obj.food_service) {
      items.push('餐饮服务');
    }
    if (obj['3D']) {
      items.push('3D');
    }
    if (obj['4K']) {
      items.push('4K');
    }
    if (obj.DolbyAtmos) {
      items.push('杜比全景声');
    }

    return items.length > 0 ? items.join(' | ') : '标准配置';
  } catch {
    return '标准配置';
  }
};

// 格式化JSON显示
const formatJson = (jsonStr) => {
  try {
    const obj = JSON.parse(jsonStr);
    return JSON.stringify(obj, null, 2);
  } catch {
    return jsonStr;
  }
};
</script>

<template>
  <h1>放映厅管理</h1>

  <!-- 查询表单 -->
  <div class="container">
    <el-form :inline="true" :model="queryParams" class="demo-form-inline">
      <el-form-item label="放映厅名称">
        <el-input v-model="queryParams.name" placeholder="放映厅名称" clearable />
      </el-form-item>
      <el-form-item label="座位容量">
        <el-input-number v-model="queryParams.minCapacity" placeholder="最小容量" :min="10" :controls="false" />
        <span style="margin: 0 5px">-</span>
        <el-input-number v-model="queryParams.maxCapacity" placeholder="最大容量" :min="10" :controls="false" />
      </el-form-item>
      <el-form-item label="行数/列数">
        <el-input-number v-model="queryParams.minRows" placeholder="最小行数" :min="5" :controls="false" />
        <span style="margin: 0 5px">/</span>
        <el-input-number v-model="queryParams.minCols" placeholder="最小列数" :min="5" :controls="false" />
      </el-form-item>
      <el-form-item label="设施">
        <el-input v-model="queryParams.facility" placeholder="设施关键词" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="关停" :value="0" />
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
    <el-button type="primary" @click="addHall"> + 新增放映厅</el-button>
  </div>

  <!-- 表格 -->
  <div class="container">
    <el-table :data="hallList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="放映厅名称" width="150" align="center" />
      <el-table-column prop="capacity" label="座位容量" width="100" align="center" />
      <el-table-column label="行列数" width="100" align="center">
        <template #default="{row}">
          {{ row.rows }}×{{ row.cols }}
        </template>
      </el-table-column>
      <el-table-column label="设施" width="250" align="center">
        <template #default="{row}">
          <el-popover
              placement="top-start"
              width="300"
              trigger="hover"
              v-if="row.facilities && row.facilities !== '{}'"
          >
            <template #reference>
              <el-tag type="info" size="small" style="margin-right: 5px" v-for="item in parseFacilities(row.facilities).split(' | ')" :key="item">
                {{ item }}
              </el-tag>
            </template>
            <div>
              <h4>详细设施配置</h4>
              <pre style="font-size: 12px">{{ formatJson(row.facilities) }}</pre>
            </div>
          </el-popover>
          <span v-else>标准配置</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '关停' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="最后更新时间" width="180" align="center" />
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="delByIds(scope.row.id.toString())">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>

  <!-- Dialog对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="hall" :rules="rules" ref="hallFormRef" label-width="100px">
      <el-form-item label="放映厅名称" prop="name">
        <el-input v-model="hall.name" />
      </el-form-item>
      <el-form-item label="座位容量" prop="capacity">
        <el-input-number v-model="hall.capacity" :min="10" :controls="false" style="width: 100%" />
      </el-form-item>
      <el-form-item label="行数" prop="rows">
        <el-input-number v-model="hall.rows" :min="5" :controls="false" style="width: 100%" />
      </el-form-item>
      <el-form-item label="列数" prop="cols">
        <el-input-number v-model="hall.cols" :min="5" :controls="false" style="width: 100%" />
      </el-form-item>
      <el-form-item label="设施">
        <el-input v-model="hall.facilities" placeholder="请输入JSON格式的设施配置" />
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="hall.status" :active-value="1" :inactive-value="0" />
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
.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
<template>
  <div class="log-management-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <h2>系统日志管理</h2>
          <div>
            <el-button
                type="danger"
                :disabled="selectedLogs.length === 0"
                @click=""
            >
              批量删除
            </el-button>
            <el-button type="primary" @click="search">
              刷新数据
            </el-button>
          </div>
        </div>
      </template>

      <!-- 日志表格 -->
      <el-table
          v-loading="loading"
          :data="logList"
          style="width: 100%"
          row-key="id"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" sortable />
        <el-table-column prop="userId" label="用户ID" width="100" sortable />
        <el-table-column prop="action" label="操作类型" width="120" />
        <el-table-column prop="ipAddress" label="IP地址" width="150" />
        <el-table-column prop="userAgent" label="用户代理" width="200" show-overflow-tooltip />
        <el-table-column prop="details" label="详细信息" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" width="180" sortable />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                type="danger"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>


<script setup>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'
import {queryAllApi} from "@/api/log";

// 日志列表数据
const logList = ref([])
// 选中的日志
const selectedLogs = ref([])
// 加载状态
const loading = ref(false)

// 初始化获取日志数据
onMounted(async () => {
  await nextTick()
  await queryAllApi()
})

// 获取日志列表
const search = async () => {
  const result = await queryAllApi();
  if(result.code){
    logList.value = result.data;
  }
}

//批量删除日志


// 处理选中日志变化
const handleSelectionChange = (selection) => {
  selectedLogs.value = selection.map(item => item.id)
}

//钩子函数
onMounted(() => {
  search();
})

</script>

<style scoped>
.log-management-container {
  padding: 20px;
}

.box-card {
  margin: 0 auto;
  max-width: 1400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-table {
  margin-top: 20px;
}
</style>
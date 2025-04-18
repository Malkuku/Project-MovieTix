<script setup>
import {onMounted, reactive, ref} from 'vue';
import {addMovieApi, deleteMoviesApi, queryByIdApi, queryMoviesApi, updateMovieApi} from '@/api/movie';
import {ElMessage, ElMessageBox} from 'element-plus';
import {Plus} from "@element-plus/icons-vue";
import {deleteFileApi, uploadFileApi} from "@/api/upload";

// 分页查询参数
const queryParams = reactive({
  title: null,
  releaseDate: null,
  minDuration: null,
  maxDuration: null,
  genre: null,
  minRating: null,
  maxRating: null,
  status: null,
  page: 1,
  pageSize: 10
});

// 分页数据
const pagination = reactive({
  total: 0,
  currentPage: 1,
  pageSize: 10
});

// 电影列表
const movieList = ref([]);

// 查询电影
const search = async () => {
  const params = {
    ...queryParams,
    page: pagination.currentPage,
    pageSize: pagination.pageSize
  };

  const result = await queryMoviesApi(params);
  if(result.code){
    movieList.value = result.data.list;
    pagination.total = result.data.total;
  }
}

// 重置查询条件
const resetSearch = () => {
  Object.keys(queryParams).forEach(key => {
    if(key !== 'page' && key !== 'pageSize') {
      queryParams[key] = null;
    }
  });
  search();
}

// 分页变化
const handleCurrentChange = (val) => {
  pagination.currentPage = val;
  search();
}

// 每页条数变化
const handleSizeChange = (val) => {
  pagination.pageSize = val;
  search();
}

// Dialog对话框
const dialogFormVisible = ref(false);
const formTitle = ref('');
const movie = ref({
  title: null,
  posterUrl: null,
  releaseDate: null,
  duration: null,
  genre: null,
  rating: null,
  status: 1
});

// 表单引用
const movieFormRef = ref();

// 新增电影
const addMovie = () => {
  dialogFormVisible.value = true;
  formTitle.value = '新增电影';
  movie.value = {
    id: null,
    title: '',
    posterUrl: '',
    releaseDate: '',
    duration: '',
    genre: '',
    rating: '',
    status: 1
  };

  // 重置表单校验
  if (movieFormRef.value){
    movieFormRef.value.resetFields();
  }
}

// 保存电影
const save = async () => {
  if (!movieFormRef.value) return

  await movieFormRef.value.validate(async (valid) => {
    if (valid) {
      // 确保有海报URL
      if (!movie.value.posterUrl) {
        ElMessage.warning('请上传电影海报')
        return
      }

      let result
      try {
        if (movie.value.id) { // 修改
          result = await updateMovieApi(movie.value)
        } else { // 新增
          result = await addMovieApi(movie.value)
        }

        if (result.code === 1) {
          ElMessage.success('操作成功')
          dialogFormVisible.value = false
          await search()
        } else {
          ElMessage.error(result.msg)
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('操作失败，请稍后重试')
      }
    } else {
      ElMessage.error('表单校验不通过')
    }
  })
}

// 编辑电影
const edit = async (id) => {
  formTitle.value = '修改电影';
  if (movieFormRef.value){
    movieFormRef.value.resetFields();
  }
  const result = await queryByIdApi(id);
  if(result.code){
    dialogFormVisible.value = true;
    movie.value = result.data;
  }
}

// 删除电影
const delMovies = (ids) => {
  ElMessageBox.confirm('您确认删除选中的电影吗?','提示',
      { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    const result = await deleteMoviesApi(ids);
    if(result.code){
      ElMessage.success('删除成功');
      await search();
    }else{
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    ElMessage.info('您已取消删除');
  });
}

// 单个删除
const delById = (id) => {
  delMovies(id);
}

// 批量删除
const batchDelete = () => {
  const selectedIds = movieList.value
      .filter(item => selected.value.includes(item.id))
      .map(item => item.id);

  if(selectedIds.length === 0){
    ElMessage.warning('请至少选择一条记录');
    return;
  }

  delMovies(selectedIds.join(','));
}

// 表格多选
const selected = ref([]);
const handleSelectionChange = (val) => {
  selected.value = val.map(item => item.id);
}

// 表单校验规则
const rules = ref({
  title: [
    { required: true, message: '电影名称是必填项', trigger: 'blur' },
    { min: 2, max: 50, message: '电影名称长度应在2-50个字符', trigger: 'blur' }
  ],
  releaseDate: [
    { required: true, message: '上映日期是必填项', trigger: 'blur' }
  ],
  duration: [
    { required: true, message: '时长是必填项', trigger: 'blur' },
    { type: 'number', min: 1, message: '时长必须大于0', trigger: 'blur' }
  ],
  genre: [
    { required: true, message: '电影类型是必填项', trigger: 'blur' }
  ],
  rating: [
    { type: 'number', min: 0, max: 10, message: '评分必须在0-10之间', trigger: 'blur' }
  ]
});

// 处理海报上传
const handlePosterChange = async (file) => {
  // 验证文件类型
  const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg']
  if (!allowedTypes.includes(file.raw.type)) {
    ElMessage.error('只能上传JPG/PNG格式的图片')
    return false
  }

  // 验证文件大小 (1MB)
  const maxSize = 1024 * 1024
  if (file.raw.size > maxSize) {
    ElMessage.error('图片大小不能超过1MB')
    return false
  }

  try {
    // 如果有旧图片，先删除
    if (movie.value.posterUrl) {
      await deleteOldPoster(movie.value.posterUrl)
    }

    const result = await uploadFileApi(file.raw)
    if (result.code === 1) {
      movie.value.posterUrl = result.data
      ElMessage.success('上传成功')
    } else {
      ElMessage.error(result.msg || '上传失败')
    }
  } catch (error) {
    console.error('上传出错:', error)
    ElMessage.error('上传失败，请稍后重试')
  }
}

// 删除旧海报
const deleteOldPoster = async (url) => {
  try {
    await deleteFileApi(url)
  } catch (error) {
    console.error('删除旧图片失败:', error)
    // 这里不提示用户，因为不影响主要功能
  }
}

// 初始化加载
onMounted(() => {
  search();
});
</script>

<template>
  <h1>电影管理</h1>

  <!-- 查询表单 -->
  <div class="container">
    <el-form :inline="true" :model="queryParams" class="search-form">
      <el-form-item label="电影名称">
        <el-input v-model="queryParams.title" placeholder="请输入电影名称" clearable />
      </el-form-item>
      <el-form-item label="上映日期">
        <el-date-picker
            v-model="queryParams.releaseDate"
            type="Date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            clearable
        />
      </el-form-item>
      <el-form-item label="电影类型">
        <el-input v-model="queryParams.genre" placeholder="请输入电影类型" clearable />
      </el-form-item>
      <el-form-item label="时长范围">
        <el-input-number v-model="queryParams.minDuration" :min="0" placeholder="最小" />
        <span style="margin: 0 5px">-</span>
        <el-input-number v-model="queryParams.maxDuration" :min="0" placeholder="最大" />
      </el-form-item>
      <el-form-item label="评分范围">
        <el-input-number v-model="queryParams.minRating" :min="0" :max="10" :precision="1" placeholder="最低" />
        <span style="margin: 0 5px">-</span>
        <el-input-number v-model="queryParams.maxRating" :min="0" :max="10" :precision="1" placeholder="最高" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="上映" :value="1" />
          <el-option label="下架" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

  <!-- 操作按钮 -->
  <div class="container">
    <el-button type="primary" @click="addMovie"> + 新增电影</el-button>
    <el-button type="danger" @click="batchDelete" :disabled="selected.length === 0">批量删除</el-button>
  </div>

  <!-- 电影表格 -->
  <div class="container">
    <el-table
        :data="movieList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="80" align="center" />
      <el-table-column prop="title" label="电影名称" width="200" align="center" />
      <el-table-column label="海报" width="120" align="center">
        <template #default="{row}">
          <el-image
              v-if="row.posterUrl"
              :src="row.posterUrl"
              style="width: 80px; height: 100px; object-fit: cover"
              :preview-src-list="[row.posterUrl]"
              preview-teleported
              hide-on-click-modal
          />
          <span v-else>无海报</span>
        </template>
      </el-table-column>
      <el-table-column prop="releaseDate" label="上映日期" width="120" align="center" />
      <el-table-column prop="duration" label="时长(分钟)" width="100" align="center" />
      <el-table-column prop="genre" label="类型" width="120" align="center" />
      <el-table-column prop="rating" label="评分" width="100" align="center"/>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '上映' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="更新时间" width="180" align="center" />
      <el-table-column label="操作" align="center" width="180">
        <template #default="{row}">
          <el-button type="primary" size="small" @click="edit(row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="delById(row.id)">删除</el-button>
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

  <!-- 新增/编辑对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="600px">
    <el-form :model="movie" :rules="rules" ref="movieFormRef" label-width="100px">
      <el-form-item label="电影名称" prop="title">
        <el-input v-model="movie.title" placeholder="请输入电影名称" />
      </el-form-item>
      <!-- 修改海报上传部分 -->
      <el-form-item label="海报" prop="posterUrl">
        <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handlePosterChange"
            :accept="'image/jpeg,image/png,image/gif'"
        >
          <img v-if="movie.posterUrl" :src="movie.posterUrl" class="avatar"  alt=""/>
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
            <div class="el-upload__text">点击上传海报</div>
          </el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="上映日期" prop="releaseDate">
        <el-date-picker
            v-model="movie.releaseDate"
            type="Date"
            placeholder="选择上映日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="时长(分钟)" prop="duration">
        <el-input-number v-model="movie.duration" :min="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="电影类型" prop="genre">
        <el-input v-model="movie.genre" placeholder="请输入电影类型" />
      </el-form-item>
      <el-form-item label="评分" prop="rating">
        <el-input-number v-model="movie.rating" :min="0" :max="10" :precision="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="movie.status">
          <el-radio :label="1">上映</el-radio>
          <el-radio :label="0">下架</el-radio>
        </el-radio-group>
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
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: contain;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.container {
  margin: 15px 0;
}
.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.pagination {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
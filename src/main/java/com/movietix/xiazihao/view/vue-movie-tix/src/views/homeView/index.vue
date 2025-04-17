<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, ChatDotRound, Platform, VideoCamera } from '@element-plus/icons-vue';
import {
  getMoviesApi,
  getOrdersApi,
  cancelOrderApi,
  getScreeningsApi,
  payOrderApi,
  getSeatsApi, createOrderApi
} from '@/api/user_work';
import { formatDateTime } from '@/utils/date';

const router = useRouter();
const userStore = useUserStore();

// 导航栏状态
const activeIndex = ref('1');
const searchQuery = ref('');

// 筛选和排序
const activeFilter = ref('all');
const sortBy = ref('popular');

// 电影数据
const featuredMovies = ref([]);
const allMovies = ref([]);
const comingSoonMovies = ref([]);
const loading = ref(false);
const orders = ref([]);
const screeningInfo = ref({});
const searchValue = ref('');

//时间选择器
const selectedStartTime = ref(null); // 用户选择的开始时间
const selectedEndTime = ref(null); // 用户选择的结束时间
//电影选择
const currentMovieTitle = ref(''); // 当前电影的标题
const currentMovieId = ref(null); // 当前电影的 ID

// 分页参数
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 0
});

// 弹窗控制
const bookingDialogVisible = ref(false);
const currentStep = ref(1); // 1:选择场次, 2:选择座位, 3:确认支付
const phone = ref('');

// 场次数据
const screenings = ref([]);
const selectedScreening = ref(null);

// 座位数据
const seats = ref({ rows: 0, cols: 0, price: 0, seats: [] });
const selectedSeats = ref([]);

// 订单数据
const orderId = ref(null);
const orderInfo = ref(null);

// 计算属性
const totalPrice = computed(() => {
  return selectedSeats.value.length * (selectedScreening.value?.price || 0);
});

//生成座位图
const generateSeatMap = (rows, cols, price) => {
  const seatMap = [];
  for (let row = 1; row <= rows; row++) {
    for (let col = 1; col <= cols; col++) {
      const seatLetter = String.fromCharCode(64 + row); // 将行号转换为字母
      seatMap.push({
        seatRow: row,
        seatCol: col,
        seatNo: `${seatLetter}${col.toString().padStart(2, '0')}`, // 例如 A03
        price: price, // 假设所有座位价格相同
        status: 'available' // 初始状态为可选
      });
    }
  }
  return seatMap;
};

const seatRows = computed(() => {
  if (!seats.value.seats || seats.value.seats.length === 0) return [];
  const rows = new Set();
  seats.value.seats.forEach(seat => rows.add(seat.seatRow));
  return Array.from(rows).sort((a, b) => a - b);
});

const getSeatByRowCol = (row, col) => {
  return seats.value.seats.find(seat => seat.seatRow === row && seat.seatCol === col);
};

const getSeatStatus = (row, col) => {
  const seat = getSeatByRowCol(row, col);
  return seat ? seat.status : 'available';
};

// 检查一个座位是否已经被选中
const isSeatSelected = (seat) => {
  return selectedSeats.value.some(s => s.seatNo === seat.seatNo);
};

// 获取电影数据
const fetchMovies = async () => {
  try {
    loading.value = true;

    // 获取推荐电影（轮播图）
    const featuredResult = await getMoviesApi({
      page: 1,
      pageSize: 3,
      status: 1
    });

    if (featuredResult.code === 1) {
      featuredMovies.value = featuredResult.data.list.map(movie => ({
        ...movie,
        poster: movie.posterUrl,
        genre: movie.genre.split(','),
        price: Math.floor(Math.random() * 20) + 30 // 模拟价格
      }));
    }

    // 获取正在热映电影
    const nowShowingResult = await getMoviesApi({
      page: pagination.value.page,
      pageSize: pagination.value.pageSize,
      status: 1
    });

    if (nowShowingResult.code === 1) {
      allMovies.value = nowShowingResult.data.list.map(movie => ({
        ...movie,
        poster: movie.posterUrl,
        genre: movie.genre.split(','),
        price: Math.floor(Math.random() * 20) + 30 // 模拟价格
      }));
      pagination.value.total = nowShowingResult.data.total;
    }

    // 获取即将上映电影
    const comingSoonResult = await getMoviesApi({
      page: 1,
      pageSize: 4,
      status: 0
    });

    if (comingSoonResult.code === 1) {
      comingSoonMovies.value = comingSoonResult.data.list.map(movie => ({
        ...movie,
        poster: movie.posterUrl,
        genre: movie.genre.split(','),
        price: Math.floor(Math.random() * 20) + 30 // 模拟价格
      }));
    }
  } catch (error) {
    ElMessage.error('获取电影数据失败');
    console.error('获取电影数据失败:', error);
  } finally {
    loading.value = false;
  }
};

// 计算属性
const filteredMovies = computed(() => {
  let movies = [...allMovies.value];

  // 筛选
  if (activeFilter.value !== 'all') {
    movies = movies.filter(movie =>
        movie.genre.some(g => g.toLowerCase().includes(activeFilter.value))
    );
  }

  // 排序
  switch (sortBy.value) {
    case 'rating':
      return movies.sort((a, b) => b.rating - a.rating);
    case 'newest':
      return movies.sort((a, b) => new Date(b.releaseDate) - new Date(a.releaseDate));
    case 'price-low':
      return movies.sort((a, b) => a.price - b.price);
    case 'price-high':
      return movies.sort((a, b) => b.price - a.price);
    default:
      return movies.sort((a, b) => b.rating - a.rating); // 默认按热门排序
  }
});

const recommendedMovies = computed(() => {
  // 从热映电影中随机推荐3部
  return [...allMovies.value]
      .sort(() => 0.5 - Math.random())
      .slice(0, 3);
});

// 获取用户订单列表
const fetchUserOrders = async () => {
  try {
    const params = {
      id: userStore.id,
      status: 1 // 默认查询已支付订单
    };
    const response = await getOrdersApi(params);
    orders.value = response.data;
  } catch (error) {
    throw error;
  }
};

// 方法
const handleSelect = (index) => {
  activeIndex.value = index;
  // 这里可以添加导航逻辑
};

const handleSearch = async () => {
  if (searchQuery.value.trim()) {
    try {
      loading.value = true;
      const result = await getMoviesApi({
        title: searchQuery.value,
        page: 1,
        pageSize: 10
      });

      if (result.code === 1) {
        allMovies.value = result.data.list.map(movie => ({
          ...movie,
          poster: movie.posterUrl,
          genre: movie.genre.split(','),
          price: Math.floor(Math.random() * 20) + 30 // 模拟价格 //TODO 替换为实际价格
        }));
        pagination.value.total = result.data.total;
        ElMessage.success(`找到${result.data.total}部相关电影`);
      }
    } catch (error) {
      ElMessage.error('搜索失败');
      console.error('搜索失败:', error);
    } finally {
      loading.value = false;
    }
  }
};

// 打开弹窗
const openBookingDialog = async (movie) => {
  bookingDialogVisible.value = true;
  currentStep.value = 1;
  currentMovieTitle.value = movie.title;
  currentMovieId.value = movie.id;
  await fetchScreenings(movie.title, movie.id);
};

// 更新场次列表
const updateScreenings = () => {
  if (selectedStartTime.value || selectedEndTime.value) {
    // 确保开始时间早于结束时间
    if (selectedEndTime.value && selectedStartTime.value > selectedEndTime.value) {
      ElMessage.error('开始时间不能晚于结束时间');
      return;
    }
    // 调用 fetchScreenings 方法更新场次列表
    fetchScreenings(currentMovieTitle.value, currentMovieId.value);
  }
};

// 获取场次列表
const fetchScreenings = async (movieTitle, movieId) => {
  try {
    const params = {
      movieTitle: movieTitle, // 使用传入的 movieTitle
      movieId: movieId, // 使用传入的 movieId
      startTimeFrom: selectedStartTime.value ? selectedStartTime.value.toISOString().replace(/\.\d{3}Z$/, "") : new Date().toISOString().replace(/\.\d{3}Z$/, ""), // 使用用户选择的开始时间或默认当前时间
      startTimeTo: selectedEndTime.value ? selectedEndTime.value.toISOString().replace(/\.\d{3}Z$/, "") : new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().replace(/\.\d{3}Z$/, ""), // 使用用户选择的结束时间或默认三天后
      status: 1, // 只查询正常状态
      page: pagination.value.page,
      pageSize: pagination.value.pageSize
    };
    const response = await getScreeningsApi(params);
    screenings.value = response.data.list;
    pagination.value.total = response.data.total;
  } catch (error) {
    throw error;
  }
};

// 选择场次
const selectScreening = async (screening) => {
  selectedScreening.value = screening;
  await fetchSeats(screening.id);
  currentStep.value = 2;
};

//获取座位信息
const fetchSeats = async (screeningId) => {
  try {
    const response = await getSeatsApi(screeningId);
    if (response.code === 1) {
      const allSeats = generateSeatMap(response.data.rows, response.data.cols, response.data.price);
      const occupiedSeats = response.data.seats.map(seat => ({
        ...seat,
        status: 'occupied'
      }));

      seats.value = {
        rows: response.data.rows,
        cols: response.data.cols,
        price: response.data.price,
        seats: allSeats.map(seat => occupiedSeats.find(s => s.seatNo === seat.seatNo) || seat)
      };

      // 更新放映信息
      screeningInfo.value = {
        movieTitle: selectedScreening.value?.movieTitle || '',
        screeningTime: selectedScreening.value?.screeningTime || '',
        hallName: selectedScreening.value?.hallName || '',
        rows: response.data.rows,
        cols: response.data.cols
      };
    } else {
      ElMessage.error(response.msg || '获取座位信息失败');
    }
  } catch (error) {
    ElMessage.error('获取座位信息失败');
    console.error(error);
  }
};

// 支付倒计时
const paymentTimer = ref(null);
const paymentTimeLeft = ref(900); // 15分钟

const startPaymentTimer = () => {
  paymentTimer.value = setInterval(() => {
    paymentTimeLeft.value--;
    if (paymentTimeLeft.value <= 0) {
      clearInterval(paymentTimer.value);
      handlePaymentTimeout();
    }
  }, 1000);
};

// 支付超时处理
const handlePaymentTimeout = async () => {
  try {
    await cancelOrderApi(orderId.value);
    ElMessage.warning('订单支付超时，已自动取消');
    closeDialog();
  } catch (error) {
    console.error('取消订单失败:', error);
  }
};

// 创建订单
const createOrder = async () => {
  if (selectedSeats.value.length === 0) {
    ElMessage.warning('请至少选择一个座位');
    return;
  }

  try {
    // 准备请求参数（现在作为查询参数）
    const params = {
      screeningId: selectedScreening.value.id,
      userId: userStore.id
    };

    // 发起创建订单的请求
    const response = await createOrderApi(params);
    console.log('createOrderApi', response);
    if (response.code === 1) {
      orderId.value = response.data.orderId;
      ElMessage.success('订单创建成功');
      currentStep.value = 3; // 跳转到支付步骤
    } else {
      ElMessage.error(response.msg || '订单创建失败');
    }
  } catch (error) {
    ElMessage.error('订单创建失败');
    console.error(error);
  }
};

//支付订单
const payOrder = async () => {
  try {
    // 准备请求数据（现在作为请求体）
    const paymentData = {
      orderId: orderId.value,
      userId: userStore.id,
      contactPhone: phone.value,
      seats: selectedSeats.value.map(seat => ({
        seatRow: seat.seatRow,
        seatCol: seat.seatCol,
        seatNo: seat.seatNo,
        price: seat.price
      }))
    };

    const response = await payOrderApi(paymentData);
    if (response.code === 1) {
      ElMessage.success('支付成功');
      clearInterval(paymentTimer.value);
      closeDialog();
    } else {
      ElMessage.warning(response.msg || '支付失败');
      // TODO 跳转到充值页面
    }
  } catch (error) {
    ElMessage.error('支付失败');
    console.error(error);
  }
};

// 取消订单
const cancelOrder = async () => {
  try {
    await ElMessageBox.confirm('确定要取消订单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    await cancelOrderApi(orderId.value);
    ElMessage.success('订单已取消');
    clearInterval(paymentTimer.value);
    closeDialog();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败');
      console.error(error);
    }
  }
};

// 关闭弹窗
const closeDialog = () => {
  bookingDialogVisible.value = false;
  clearInterval(paymentTimer.value);
  // 重置状态
  currentStep.value = 1;
  selectedScreening.value = null;
  selectedSeats.value = [];
  orderId.value = null;
  orderInfo.value = null;
  paymentTimeLeft.value = 900;
};

// 格式化时间
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
};

// 筛选和排序处理
const handleFilterChange = () => { //TODO
  fetchMovies();
};

const handleSortChange = () => { //TODO
  fetchMovies();
};

// 路径跳转
const goToMovieDetail = (movieId) => {
  router.push(`/movie/${movieId}`);
};

const goToProfile = () => {
  router.push('/profile');
};

const goToOrders = () => {
  router.push('/user_order');
};

const goToLayout = () => {
  router.push('/layout');
};

const toggleLoginState = () => {
  if (userStore.isAuthenticated()) { // 修复这里
    userStore.clearUserInfo();
    ElMessage({
      message: '退出登录成功',
      type: 'success'
    });
  }
  router.push('/login');
};



// 方法
const toggleSeatSelection = (seat) => {
  if (seat.status !== 'available') return;

  const index = selectedSeats.value.findIndex(s => s.seatNo === seat.seatNo);
  if (index === -1) {
    selectedSeats.value.push(seat);
  } else {
    selectedSeats.value.splice(index, 1);
  }
};

// 生命周期钩子
onMounted(() => {
  fetchMovies(); // 确保加载电影数据
  if (userStore.isAuthenticated()) {
    fetchUserOrders();
  }
});
</script>

<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo">
        <h1>MovieTix</h1>
        <p>您的专属电影购票平台</p>
      </div>
      <div class="nav">
        <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
        >
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="2">正在热映</el-menu-item>
          <el-menu-item index="3">即将上映</el-menu-item>
          <el-menu-item index="4">排行榜</el-menu-item>
        </el-menu>
      </div>
      <div class="search">
        <el-input
            v-model="searchQuery"
            placeholder="搜索电影..."
            class="search-input"
            @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon='Search' @click="handleSearch"/>
          </template>
        </el-input>
      </div>
      <div class="user-actions">
        <el-dropdown>
          <span class="el-dropdown-link">
            <el-avatar :size="40" :src="userStore.avatar"/>
            <span class="username">{{ userStore.username }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
              <el-dropdown-item @click="goToOrders">我的订单</el-dropdown-item>
              <el-dropdown-item divided @click="toggleLoginState">
                {{ userStore.isAuthenticated() ? '退出登录' : '登录' }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-button @click="goToLayout">
      <el-icon>管理员入口</el-icon>
    </el-button>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 轮播图 -->
      <el-carousel :interval="5000" arrow="always" height="400px" class="carousel">
        <el-carousel-item v-for="movie in featuredMovies" :key="movie.id">
          <div
              class="carousel-item"
              :style="{ backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3)), url(${movie.poster})` }"
              @click="goToMovieDetail(movie.id)"
          >
            <div class="carousel-content">
              <h2>{{ movie.title }}</h2>
              <p>{{ movie.description }}</p>
              <el-button type="primary" round @click.stop="openBookingDialog(movie)">立即购票</el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <!-- 分类筛选 -->
      <div class="filter-section">
        <div class="filter-tabs">
          <el-tabs v-model="activeFilter" @tab-change="handleFilterChange">
            <el-tab-pane label="全部" name="all"></el-tab-pane>
            <el-tab-pane label="动作" name="动作"></el-tab-pane>
            <el-tab-pane label="喜剧" name="喜剧"></el-tab-pane> <!--TODO-->
            <el-tab-pane label="爱情" name="爱情"></el-tab-pane>
            <el-tab-pane label="科幻" name="科幻"></el-tab-pane>
            <el-tab-pane label="恐怖" name="恐怖"></el-tab-pane>
            <el-tab-pane label="动画" name="动画"></el-tab-pane>
          </el-tabs>
        </div>
        <div class="sort-options">
          <el-select v-model="sortBy" placeholder="排序方式" @change="handleSortChange">
            <el-option label="热门排序" value="popular"></el-option>
            <el-option label="评分最高" value="rating"></el-option>
            <el-option label="最新上映" value="newest"></el-option>
            <el-option label="价格最低" value="price-low"></el-option>
            <el-option label="价格最高" value="price-high"></el-option>
          </el-select>
        </div>
      </div>

      <!-- 电影列表 -->
      <div class="movie-list">
        <h2 class="section-title">正在热映</h2>
        <el-row :gutter="20">
          <el-col
              v-for="movie in filteredMovies"
              :key="movie.id"
              :xs="12"
              :sm="8"
              :md="6"
              :lg="4"
              :xl="4"
          >
            <el-card
                class="movie-card"
                :body-style="{ padding: '0px' }"
                shadow="hover"
                @click="goToMovieDetail(movie.id)"
            >
              <div class="movie-poster" :style="{ backgroundImage: `url(${movie.poster})` }">
                <div class="movie-rating">
                  <el-rate
                      v-model="movie.rating"
                      disabled
                      show-score
                      text-color="#ff9900"
                      score-template="{value}"
                  />
                </div>
              </div>
              <div class="movie-info">
                <h3 class="movie-title">{{ movie.title }}</h3>
                <p class="movie-genre">{{ movie.genre.join(' / ') }}</p>
                <div class="movie-footer">
                  <span class="price">¥{{ movie.price }}</span>
                  <el-button
                      type="primary"
                      size="small"
                      round
                      @click.stop="openBookingDialog(movie)"
                  >
                    购票
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 即将上映 -->
      <div class="coming-soon">
        <h2 class="section-title">即将上映</h2>
        <el-row :gutter="20">
          <el-col
              v-for="movie in comingSoonMovies"
              :key="movie.id"
              :xs="12"
              :sm="8"
              :md="6"
              :lg="4"
              :xl="4"
          >
            <el-card
                class="coming-soon-card"
                :body-style="{ padding: '0px' }"
                shadow="hover"
                @click="goToMovieDetail(movie.id)"
            >
              <div class="coming-soon-poster" :style="{ backgroundImage: `url(${movie.poster})` }">
                <div class="release-date">
                  {{ movie.releaseDate }} 上映
                </div>
              </div>
              <div class="coming-soon-info">
                <h3 class="movie-title">{{ movie.title }}</h3>
                <p class="movie-genre">{{ movie.genre.join(' / ') }}</p>
                <el-button
                    type="info"
                    size="small"
                    round
                    disabled
                >
                  预售未开始
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 底部推荐 -->
      <div class="recommendations">
        <h2 class="section-title">为您推荐</h2>
        <el-row :gutter="20">
          <el-col
              v-for="movie in recommendedMovies"
              :key="movie.id"
              :xs="12"
              :sm="8"
              :md="6"
              :lg="4"
              :xl="4"
          >
            <el-card
                class="recommendation-card"
                :body-style="{ padding: '0px' }"
                shadow="hover"
                @click="goToMovieDetail(movie.id)"
            >
              <div class="recommendation-poster" :style="{ backgroundImage: `url(${movie.poster})` }">
                <div class="recommendation-tag">
                  <el-tag type="warning" effect="dark">推荐</el-tag>
                </div>
              </div>
              <div class="recommendation-info">
                <h3 class="movie-title">{{ movie.title }}</h3>
                <p class="movie-genre">{{ movie.genre.join(' / ') }}</p>
                <div class="movie-footer">
                  <span class="price">¥{{ movie.price }}</span>
                  <el-button type="primary" size="small" round @click.stop="openBookingDialog(movie)">
                    购票
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 底部页脚 -->
    <el-footer class="footer">
      <div class="copyright">
        <p>© 2025 MovieTix ka-cat制作</p>
      </div>
    </el-footer>
  </div>

  <!-- 购票弹窗 -->
  <el-dialog
      v-model="bookingDialogVisible"
      title="电影购票"
      width="80%"
      :before-close="closeDialog"
      :close-on-click-modal="false"
  >
    <!-- 步骤指示器 -->
    <el-steps :active="currentStep" align-center>
      <el-step title="选择场次" />
      <el-step title="选择座位" />
      <el-step title="确认支付" />
    </el-steps>

    <!-- 第一步: 选择场次 -->
    <div v-if="currentStep === 1" class="step-content">
      <h3>选择放映场次</h3>

      <!-- 日期选择器 -->
      <div class="time-filter">
        <el-date-picker
            v-model="selectedStartTime"
            type="datetime"
            placeholder="选择开始时间"
            @change="updateScreenings"
        />
        <el-date-picker
            v-model="selectedEndTime"
            type="datetime"
            placeholder="选择结束时间"
            @change="updateScreenings"
        />
      </div>

      <div class="screening-list">
        <el-card
            v-for="screening in screenings"
            :key="screening.id"
            class="screening-card"
            :class="{ 'selected': selectedScreening?.id === screening.id }"
            @click="selectScreening(screening)"
        >
          <div class="screening-info">
            <div class="screening-time">
              <span class="time">{{ formatDateTime(screening.startTime) }}</span>
              <span class="end-time">{{ formatDateTime(screening.endTime) }}</span>
            </div>
            <div class="screening-hall">
              <el-icon><Platform /></el-icon>
              <span>{{ screening.hallName }}</span>
            </div>
            <div class="screening-price">
              <span>¥{{ screening.price }}</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 第二步: 选择座位 -->
    <div v-if="currentStep === 2" class="step-content">
      <h3>选择座位 - {{ screeningInfo.movieTitle }}</h3>
      <p class="screening-info">
        {{ formatDateTime(screeningInfo.screeningTime) }} | {{ screeningInfo.hallName }}
      </p>

      <div class="seat-selection">
        <div class="screen">银幕</div>
        <div class="seat-map">
          <div v-for="row in seatRows" :key="row" class="seat-row">
            <div class="row-letter">{{ String.fromCharCode(64 + row) }}</div> <!-- 显示行字母 -->
            <div
                v-for="col in Array.from({ length: screeningInfo.cols }, (_, c) => c + 1)"
                :key="`seat-${row}-${col}`"
                class="seat"
                :class="{
          'available': getSeatStatus(row, col) === 'available',
          'selected': isSeatSelected(getSeatByRowCol(row, col)),
          'occupied': getSeatStatus(row, col) === 'occupied',
          'vip': getSeatByRowCol(row, col)?.price > selectedScreening.price
        }"
                @click="toggleSeatSelection(getSeatByRowCol(row, col))"
            >
              {{ col }}
            </div>
          </div>
        </div>

        <div class="seat-legend">
          <div class="legend-item">
            <div class="legend-color available"></div>
            <span>可选</span>
          </div>
          <div class="legend-item">
            <div class="legend-color selected"></div>
            <span>已选</span>
          </div>
          <div class="legend-item">
            <div class="legend-color occupied"></div>
            <span>已售</span>
          </div>
          <div class="legend-item">
            <div class="legend-color vip"></div>
            <span>VIP座位</span>
          </div>
        </div>

        <div class="selected-seats-info">
          <h4>已选座位：</h4>
          <div v-if="selectedSeats.length > 0" class="selected-seats-list">
            <div v-for="seat in selectedSeats" :key="seat.seatNo" class="seat-item">
              <span>{{ seat.seatNo }}</span>
              <span class="seat-price">¥{{ seat.price }}</span>
            </div>
          </div>
          <p v-else class="no-seat">尚未选择座位</p>

          <div class="total-price">
            总计: <span>¥{{ totalPrice }}</span>
          </div>
        </div>

        <div class="action-buttons">
          <el-button @click="currentStep = 1">上一步</el-button>
          <el-button type="primary" @click="createOrder">下一步</el-button>
        </div>
      </div>
    </div>

    <!-- 第三步: 确认支付 -->
    <div v-if="currentStep === 3" class="step-content">
      <h3>确认订单信息</h3>
      <div class="order-summary">
        <div class="order-row">
          <span class="label">电影名称：</span>
          <span class="value">{{ selectedScreening.movieTitle }}</span>
        </div>
        <div class="order-row">
          <span class="label">放映时间：</span>
          <span class="value">{{ formatDateTime(selectedScreening.startTime) }}</span>
        </div>
        <div class="order-row">
          <span class="label">影厅：</span>
          <span class="value">{{ selectedScreening.hallName }}</span>
        </div>
        <div class="order-row">
          <span class="label">座位：</span>
          <span class="value">
          <el-tag
              v-for="seat in selectedSeats"
              :key="seat.seatNo"
              type="info"
              class="seat-tag"
          >
          {{ String.fromCharCode(64 + seat.seatRow) }}{{ seat.seatCol }}座 <!-- 使用字母表示行号 -->
          </el-tag>
          </span>
        </div>
        <div class="order-row">
          <span class="label">总价：</span>
          <span class="value price">¥{{ totalPrice }}</span>
        </div>
        <div class="order-row">
          <span class="label">手机号：</span>
          <span class="value">
            <el-input v-model="phone" placeholder="请输入手机号" style="width: 200px" />
          </span>
        </div>
      </div>

      <div class="payment-timer">
        <el-alert
            title="请在15分钟内完成支付，超时订单将自动取消"
            type="warning"
            :closable="false"
            show-icon
        >
          <template #default>
            剩余时间: {{ formatTime(paymentTimeLeft) }}
          </template>
        </el-alert>
      </div>

      <div class="action-buttons">
        <el-button @click="currentStep = 2">上一步</el-button>
        <el-button type="danger" @click="cancelOrder">取消订单</el-button>
        <el-button type="primary" @click="payOrder">立即支付</el-button> <!-- TODO 回到上一步/关闭弹窗时，如何处理已经创建的订单问题？ --->
      </div>
    </div>
  </el-dialog>
</template>


<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #1a1a2e;
  color: white;
  padding: 0 20px;
  height: 80px;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  color: #e94560;
}

.logo p {
  margin: 0;
  font-size: 12px;
  color: #a1a1a1;
}

.nav .el-menu {
  background-color: transparent;
  border-bottom: none;
}

.nav .el-menu-item {
  color: white;
  font-size: 16px;
}

.nav .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav .el-menu-item.is-active {
  color: #e94560;
  border-bottom: 2px solid #e94560;
}

.search {
  width: 300px;
}

.search-input {
  border-radius: 20px;
}

.user-actions {
  display: flex;
  align-items: center;
}

.username {
  margin-left: 10px;
  font-size: 14px;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #f8f8f8;
}

.carousel {
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
}

.carousel-item {
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: flex-end;
  cursor: pointer;
}

.carousel-content {
  padding: 40px;
  color: white;
  width: 100%;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
}

.carousel-content h2 {
  font-size: 36px;
  margin-bottom: 10px;
}

.carousel-content p {
  font-size: 16px;
  margin-bottom: 20px;
  max-width: 60%;
}

.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-tabs {
  flex: 1;
}

.sort-options {
  width: 150px;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.movie-list, .coming-soon, .recommendations {
  margin-bottom: 40px;
}

.movie-card, .coming-soon-card, .recommendation-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.movie-card:hover, .coming-soon-card:hover, .recommendation-card:hover {
  transform: translateY(-5px);
}

.movie-poster, .coming-soon-poster, .recommendation-poster {
  height: 250px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.movie-rating {
  position: absolute;
  bottom: 10px;
  left: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.7);
  padding: 5px;
  border-radius: 4px;
}

.release-date {
  position: absolute;
  bottom: 10px;
  left: 0;
  right: 0;
  text-align: center;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px;
}

.recommendation-tag {
  position: absolute;
  top: 10px;
  right: 10px;
}

.movie-info, .coming-soon-info, .recommendation-info {
  padding: 15px;
}

.movie-title {
  font-size: 16px;
  margin: 0 0 5px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.movie-genre {
  font-size: 12px;
  color: #888;
  margin: 0 0 10px 0;
}

.movie-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  color: #e94560;
  font-weight: bold;
}

.footer {
  background-color: #1a1a2e;
  color: white;
  padding: 30px 0 0 0;
}

.footer-content {
  display: flex;
  justify-content: space-around;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-section {
  flex: 1;
  padding: 0 20px;
}

.footer-section h3 {
  color: #e94560;
  margin-bottom: 15px;
}

.footer-section p {
  margin: 5px 0;
  font-size: 14px;
}

.social-icons {
  display: flex;
  gap: 15px;
}

.copyright {
  text-align: center;
  padding: 20px 0;
  border-top: 1px solid #333;
  margin-top: 20px;
  font-size: 12px;
  color: #888;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    height: auto;
    padding: 10px;
  }

  .logo, .nav, .search, .user-actions {
    width: 100%;
    margin-bottom: 10px;
  }

  .search {
    order: 3;
  }

  .carousel-content h2 {
    font-size: 24px;
  }

  .carousel-content p {
    max-width: 100%;
  }

  .filter-section {
    flex-direction: column;
    align-items: flex-start;
  }

  .sort-options {
    width: 100%;
    margin-top: 10px;
  }

  .footer-content {
    flex-direction: column;
  }

  .footer-section {
    margin-bottom: 20px;
  }
}

.step-content {
  padding: 20px;
}

.screening-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
  margin-top: 20px;
}

.screening-card {
  cursor: pointer;
  transition: all 0.3s;
}

.screening-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.screening-card.selected {
  border: 2px solid #409EFF;
}

.screening-info {
  padding: 15px;
}

.screening-time {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.screening-time .end-time {
  font-size: 14px;
  color: #999;
  margin-left: 5px;
}

.screening-hall {
  display: flex;
  align-items: center;
  color: #666;
  margin-bottom: 10px;
}

.screening-hall i {
  margin-right: 5px;
}

.screening-price {
  color: #E6A23C;
  font-weight: bold;
}

.seat-selection {
  margin-top: 20px;
}


.screen {
  width: 80%;
  height: 20px;
  background: linear-gradient(to bottom, #ccc, #fff);
  margin: 0 auto 30px;
  text-align: center;
  font-size: 14px;
  color: #333;
  border-radius: 0 0 50% 50%;
}

.seat-map {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin-bottom: 30px;
}

.seat-row {
  display: flex;
  gap: 8px;
}

.seat {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
  position: relative;
}

.seat.available {
  background-color: #67C23A;
  color: white;
}

.seat.available:hover {
  background-color: #85ce61;
}

.seat.selected {
  background-color: #409EFF;
  color: white;
}

.seat.occupied {
  background-color: #F56C6C;
  color: white;
  cursor: not-allowed;
}

.seat.vip {
  background-color: #E6A23C;
  color: white;
}

.seat-price {
  position: absolute;
  bottom: -18px;
  font-size: 10px;
  color: #E6A23C;
  width: 100%;
  text-align: center;
}

.selected-seats-info {
  margin: 20px 0;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.selected-seats-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 10px;
  margin: 10px 0;
}

.seat-item {
  display: flex;
  justify-content: space-between;
  padding: 5px;
  background-color: white;
  border-radius: 3px;
}

.seat-item .seat-price {
  color: #E6A23C;
  font-weight: bold;
}

.total-price {
  text-align: right;
  font-size: 16px;
  margin-top: 10px;
}

.total-price span {
  color: #E6A23C;
  font-weight: bold;
  font-size: 20px;
}

.no-seat {
  color: #999;
  text-align: center;
  margin: 10px 0;
}

.seat-legend {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.legend-color {
  width: 20px;
  height: 20px;
  border-radius: 3px;
}

.legend-color.available {
  background-color: #67C23A;
}

.legend-color.selected {
  background-color: #409EFF;
}

.legend-color.occupied {
  background-color: #F56C6C;
}

.legend-color.reserved {
  background-color: #E6A23C;
}

.selected-seats {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.seat-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.order-summary {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-bottom: 20px;
}

.order-row {
  display: flex;
  margin-bottom: 15px;
  line-height: 1.6;
}

.order-row .label {
  width: 100px;
  color: #666;
}

.order-row .value {
  flex: 1;
}

.order-row .price {
  color: #E6A23C;
  font-weight: bold;
  font-size: 18px;
}

.payment-timer {
  margin-bottom: 20px;
}
</style>
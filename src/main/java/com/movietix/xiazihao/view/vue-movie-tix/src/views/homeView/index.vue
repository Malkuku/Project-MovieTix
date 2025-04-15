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
            <el-button :icon="Search" @click="handleSearch"/>
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
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
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
              <el-button type="primary" round @click.stop="goToBooking(movie.id)">立即购票</el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <!-- 分类筛选 -->
      <div class="filter-section">
        <div class="filter-tabs">
          <el-tabs v-model="activeFilter" @tab-change="handleFilterChange">
            <el-tab-pane label="全部" name="all"></el-tab-pane>
            <el-tab-pane label="动作" name="action"></el-tab-pane>
            <el-tab-pane label="喜剧" name="comedy"></el-tab-pane>
            <el-tab-pane label="爱情" name="romance"></el-tab-pane>
            <el-tab-pane label="科幻" name="sci-fi"></el-tab-pane>
            <el-tab-pane label="恐怖" name="horror"></el-tab-pane>
            <el-tab-pane label="动画" name="animation"></el-tab-pane>
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
                      @click.stop="goToBooking(movie.id)"
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
                  <el-button
                      type="primary"
                      size="small"
                      round
                      @click.stop="goToBooking(movie.id)"
                  >
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
</template>

<script setup>
import {ref, computed, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import {useUserStore} from '@/stores/user';
import {ElMessage} from 'element-plus';
import {Search, ChatDotRound, Platform, VideoCamera} from '@element-plus/icons-vue';
import {getMoviesApi} from '@/api/user_work';

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

// 分页参数
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 0
});

// 获取电影数据
const fetchMovies = async () => {
  try {
    loading.value = true;

    // 获取推荐电影（轮播图）
    const featuredResult = await getMoviesApi({
      page: 1,
      pageSize: 3,
      status: 1,
      sortBy: 'rating',
      order: 'desc'
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
      status: 0,
      sortBy: 'releaseDate',
      order: 'asc'
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
          price: Math.floor(Math.random() * 20) + 30
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

const handleFilterChange = () => {
  fetchMovies();
};

const handleSortChange = () => {
  fetchMovies();
};


//路径跳转
const goToMovieDetail = (movieId) => {
  router.push(`/movie/${movieId}`);
};

const goToBooking = (movieId) => {
  router.push(`/booking/${movieId}`);
};

const goToProfile = () => {
  router.push('/profile');
};

const goToOrders = () => {
  router.push('/orders');
};

const goToLayout = () => {
  router.push('/layout');
};


const logout = () => {
  userStore.clearUserInfo();
  ElMessage.success('已退出登录');
  router.push('/login');
};

// 生命周期钩子
onMounted(() => {
  fetchMovies();
});
</script>

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
</style>
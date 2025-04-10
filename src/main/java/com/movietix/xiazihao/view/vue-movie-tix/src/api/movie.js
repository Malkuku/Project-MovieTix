import request from "@/utils/request";

// 分页查询电影数据
export const queryMoviesApi = (params) => request.get('/movies', { params });

// 批量删除电影
export const deleteMoviesApi = (ids) => request.delete('/movies', { params: { ids } });

// 新增电影
export const addMovieApi = (movie) => request.post('/movies', movie);

// 根据ID查询电影
export const queryByIdApi = (id) => request.get(`/movies/${id}`);

// 修改电影信息
export const updateMovieApi = (movie) => request.put('/movies', movie);
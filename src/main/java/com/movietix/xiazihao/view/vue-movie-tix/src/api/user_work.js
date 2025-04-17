import request from "@/utils/request";
import {sha256} from 'js-sha256';

// 用户登录
export const loginApi = (username, password) => {
    const passwordHash = sha256(password);
    return request.post('/works/login', { username, passwordHash });
}

// 用户注册
export const registerApi = (username, password) => {
    const passwordHash = sha256(password);
    return request.post('/works/register', { username, passwordHash });
}

// 用户充值
export const rechargeApi = (userId, balance) => {
    return request.post('/works/recharge', { id: userId, balance });
}

// 修改密码
export const updatePasswordApi = (userId, oldPassword, newPassword) => {
    const oldPasswordHash = sha256(oldPassword);
    const newPasswordHash = sha256(newPassword);
    return request.post('/works/users/password', {
        id: userId,
        oldPasswordHash,
        passwordHash: newPasswordHash
    });
}

// 修改用户信息
export const updateUserProfileApi = (profileData) => {
    return request.put('/works/user_profiles', profileData);
}

// 查询用户信息
export const getUserProfileApi = (userId) => {
    return request.get(`/works/user_profiles/${userId}`);
}

// 查询电影列表
export const getMoviesApi = (params) => {
    return request.get('/works/movies', { params });
}

// 查询电影详情 //TODO
export const getMovieDetailApi = (id) => {
    return request.get(`/works/movies_detail/${id}`);
}

// 查询场次信息
export const getScreeningsApi = (params) => {
    return request.get('/works/screenings', { params });
}

// 创建订单 - 修改为使用查询参数
export const createOrderApi = (params) => {
    return request.post('/works/orders', null, {
        params
    });
}

// 支付订单 - 修改为使用请求体
export const payOrderApi = (data) => {
    return request.post('/works/orders/pay', data);
}

// 查询订单
export const getOrdersApi = (params) => {
    return request.get('/works/orders', { params });
}

// 取消订单
export const cancelOrderApi = (id) => {
    return request.post('/works/orders/cancel', null, { params: { id } });
}

// 查询座位信息
export const getSeatsApi = (screeningId) => {
    return request.get('/works/seats', { params: { id: screeningId } });
}

// 创建退票申请
export const createRefundApi = (refundData) => request.post('/works/refunds', refundData);

// 查询用户退票申请列表
export const queryRefundsByUserIdApi = (userId) => request.get(`/works/refunds/${userId}`);

// 根据电影ID查询场次最低价格
export const queryMinPriceByMovieIdApi = (movieId) => request.get(`/works/price/${movieId}`);
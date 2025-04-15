import request from "@/utils/request";
import { sha256 } from 'js-sha256';

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
export const rechargeApi = (userId, amount) => {
    return request.post('/works/recharge', { id: userId, amount });
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

// 查询电影详情
export const getMovieDetailApi = (id) => {
    return request.get(`/works/movies_detail/${id}`);
}

// 查询场次信息
export const getScreeningsApi = (params) => {
    return request.get('/works/screenings', { params });
}

// 创建订单
export const createOrderApi = (data) => {
    return request.post('/works/orders', data);
}

// 支付订单
export const payOrderApi = (orderId, userId) => {
    return request.post('/works/orders/pay', null, {
        params: { orderId, userId }
    });
}

// 查询订单
export const getOrdersApi = (params) => {
    return request.get('/works/orders', { params });
}

// 取消订单
export const cancelOrderApi = (id) => {
    return request.post('/works/orders/cancel', null, { params: { id } });
}

// 申请退款
export const refundOrderApi = (data) => {
    return request.post('/works/refunds', data);
}
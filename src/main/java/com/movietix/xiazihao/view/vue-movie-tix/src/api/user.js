import request from "@/utils/request";

// 查询用户列表
export const queryUsersApi = (params) => request.get('/users', { params });

// 批量删除用户
export const deleteUsersApi = (ids) => request.delete('/users', { params: { ids } });

// 添加用户
export const addUserApi = (user) => request.post('/users', user);

// 修改密码
export const updatePasswordApi = (data) => request.put('/users/password', data);

// 修改余额
export const updateBalanceApi = (data) => request.put('/users/balance', data);

// 修改用户状态
export const updateUserStatusApi = (ids, status) => request.put('/users/status', null, { params: { ids, status } });

// 根据ID查询用户
export const queryUserByIdApi = (id) => request.get(`/users/${id}`);

// 用户登录
export const loginApi = (data) => request.post('/users/login', data);
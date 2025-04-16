import request from "@/utils/request";

// 分页查询退票申请
export const queryRefundsApi = (params) => request.get('/refunds', { params });

// 创建退票申请
export const createRefundApi = (data) => request.post('/refunds', data);

// 处理退票申请
export const processRefundApi = (params) => request.put('/refunds', null, { params });

// 查询用户退票申请
export const queryUserRefundsApi = (userId) => request.get(`/refunds/${userId}`);
import request from "@/utils/request";

// 订单列表查询
export const queryOrdersApi = (params) => request.get('/orders', { params });

// 创建订单
export const createOrderApi = (order) => request.post('/orders', order);

// 订单详情查询
export const queryOrderByIdApi = (id) => request.get(`/orders/${id}`);

// 取消订单
export const cancelOrderApi = (id) => request.put(`/orders/${id}/cancel`);

// 订单座位管理
export const batchAddSeatsApi = (seats) => request.post('/order_seats', seats);
export const querySeatByIdApi = (id) => request.get(`/order_seats/${id}`);
export const querySeatsByOrderApi = (orderId) => request.get(`/order_seats/by_order/${orderId}`);
export const updateSeatApi = (seat) => request.put('/order_seats', seat);
export const batchDeleteSeatsApi = (ids) => request.delete('/order_seats', { params: { ids } });

// 支付记录管理
export const queryPaymentsApi = (params) => request.get('/payments', { params });
export const createPaymentApi = (payment) => request.post('/payments', payment);
export const updatePaymentStatusApi = (id, data) => request.put(`/payments/${id}/notify`, data);
export const queryPaymentByIdApi = (id) => request.get(`/payments/${id}`);
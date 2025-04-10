import request from "@/utils/request";

// 查询放映场次列表
export const queryScreeningsApi = (params) => request.get('/screenings', { params });

// 批量删除场次
export const deleteScreeningsApi = (ids) => request.delete('/screenings', { params: { ids } });

// 添加场次
export const addScreeningApi = (screening) => request.post('/screenings', screening);

// 修改场次信息
export const updateScreeningApi = (screening) => request.put('/screenings', screening);

// 批量更新状态
export const updateStatusApi = (ids, status) => request.put('/screenings/status', null, { params: { ids, status } });

// 根据ID查询场次
export const queryScreeningByIdApi = (id) => request.get(`/screenings/${id}`);
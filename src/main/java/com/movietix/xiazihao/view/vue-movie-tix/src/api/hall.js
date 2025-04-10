import request from "@/utils/request";

// 分页查询放映厅信息
export const queryHallsApi = (params) => request.get('/halls', { params });

// 批量删除放映厅
export const deleteHallsApi = (ids) => request.delete('/halls', { params: { ids } });

// 新增放映厅
export const addHallApi = (hall) => request.post('/halls', hall);

// 修改放映厅信息
export const updateHallApi = (hall) => request.put('/halls', hall);

// 根据ID查询放映厅
export const queryHallByIdApi = (id) => request.get(`/halls/${id}`);


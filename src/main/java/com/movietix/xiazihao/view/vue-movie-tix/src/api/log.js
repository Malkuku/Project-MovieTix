// src/api/logApi.js


import request from "@/utils/request";

//查询全部部门数据
export const queryAllApi = () =>  request.get('/logs');

//新增
//export const addApi = (dept) =>  request.post('/depts', dept);

//根据ID查询
//export const queryByIdApi = (id) =>  request.get(`/depts/${id}`);

//修改
//export const updateApi = (dept) =>  request.put('/depts', dept);

//删除
export const deleteByIdApi = (id) =>  request.delete(`/logs?id=${id}`);
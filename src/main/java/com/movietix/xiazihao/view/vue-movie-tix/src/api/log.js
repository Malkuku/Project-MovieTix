// src/api/logApi.js


import request from "@/utils/request";

//查询全部部门数据
export const queryAllApi = () =>  request.get('/logs');

//删除
export const deleteByIdApi = (id) =>  request.delete(`/logs?id=${id}`);
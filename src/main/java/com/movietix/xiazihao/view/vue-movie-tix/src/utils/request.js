import axios from 'axios'
import {ElMessage} from "element-plus";
import {useUserStore} from "@/store/user";


//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

// // 请求拦截器
// request.interceptors.request.use(
//     (config) => {
//       const userStore = useUserStore();
//       if (userStore.token) {
//         config.headers['token'] = `Bearer ${userStore.token}`;
//       }
//       return config;
//     },
//     (error) => {
//       return Promise.reject(error);
//     }
// );
//
// // 响应拦截器
// request.interceptors.response.use(
//     (response) => {
//       const res = response.data;
//       if (res.code !== 1) {
//         ElMessage.error(res.msg || '请求失败');
//
//         // token失效处理
//         if (res.code === 401) {
//           const userStore = useUserStore();
//           userStore.clearUserInfo();
//           window.location.href = '/login';
//         }
//
//         return Promise.reject(new Error(res.msg || 'Error'));
//       } else {
//         return res;
//       }
//     },
//     (error) => {
//       ElMessage.error(error.message || '请求错误');
//       return Promise.reject(error);
//     }
// );

//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    return Promise.reject(error)
  }
)

export default request
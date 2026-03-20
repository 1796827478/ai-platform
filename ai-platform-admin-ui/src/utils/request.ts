import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API || '/api', // 会被 Vite proxy 代理或覆盖
  timeout: 15000, 
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 统一将 token 注入请求头
    const token = localStorage.getItem('admin_token');
    if (token && config.headers) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    // 假设后端统一返回 Result 对象 { code, message, data }
    // 这里的 code: 200 我们认为是成功
    if (res.code && res.code !== 200) {
      ElMessage.error(res.message || '业务请求异常');
      
      // 处理 Token 失效
      if (res.code === 401) {
         localStorage.removeItem('admin_token');
         window.location.href = '/login';
      }
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      // 抹平包装，直接返回给业务方 data 实体
      return res.data !== undefined ? res.data : res;
    }
  },
  (error) => {
    // 真正的 HTTP 请求层面抛出了 401, 404, 500 等
    if (error.response && error.response.status === 401) {
       ElMessage.error('认证过期，请重新登录！');
       localStorage.removeItem('admin_token');
       window.location.href = '/login';
    } else {
       ElMessage.error(error.message || '网络或服务器异常');
    }
    return Promise.reject(error);
  }
);

export default service;

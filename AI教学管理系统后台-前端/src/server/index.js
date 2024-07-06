// src/services/axios.js
import axios from 'axios';

const baseURL = import.meta.env.VITE_API_BASE_URL

// 创建一个 axios 实例
const axiosInstance = axios.create({
  baseURL: baseURL, // 使用环境变量配置基础URL
  timeout: 10000, // 请求超时时间
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  config => {
    // 在请求发送之前做一些处理
    const tokenKey = import.meta.env.VITE_TOKEN_KEY;
    const token = localStorage.getItem(tokenKey);
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    // 处理请求错误
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  response => {
    // 处理响应数据
    return response;
  },
  error => {
    // 处理响应错误
    if (error.response && error.response.status === 401) {
      // 未授权错误处理，如重定向到登录页面
      window.location.href = '/signIn';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;

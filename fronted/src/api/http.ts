import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const http = axios.create({
  baseURL: '/api'
});

http.interceptors.request.use((config) => {
  const auth = useAuthStore();
  if (auth.token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${auth.token}`;
  }
  return config;
});

http.interceptors.response.use(
  (resp) => {
    const { data } = resp;
    if (data && typeof data.code !== 'undefined' && data.code !== 0) {
      return Promise.reject(new Error(data.message || '请求失败'));
    }
    return data.data;
  },
  (error) => Promise.reject(error)
);

export default http;

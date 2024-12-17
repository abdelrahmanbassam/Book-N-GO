import axios from 'axios';

const client = axios.create({
  baseURL: 'http://localhost:8080',
  json: true
});

client.interceptors.request.use(
  config => {
    const token = window.localStorage.getItem('token');
    config.headers.Authorization = token ? `Bearer ${token}` : '';
    return config;
  },
  error => {
    if (error.response.status === 401) {
      window.location = '/login';
    }
    return Promise.reject(error);
  }
);


export default client;
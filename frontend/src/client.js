import axios from 'axios';

const client = axios.create({
  baseURL: 'http://localhost:8080',
  json: true
});

const permitAll = ['/auth/login', '/auth/register'];

client.interceptors.request.use(
  config => {
    const token = window.localStorage.getItem('token');
    console.log(config.url)
    if(!token && !permitAll.includes(config.url)) {
      window.location = '/login';
    }

    if(permitAll.includes(config.url)) {
      return config;
    }
    
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
import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth endpoints
export const authApi = {
  register: (data: any) => api.post('/auth/register', data),
  login: (data: any) => api.post('/auth/login', data),
};

// Event endpoints
export const eventApi = {
  getAll: () => api.get('/events'),
  getById: (id: number) => api.get(`/events/${id}`),
  getByType: (type: string) => api.get(`/events/type/${type}`),
  getUpcoming: () => api.get('/events/upcoming'),
  getMyEvents: () => api.get('/events/my-events'),
  create: (data: any) => api.post('/events', data),
  update: (id: number, data: any) => api.put(`/events/${id}`, data),
  delete: (id: number) => api.delete(`/events/${id}`),
};

// Booking endpoints
export const bookingApi = {
  create: (eventId: number) => api.post(`/bookings/event/${eventId}`),
  cancel: (bookingId: number) => api.put(`/bookings/${bookingId}/cancel`),
  getMyBookings: () => api.get('/bookings/my-bookings'),
  getEventBookings: (eventId: number) => api.get(`/bookings/event/${eventId}`),
  getByCode: (code: string) => api.get(`/bookings/code/${code}`),
};

// Notification endpoints
export const notificationApi = {
  getAll: () => api.get('/notifications'),
  getUnread: () => api.get('/notifications/unread'),
  markAsRead: (id: number) => api.put(`/notifications/${id}/read`),
};

// Report endpoints
export const reportApi = {
  getEventReport: (eventId: number) => api.get(`/admin/reports/event/${eventId}`),
  getOverallReport: () => api.get('/admin/reports/overall'),
};

export default api;

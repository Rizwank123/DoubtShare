import { LoginRequest, LoginResponse, RegisterRequest, User, CreateDoubtRequest, DoubtRequest, PagedDoubtRequests } from '../types';

const API_URL = 'http://localhost:8080/api';

// Helper function to handle fetch responses
// Updated: Safe against empty response bodies (fixes JSON.parse issue)
const handleResponse = async (response: Response) => {
  const text = await response.text(); // safer than response.json() directly

  let data;
  try {
    data = text ? JSON.parse(text) : {};
  } catch (error) {
    console.warn('Failed to parse JSON:', error);
    data = {};
  }

  if (!response.ok) {
    const error = data?.message || response.statusText;
    return Promise.reject(error);
  }

  return data;
};


// Get auth token from local storage
const getToken = () => localStorage.getItem('token');

// Auth Services
export const authService = {
  register: async (registerData: RegisterRequest): Promise<User> => {
    const response = await fetch(`${API_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(registerData)
    });
    return handleResponse(response);
  },

  login: async (loginData: LoginRequest): Promise<LoginResponse> => {
    const response = await fetch(`${API_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(loginData)
    });
    return handleResponse(response);
  },

  getCurrentUser: async (): Promise<User> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/auth/user?token=${token}`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
};

// Doubt Services
export const doubtService = {
  createDoubt: async (doubtData: CreateDoubtRequest): Promise<DoubtRequest> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/doubt-request/`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(doubtData)
    });
    return handleResponse(response);
  },

  getDoubts: async (page = 0, size = 10): Promise<PagedDoubtRequests> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/doubt-request/?page=${page}&size=${size}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  },

  getStudentHistory: async (studentId: string, page = 0, size = 10): Promise<PagedDoubtRequests> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/doubt-request/history/${studentId}?page=${page}&size=${size}`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  },

  acceptDoubt: async (doubtId: string): Promise<DoubtRequest> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/doubt-request/${doubtId}/accept`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  },

  completeDoubt: async (doubtId: string): Promise<DoubtRequest> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/doubt-request/${doubtId}/complete`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  }
};

// Tutor Availability Services
export const tutorService = {
  updateAvailability: async (isAvailable: boolean): Promise<void> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/tutor-availability/status?isAvailable=${isAvailable}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  },

  ping: async (): Promise<void> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/tutor-availability/ping`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  },

  getOnlineCount: async (): Promise<number> => {
    const token = getToken();
    const response = await fetch(`${API_URL}/tutor-availability/online-count`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    return handleResponse(response);
  }
};
// User Types
export type UserType = 'STUDENT' | 'TUTOR';

export interface User {
  id: string;
  username: string;
  fullName: string;
  email: string;
  userType: UserType;
  language: string;
  classGrade?: number;
  subjectExpertise?: string[];
  teachingGrades?: number[];
}

export interface RegisterRequest {
  username: string;
  fullName: string;
  email: string;
  password: string;
  userType: UserType;
  language: string;
  classGrade?: number;
  subjectExpertise?: string[];
  teachingGrades?: number[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

// Doubt Types
export type DoubtStatus = 'PENDING' | 'ACCEPTED' | 'COMPLETED' | 'EXPIRED';

export interface DoubtRequest {
  id: string;
  studentId: string;
  studentUsername: string;
  tutorId?: string;
  tutorUsername?: string;
  subject: string;
  doubtDescription: string;
  status: DoubtStatus;
  createdAt: string;
  acceptedAt?: string;
  completedAt?: string;
}

export interface CreateDoubtRequest {
  subject: string;
  doubtDescription: string;
}

export interface PageInfo {
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface PagedResponse<T> extends PageInfo {
  content: T[];
}

export type PagedDoubtRequests = PagedResponse<DoubtRequest>;
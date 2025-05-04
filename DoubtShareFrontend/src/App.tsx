import React, { lazy, Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';

// Lazy-loaded components
const Login = lazy(() => import('./pages/Login'));
const Register = lazy(() => import('./pages/Register'));
const StudentDashboard = lazy(() => import('./pages/StudentDashboard'));
const TutorDashboard = lazy(() => import('./pages/TutorDashboard'));
const NotFound = lazy(() => import('./pages/NotFound'));

// Protected Route Component
const ProtectedRoute: React.FC<{ 
  element: React.ReactNode; 
  requiredUserType?: 'STUDENT' | 'TUTOR';
}> = ({ element, requiredUserType }) => {
  const { isAuthenticated, user, isLoading } = useAuth();
  
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }
  
  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }
  
  if (requiredUserType && user?.userType !== requiredUserType) {
    return <Navigate to={user?.userType === 'STUDENT' ? "/student-dashboard" : "/tutor-dashboard"} />;
  }
  
  return <>{element}</>;
};

const LoadingFallback: React.FC = () => (
  <div className="min-h-screen flex items-center justify-center">
    <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
  </div>
);

const AppRoutes: React.FC = () => {
  const { isAuthenticated, user } = useAuth();
  
  return (
    <Suspense fallback={<LoadingFallback />}>
      <Routes>
        {/* Public Routes */}
        <Route 
          path="/login" 
          element={isAuthenticated ? 
            <Navigate to={user?.userType === 'STUDENT' ? "/student-dashboard" : "/tutor-dashboard"} /> : 
            <Login />
          } 
        />
        <Route 
          path="/register" 
          element={isAuthenticated ? 
            <Navigate to={user?.userType === 'STUDENT' ? "/student-dashboard" : "/tutor-dashboard"} /> : 
            <Register />
          } 
        />
        
        {/* Protected Routes */}
        <Route 
          path="/student-dashboard" 
          element={<ProtectedRoute element={<StudentDashboard />} requiredUserType="STUDENT" />} 
        />
        <Route 
          path="/tutor-dashboard" 
          element={<ProtectedRoute element={<TutorDashboard />} requiredUserType="TUTOR" />} 
        />
        
        {/* Redirect root based on auth status */}
        <Route 
          path="/" 
          element={
            isAuthenticated ? 
            <Navigate to={user?.userType === 'STUDENT' ? "/student-dashboard" : "/tutor-dashboard"} /> : 
            <Navigate to="/login" />
          }
        />
        
        {/* 404 Route */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Suspense>
  );
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
};

export default App;
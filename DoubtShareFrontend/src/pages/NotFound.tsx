import React from 'react';
import { Link } from 'react-router-dom';
import Button from '../components/Button';
import { AlertTriangle, Home } from 'lucide-react';

const NotFound: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center items-center px-4">
      <AlertTriangle className="h-16 w-16 text-amber-500 mb-6" />
      <h1 className="text-4xl font-bold text-gray-900 mb-2 text-center">Page Not Found</h1>
      <p className="text-gray-600 mb-8 text-center max-w-md">
        The page you're looking for doesn't exist or has been moved.
      </p>
      <Link to="/">
        <Button variant="primary" className="flex items-center">
          <Home className="h-4 w-4 mr-2" />
          Go to Homepage
        </Button>
      </Link>
    </div>
  );
};

export default NotFound;
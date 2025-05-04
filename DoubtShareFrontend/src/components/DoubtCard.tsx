import React from 'react';
import { DoubtRequest } from '../types';
import { Clock, CheckCircle, AlertCircle, CheckCheck } from 'lucide-react';
import Button from './Button';

interface DoubtCardProps {
  doubt: DoubtRequest;
  userType: 'STUDENT' | 'TUTOR';
  onAccept?: (doubtId: string) => void;
  onComplete?: (doubtId: string) => void;
  isLoading?: boolean;
}

const DoubtCard: React.FC<DoubtCardProps> = ({
  doubt,
  userType,
  onAccept,
  onComplete,
  isLoading = false,
}) => {
  const formatDate = (dateString?: string) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleString();
  };

  const getStatusIcon = () => {
    switch (doubt.status) {
      case 'PENDING':
        return <Clock className="h-5 w-5 text-amber-500" />;
      case 'ACCEPTED':
        return <CheckCircle className="h-5 w-5 text-blue-500" />;
      case 'COMPLETED':
        return <CheckCheck className="h-5 w-5 text-green-500" />;
      case 'EXPIRED':
        return <AlertCircle className="h-5 w-5 text-red-500" />;
      default:
        return null;
    }
  };

  const getStatusBadgeClass = () => {
    switch (doubt.status) {
      case 'PENDING':
        return 'bg-amber-100 text-amber-800';
      case 'ACCEPTED':
        return 'bg-blue-100 text-blue-800';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'EXPIRED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const renderActionButton = () => {
    if (userType === 'TUTOR') {
      if (doubt.status === 'PENDING') {
        return (
          <Button
            variant="primary"
            size="sm"
            onClick={() => onAccept?.(doubt.id)}
            isLoading={isLoading}
          >
            Accept
          </Button>
        );
      } else if (doubt.status === 'ACCEPTED') {
        return (
          <Button
            variant="success"
            size="sm"
            onClick={() => onComplete?.(doubt.id)}
            isLoading={isLoading}
          >
            Complete
          </Button>
        );
      }
    }
    return null;
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-4 border border-gray-200 hover:shadow-lg transition-shadow">
      <div className="flex justify-between items-start mb-3">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">{doubt.subject}</h3>
          <p className="text-sm text-gray-500">
            by {doubt.studentUsername}
          </p>
        </div>
        <div className="flex items-center">
          {getStatusIcon()}
          <span className={`ml-2 text-xs px-2 py-1 rounded-full ${getStatusBadgeClass()}`}>
            {doubt.status}
          </span>
        </div>
      </div>
      
      <p className="text-gray-700 mb-4">{doubt.doubtDescription}</p>
      
      <div className="grid grid-cols-2 gap-2 text-xs text-gray-500 mb-4">
        <div>
          <span className="font-medium">Created:</span> {formatDate(doubt.createdAt)}
        </div>
        {doubt.acceptedAt && (
          <div>
            <span className="font-medium">Accepted:</span> {formatDate(doubt.acceptedAt)}
          </div>
        )}
        {doubt.completedAt && (
          <div>
            <span className="font-medium">Completed:</span> {formatDate(doubt.completedAt)}
          </div>
        )}
      </div>
      
      <div className="flex justify-end">
        {renderActionButton()}
      </div>
    </div>
  );
};

export default DoubtCard;
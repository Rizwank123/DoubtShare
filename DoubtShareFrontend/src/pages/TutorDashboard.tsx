import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { doubtService, tutorService } from '../services/api';
import { DoubtRequest, PagedDoubtRequests } from '../types';
import Navbar from '../components/Navbar';
import DoubtCard from '../components/DoubtCard';
import Button from '../components/Button';
import EmptyState from '../components/EmptyState';
import Pagination from '../components/Pagination';
import { RefreshCw, UserCheck, Users } from 'lucide-react';

const TutorDashboard: React.FC = () => {
  const { user } = useAuth();
  const [doubts, setDoubts] = useState<DoubtRequest[]>([]);
  const [pageInfo, setPageInfo] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [activeDoubtId, setActiveDoubtId] = useState<string | null>(null);
  const [isAvailable, setIsAvailable] = useState(true);
  const [onlineTutors, setOnlineTutors] = useState(0);

  const fetchDoubts = useCallback(async (page = 0) => {
    if (!user?.id) return;

    setIsLoading(true);
    setError('');

    try {
      const response: PagedDoubtRequests = await doubtService.getDoubts(page, 10);
      console.log(response);
      setDoubts(response.content || []);
      setPageInfo({
        currentPage: response.number || 0,
        totalPages: response.totalPages || 0,
        totalElements: response.totalElements || 0
      });
    } catch (err) {
      setError('Failed to load doubts. Please try again.');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  }, [user?.id]);

  const fetchOnlineTutors = useCallback(async () => {
    try {
      const count = await tutorService.getOnlineCount();
      setOnlineTutors(count);
    } catch (err) {
      console.error('Failed to get online tutors count:', err);
    }
  }, []);

  const pingServer = useCallback(async () => {
    if (isAvailable && user?.userType === 'TUTOR') {
      try {
        await tutorService.ping();
      } catch (err) {
        console.error('Failed to ping server:', err);
      }
    }
  }, [isAvailable, user?.userType]);

  const handleAcceptDoubt = async (doubtId: string) => {
    if (activeDoubtId) return;

    setActiveDoubtId(doubtId);
    setError('');

    try {
      await doubtService.acceptDoubt(doubtId);
      await fetchDoubts(pageInfo.currentPage);
    } catch (err: unknown) {
      setError(err?.toString() || 'Unknown error');
      console.error(err);
    } finally {
      setActiveDoubtId(null);
    }
  };

  const handleCompleteDoubt = async (doubtId: string) => {
    setActiveDoubtId(doubtId);
    setError('');

    try {
      await doubtService.completeDoubt(doubtId);
      await fetchDoubts(pageInfo.currentPage);
    } catch (err) {
      setError('Failed to complete doubt. Please try again.');
      console.error(err);
    } finally {
      setActiveDoubtId(null);
    }
  };

  const handleToggleAvailability = async () => {
    try {
      await tutorService.updateAvailability(!isAvailable);
      setIsAvailable(!isAvailable);
    } catch (err) {
      console.error('Failed to update availability:', err);
    }
  };

  const handlePageChange = (page: number) => {
    fetchDoubts(page);
  };

  useEffect(() => {
    if (user?.userType === 'TUTOR') {
      fetchDoubts();
      fetchOnlineTutors();
    }
  }, [user?.userType, fetchDoubts, fetchOnlineTutors]);

  useEffect(() => {
    if (user?.userType === 'TUTOR') {
      pingServer();
      const pingInterval = setInterval(pingServer, 30000);
      return () => clearInterval(pingInterval);
    }
  }, [user?.userType, pingServer]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 sm:px-0">
          <div className="mb-6 flex flex-col sm:flex-row justify-between items-start sm:items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Tutor Dashboard</h1>
              <p className="text-sm text-gray-500 mt-1">
                Help students by answering their doubts
              </p>
            </div>
            <div className="mt-4 sm:mt-0 flex space-x-3">
              <div className="flex items-center text-sm text-gray-700 mr-4">
                <Users className="h-4 w-4 mr-1 text-blue-600" />
                <span>{onlineTutors} tutors online</span>
              </div>
              <Button
                variant={isAvailable ? 'success' : 'secondary'}
                size="sm"
                onClick={handleToggleAvailability}
                className="flex items-center"
              >
                <UserCheck className="h-4 w-4 mr-1" />
                {isAvailable ? 'Available' : 'Unavailable'}
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => fetchDoubts(pageInfo.currentPage)}
                disabled={isLoading}
                className="flex items-center"
              >
                <RefreshCw className={`h-4 w-4 mr-1 ${isLoading ? 'animate-spin' : ''}`} />
                Refresh
              </Button>
            </div>
          </div>

          {error && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}

          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-4 sm:p-6 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">All Student Doubts</h2>
              <p className="text-sm text-gray-500 mt-1">
                {pageInfo.totalElements} total doubts
              </p>
            </div>

            <div className="divide-y divide-gray-200">
              {isLoading && !doubts.length ? (
                <div className="p-6 text-center text-gray-500">Loading...</div>
              ) : doubts.length === 0 ? (
                <div className="p-6">
                  <EmptyState
                    title="No doubts available"
                    description="There are no student doubts to answer at the moment. Check back later or refresh the page."
                    actionText="Refresh"
                    onAction={() => fetchDoubts(0)}
                  />
                </div>
              ) : (
                <div className="grid gap-4 p-4 sm:p-6">
                  {doubts.map(doubt => (
                    <DoubtCard
                      key={doubt.id}
                      doubt={doubt}
                      userType="TUTOR"
                      onAccept={handleAcceptDoubt}
                      onComplete={handleCompleteDoubt}
                      isLoading={activeDoubtId === doubt.id}
                    />
                  ))}
                </div>
              )}
            </div>

            <div className="p-4 border-t border-gray-200">
              <Pagination
                currentPage={pageInfo.currentPage}
                totalPages={pageInfo.totalPages}
                onPageChange={handlePageChange}
                disabled={isLoading}
              />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default TutorDashboard;
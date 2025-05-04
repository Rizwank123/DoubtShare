import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { doubtService } from '../services/api';
import { CreateDoubtRequest, DoubtRequest, PagedDoubtRequests } from '../types';
import Navbar from '../components/Navbar';
import DoubtCard from '../components/DoubtCard';
import Button from '../components/Button';
import EmptyState from '../components/EmptyState';
import Pagination from '../components/Pagination';
import CreateDoubtModal from '../components/CreateDoubtModal';
import { PlusCircle, RefreshCw } from 'lucide-react';

const StudentDashboard: React.FC = () => {
  const { user } = useAuth();
  const [doubts, setDoubts] = useState<DoubtRequest[]>([]);
  const [pageInfo, setPageInfo] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0
  });
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  const fetchDoubts = async (page = 0) => {
    if (!user?.id) return;
    
    setIsLoading(true);
    setError('');
    
    try {
      const response: PagedDoubtRequests = await doubtService.getStudentHistory(user.id, page, 10);
      setDoubts(response.content);
      setPageInfo({
        currentPage: response.number,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      });
    } catch (err) {
      setError('Failed to load doubts. Please try again.');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreateDoubt = async (doubtData: CreateDoubtRequest) => {
    setIsSubmitting(true);
    setError('');
    
    try {
      await doubtService.createDoubt(doubtData);
      setIsModalOpen(false);
      // Fetch updated list
      fetchDoubts(0);
    } catch (err) {
      setError('Failed to create doubt. Please try again.');
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handlePageChange = (page: number) => {
    fetchDoubts(page);
  };

  useEffect(() => {
    if (user?.id) {
      fetchDoubts();
    }
  }, [user?.id]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 sm:px-0">
          <div className="mb-6 flex flex-col sm:flex-row justify-between items-start sm:items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Student Dashboard</h1>
              <p className="text-sm text-gray-500 mt-1">
                Ask questions and track your learning progress
              </p>
            </div>
            <div className="mt-4 sm:mt-0 flex space-x-3">
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
              <Button 
                variant="primary" 
                size="sm"
                onClick={() => setIsModalOpen(true)}
                className="flex items-center"
              >
                <PlusCircle className="h-4 w-4 mr-1" />
                Create Doubt
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
              <h2 className="text-lg font-medium text-gray-900">Your Doubt History</h2>
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
                    title="No doubts yet"
                    description="You haven't created any doubts yet. Create your first doubt to get help from tutors."
                    actionText="Create Doubt"
                    onAction={() => setIsModalOpen(true)}
                  />
                </div>
              ) : (
                <div className="grid gap-4 p-4 sm:p-6">
                  {doubts.map(doubt => (
                    <DoubtCard
                      key={doubt.id}
                      doubt={doubt}
                      userType="STUDENT"
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
      
      <CreateDoubtModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleCreateDoubt}
        isLoading={isSubmitting}
      />
    </div>
  );
};

export default StudentDashboard;
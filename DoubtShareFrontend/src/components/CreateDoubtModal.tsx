import React, { useState } from 'react';
import { CreateDoubtRequest } from '../types';
import Button from './Button';
import { X } from 'lucide-react';

interface CreateDoubtModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (doubtData: CreateDoubtRequest) => void;
  isLoading: boolean;
}

const SUBJECTS = [
  'Mathematics',
  'Physics',
  'Chemistry',
  'Biology',
  'Computer Science',
  'English',
  'History',
  'Geography',
  'Economics'
];

const CreateDoubtModal: React.FC<CreateDoubtModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  isLoading
}) => {
  const [doubtData, setDoubtData] = useState<CreateDoubtRequest>({
    subject: SUBJECTS[0],
    doubtDescription: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setDoubtData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(doubtData);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4 overflow-hidden">
        <div className="flex justify-between items-center p-4 border-b">
          <h2 className="text-xl font-semibold text-gray-900">Create New Doubt</h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
          >
            <X className="h-5 w-5" />
          </button>
        </div>
        
        <form onSubmit={handleSubmit} className="p-4">
          <div className="mb-4">
            <label htmlFor="subject" className="block text-sm font-medium text-gray-700 mb-1">
              Subject
            </label>
            <select
              id="subject"
              name="subject"
              value={doubtData.subject}
              onChange={handleChange}
              className="w-full rounded-md border border-gray-300 py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              required
            >
              {SUBJECTS.map(subject => (
                <option key={subject} value={subject}>
                  {subject}
                </option>
              ))}
            </select>
          </div>
          
          <div className="mb-6">
            <label htmlFor="doubtDescription" className="block text-sm font-medium text-gray-700 mb-1">
              Doubt Description
            </label>
            <textarea
              id="doubtDescription"
              name="doubtDescription"
              value={doubtData.doubtDescription}
              onChange={handleChange}
              rows={5}
              className="w-full rounded-md border border-gray-300 py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Describe your doubt in detail..."
              required
            />
          </div>
          
          <div className="flex justify-end space-x-3">
            <Button 
              type="button" 
              variant="ghost" 
              onClick={onClose}
              disabled={isLoading}
            >
              Cancel
            </Button>
            <Button 
              type="submit" 
              variant="primary"
              isLoading={isLoading}
            >
              Submit Doubt
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateDoubtModal;
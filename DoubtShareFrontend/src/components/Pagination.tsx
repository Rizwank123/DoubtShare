import React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  disabled?: boolean;
}

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  disabled = false
}) => {
  // If there's only 1 page or less, don't show pagination
  if (totalPages <= 1) return null;

  const handlePrevious = () => {
    if (currentPage > 0 && !disabled) {
      onPageChange(currentPage - 1);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1 && !disabled) {
      onPageChange(currentPage + 1);
    }
  };

  // Create page numbers to display
  const getPageNumbers = () => {
    const pageNumbers = [];
    const maxPagesToShow = 5;
    
    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
    
    if (endPage - startPage + 1 < maxPagesToShow) {
      startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(i);
    }
    
    return pageNumbers;
  };

  return (
    <div className="flex items-center justify-center space-x-2 mt-4">
      <button
        onClick={handlePrevious}
        disabled={currentPage === 0 || disabled}
        className={`p-2 rounded-md border ${
          currentPage === 0 || disabled
            ? 'text-gray-400 border-gray-200 cursor-not-allowed'
            : 'text-gray-700 border-gray-300 hover:bg-gray-50'
        }`}
        aria-label="Previous page"
      >
        <ChevronLeft className="h-4 w-4" />
      </button>
      
      <div className="flex space-x-1">
        {getPageNumbers().map(pageNumber => (
          <button
            key={pageNumber}
            onClick={() => !disabled && onPageChange(pageNumber)}
            disabled={disabled}
            className={`px-3 py-1.5 text-sm rounded-md ${
              pageNumber === currentPage
                ? 'bg-blue-600 text-white'
                : 'text-gray-700 hover:bg-gray-100'
            } ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
          >
            {pageNumber + 1}
          </button>
        ))}
      </div>
      
      <button
        onClick={handleNext}
        disabled={currentPage >= totalPages - 1 || disabled}
        className={`p-2 rounded-md border ${
          currentPage >= totalPages - 1 || disabled
            ? 'text-gray-400 border-gray-200 cursor-not-allowed'
            : 'text-gray-700 border-gray-300 hover:bg-gray-50'
        }`}
        aria-label="Next page"
      >
        <ChevronRight className="h-4 w-4" />
      </button>
    </div>
  );
};

export default Pagination;
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/api';
import { RegisterRequest, UserType } from '../types';
import Button from '../components/Button';
import { UserPlus, HelpCircle } from 'lucide-react';

const LANGUAGES = ['English', 'Hindi', 'Spanish', 'French', 'Mandarin', 'Arabic'];
const SUBJECTS = ['Mathematics', 'Physics', 'Chemistry', 'Biology', 'Computer Science', 'English', 'History'];
const GRADES = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

const Register: React.FC = () => {
  const [formData, setFormData] = useState<RegisterRequest>({
    username: '',
    fullName: '',
    email: '',
    password: '',
    userType: 'STUDENT',
    language: 'English',
    classGrade: 10,
    subjectExpertise: [],
    teachingGrades: []
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleUserTypeChange = (type: UserType) => {
    setFormData(prev => ({ ...prev, userType: type }));
  };

  const handleSubjectChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const subject = e.target.value;
    const isChecked = e.target.checked;

    setFormData(prev => ({
      ...prev,
      subjectExpertise: isChecked
        ? [...(prev.subjectExpertise || []), subject]
        : (prev.subjectExpertise || []).filter(s => s !== subject)
    }));
  };

  const handleGradeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const grade = parseInt(e.target.value, 10);
    const isChecked = e.target.checked;

    setFormData(prev => ({
      ...prev,
      teachingGrades: isChecked
        ? [...(prev.teachingGrades || []), grade]
        : (prev.teachingGrades || []).filter(g => g !== grade)
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    // Validation
    if (formData.userType === 'TUTOR' && (!formData.subjectExpertise?.length || !formData.teachingGrades?.length)) {
      setError('Tutors must select at least one subject expertise and one teaching grade');
      setIsLoading(false);
      return;
    }

    try {
      await authService.register(formData);
      navigate('/login', { state: { message: 'Registration successful! Please log in.' } });
    } catch (err) {
      setError(typeof err === 'string' ? err : 'Registration failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="flex justify-center">
          <HelpCircle className="h-12 w-12 text-blue-600" />
        </div>
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Create your account
        </h2>
        <p className="mt-2 text-center text-sm text-gray-600">
          Already have an account?{' '}
          <Link to="/login" className="font-medium text-blue-600 hover:text-blue-500">
            Sign in
          </Link>
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          {error && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}

          <form className="space-y-6" onSubmit={handleSubmit}>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                I am a
              </label>
              <div className="mt-1 grid grid-cols-2 gap-3">
                <div
                  className={`flex items-center justify-center px-4 py-2 border rounded-md cursor-pointer ${formData.userType === 'STUDENT'
                    ? 'bg-blue-50 border-blue-500 text-blue-700'
                    : 'border-gray-300 text-gray-700 hover:bg-gray-50'
                    }`}
                  onClick={() => handleUserTypeChange('STUDENT')}
                >
                  Student
                </div>
                <div
                  className={`flex items-center justify-center px-4 py-2 border rounded-md cursor-pointer ${formData.userType === 'TUTOR'
                    ? 'bg-blue-50 border-blue-500 text-blue-700'
                    : 'border-gray-300 text-gray-700 hover:bg-gray-50'
                    }`}
                  onClick={() => handleUserTypeChange('TUTOR')}
                >
                  Tutor
                </div>
              </div>
            </div>

            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                Full Name
              </label>
              <div className="mt-1">
                <input
                  id="fullName"
                  name="fullName"
                  type="text"
                  autoComplete="fullName"
                  required
                  value={formData.fullName}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                Username
              </label>
              <div className="mt-1">
                <input
                  id="username"
                  name="username"
                  type="text"
                  autoComplete="username"
                  required
                  value={formData.username}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Email address
              </label>
              <div className="mt-1">
                <input
                  id="email"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  value={formData.email}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Password
              </label>
              <div className="mt-1">
                <input
                  id="password"
                  name="password"
                  type="password"
                  autoComplete="new-password"
                  required
                  value={formData.password}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="language" className="block text-sm font-medium text-gray-700">
                Preferred Language
              </label>
              <div className="mt-1">
                <select
                  id="language"
                  name="language"
                  required
                  value={formData.language}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                >
                  {LANGUAGES.map(lang => (
                    <option key={lang} value={lang}>
                      {lang}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            {formData.userType === 'STUDENT' && (
              <div>
                <label htmlFor="classGrade" className="block text-sm font-medium text-gray-700">
                  Class/Grade
                </label>
                <div className="mt-1">
                  <select
                    id="classGrade"
                    name="classGrade"
                    required
                    value={formData.classGrade}
                    onChange={handleChange}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  >
                    {GRADES.map(grade => (
                      <option key={grade} value={grade}>
                        Grade {grade}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            )}

            {formData.userType === 'TUTOR' && (
              <>
                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Subject Expertise
                  </label>
                  <div className="mt-2 grid grid-cols-2 gap-2">
                    {SUBJECTS.map(subject => (
                      <div key={subject} className="flex items-center">
                        <input
                          id={`subject-${subject}`}
                          name="subjectExpertise"
                          type="checkbox"
                          value={subject}
                          checked={formData.subjectExpertise?.includes(subject) || false}
                          onChange={handleSubjectChange}
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label htmlFor={`subject-${subject}`} className="ml-2 block text-sm text-gray-700">
                          {subject}
                        </label>
                      </div>
                    ))}
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Teaching Grades
                  </label>
                  <div className="mt-2 grid grid-cols-4 gap-2">
                    {GRADES.map(grade => (
                      <div key={grade} className="flex items-center">
                        <input
                          id={`grade-${grade}`}
                          name="teachingGrades"
                          type="checkbox"
                          value={grade}
                          checked={formData.teachingGrades?.includes(grade) || false}
                          onChange={handleGradeChange}
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label htmlFor={`grade-${grade}`} className="ml-2 block text-sm text-gray-700">
                          {grade}
                        </label>
                      </div>
                    ))}
                  </div>
                </div>
              </>
            )}

            <div>
              <Button
                type="submit"
                variant="primary"
                fullWidth
                isLoading={isLoading}
                className="group"
              >
                <UserPlus className="h-4 w-4 mr-2" />
                Register
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;
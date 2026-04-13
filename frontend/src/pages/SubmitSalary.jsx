import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
// import api from '../services/api';

export const SubmitSalary = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [formData, setFormData] = useState({
    company: '',
    role: '',
    salary: '',
    experience: '',
    location: '',
    anonymous: true
  });

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      // Mock API call
      // await api.post('/salary/submit', formData);
      await new Promise(r => setTimeout(r, 1000));
      
      setSuccess(true);
      setTimeout(() => {
        navigate('/search');
      }, 2000);
    } catch (error) {
      console.error(error);
      alert('Failed to submit salary');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="min-h-[calc(100vh-4rem)] flex items-center justify-center p-4">
        <Card className="w-full max-w-lg text-center p-8 animate-in zoom-in duration-300">
          <div className="w-16 h-16 bg-green-100 text-green-500 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Submission Successful</h2>
          <p className="text-gray-500">Thank you for contributing to transparency. Redirecting to search...</p>
        </Card>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto py-12 px-4 sm:px-6">
      <Card className="animate-in fade-in slide-in-from-bottom-4 duration-700">
        <CardHeader>
          <CardTitle className="text-2xl">Share Your Salary</CardTitle>
          <p className="text-gray-500 text-sm mt-1">Help others by sharing your compensation details. It takes less than a minute.</p>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
              <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">Company Name</label>
                <Input name="company" value={formData.company} onChange={handleChange} required placeholder="e.g. Google" />
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">Job Role</label>
                <Input name="role" value={formData.role} onChange={handleChange} required placeholder="e.g. Frontend Engineer" />
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">Annual Salary (USD)</label>
                <div className="relative">
                  <span className="absolute left-3 top-2.5 text-gray-500">$</span>
                  <Input type="number" name="salary" value={formData.salary} onChange={handleChange} required min="0" className="pl-8" placeholder="120000" />
                </div>
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">Years of Experience</label>
                <Input type="number" name="experience" value={formData.experience} onChange={handleChange} required min="0" step="0.5" placeholder="3" />
              </div>
              <div className="space-y-2 sm:col-span-2">
                <label className="text-sm font-medium text-gray-700">Location (Optional)</label>
                <Input name="location" value={formData.location} onChange={handleChange} placeholder="e.g. San Francisco, CA or Remote" />
              </div>
            </div>

            <div className="flex items-center gap-2 p-4 bg-gray-50 rounded-xl border border-gray-100">
              <input 
                type="checkbox" 
                id="anonymous" 
                name="anonymous" 
                checked={formData.anonymous} 
                onChange={handleChange}
                className="w-4 h-4 text-primary-600 rounded border-gray-300 focus:ring-primary-500"
              />
              <label htmlFor="anonymous" className="text-sm text-gray-700 font-medium cursor-pointer">
                Submit anonymously (hide user details if logged in)
              </label>
            </div>

            <div className="pt-4 border-t border-gray-100 flex justify-end">
              <Button type="submit" size="lg" disabled={loading} className="w-full sm:w-auto">
                {loading ? 'Submitting...' : 'Submit Salary Details'}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

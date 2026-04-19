import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/Card';
import { TrendingUp, Users, DollarSign } from 'lucide-react';

export const Stats = () => {
  return (
    <div className="max-w-5xl mx-auto py-8 px-4 sm:px-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 tracking-tight">Market Statistics</h1>
        <p className="text-gray-500 mt-2">Overall insights based on community submissions.</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 mb-8">
        <Card className="bg-primary-50/50 border-primary-100">
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-primary-600 mb-1">Total Submissions</p>
                <h3 className="text-3xl font-bold text-gray-900">12,405</h3>
              </div>
              <div className="bg-white p-2 rounded-lg shadow-sm">
                <Users className="w-6 h-6 text-primary-500" />
              </div>
            </div>
          </CardContent>
        </Card>
        
        <Card className="bg-blue-50/50 border-blue-100">
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-blue-600 mb-1">Average Salary</p>
                <h3 className="text-3xl font-bold text-gray-900">$142,500</h3>
              </div>
              <div className="bg-white p-2 rounded-lg shadow-sm">
                <DollarSign className="w-6 h-6 text-blue-500" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="bg-indigo-50/50 border-indigo-100">
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-indigo-600 mb-1">Median Salary</p>
                <h3 className="text-3xl font-bold text-gray-900">$135,000</h3>
              </div>
              <div className="bg-white p-2 rounded-lg shadow-sm">
                <TrendingUp className="w-6 h-6 text-indigo-500" />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
      
      <Card>
        <CardHeader>
          <CardTitle>Top Paying Companies</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {['Netflix', 'Stripe', 'Meta', 'Google', 'Airbnb'].map((company, index) => (
              <div key={company} className="flex items-center justify-between border-b border-gray-50 pb-4 last:border-0 last:pb-0">
                <div className="flex items-center gap-3">
                  <span className="text-sm font-bold text-gray-400 w-4">{index + 1}</span>
                  <span className="font-medium text-gray-900">{company}</span>
                </div>
                <span className="text-gray-600 font-medium">${(300000 - index * 20000).toLocaleString()} <span className="text-sm text-gray-400 font-normal">avg</span></span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

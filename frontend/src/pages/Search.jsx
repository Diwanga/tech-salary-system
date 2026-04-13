import React, { useState, useEffect } from 'react';
import { Search as SearchIcon, Filter } from 'lucide-react';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { SalaryCard } from '../components/SalaryCard';

// Dummy data for visual development
const DUMMY_DATA = [
  { id: 1, company: 'Google', role: 'Senior Software Engineer', salary: 250000, experience: 5, location: 'Mountain View, CA', votes: 124, userVote: 0 },
  { id: 2, company: 'Microsoft', role: 'Product Manager', salary: 180000, experience: 3, location: 'Redmond, WA', votes: 89, userVote: 1 },
  { id: 3, company: 'Meta', role: 'Frontend Engineer', salary: 210000, experience: 4, location: 'Menlo Park, CA', votes: 45, userVote: 0 },
  { id: 4, company: 'Amazon', role: 'Data Scientist', salary: 165000, experience: 2, location: 'Seattle, WA', votes: -5, userVote: -1 },
  { id: 5, company: 'Stripe', role: 'Backend Engineer', salary: 220000, experience: 3, location: 'Remote', votes: 230, userVote: 0 },
];

export const Search = () => {
  const [salaries, setSalaries] = useState(DUMMY_DATA);
  const [loading, setLoading] = useState(false);
  const [filters, setFilters] = useState({ query: '', company: '', role: '', experience: '' });
  const [showFilters, setShowFilters] = useState(false);

  // In a real app we'd trigger a search API call when filters change or form is submitted
  useEffect(() => {
    // const fetchSalaries = async () => {
    //   setLoading(true);
    //   try {
    //     const { data } = await api.get('/search', { params: filters });
    //     setSalaries(data);
    //   } catch (err) { ... }
    //   setLoading(false);
    // }
    // fetchSalaries();
    
    // For dummy logic:
    const filtered = DUMMY_DATA.filter(item => 
      item.company.toLowerCase().includes(filters.query.toLowerCase()) || 
      item.role.toLowerCase().includes(filters.query.toLowerCase())
    );
    setSalaries(filtered);
  }, [filters.query]);

  return (
    <div className="max-w-5xl mx-auto py-8 px-4 sm:px-6">
      <div className="mb-8 space-y-4">
        <h1 className="text-3xl font-bold text-gray-900 tracking-tight">Search Salaries</h1>
        <p className="text-gray-500">Find compensation data by company or role to know your worth.</p>
        
        <div className="flex gap-2">
          <div className="relative flex-1">
            <SearchIcon className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
            <Input 
              placeholder="Search companies, roles..." 
              className="pl-10 h-12 text-base shadow-sm"
              value={filters.query}
              onChange={(e) => setFilters({ ...filters, query: e.target.value })}
            />
          </div>
          <Button variant="outline" className="h-12 px-4 shadow-sm group" onClick={() => setShowFilters(!showFilters)}>
            <Filter className="w-5 h-5 text-gray-500 group-hover:text-gray-900" />
            <span className="hidden sm:inline ml-2">Filters</span>
          </Button>
        </div>

        {showFilters && (
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 p-4 bg-white rounded-xl border border-gray-200 shadow-sm animate-in fade-in slide-in-from-top-2 duration-200">
            <div>
              <label className="block text-xs font-medium text-gray-500 uppercase tracking-wider mb-1">Company</label>
              <Input placeholder="Any company" />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-500 uppercase tracking-wider mb-1">Role</label>
              <Input placeholder="Any role" />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-500 uppercase tracking-wider mb-1">Experience (Years)</label>
              <Input type="number" placeholder="Any" />
            </div>
          </div>
        )}
      </div>

      <div className="space-y-4">
        {loading ? (
          <div className="py-12 text-center text-gray-500">Loading results...</div>
        ) : salaries.length > 0 ? (
          salaries.map(salary => (
            <SalaryCard key={salary.id} data={salary} />
          ))
        ) : (
          <div className="py-12 text-center bg-gray-50 rounded-xl border border-gray-100 border-dashed">
            <h3 className="text-lg font-medium text-gray-900 mb-1">No results found</h3>
            <p className="text-gray-500 mb-4">Try adjusting your filters or search query.</p>
            <Button variant="outline" onClick={() => setFilters({ query: '', company: '', role: '', experience: '' })}>
              Clear all filters
            </Button>
          </div>
        )}
      </div>
    </div>
  );
};

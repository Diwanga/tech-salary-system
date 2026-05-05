import React, { useContext, useState } from 'react';
import { Card, CardContent } from './ui/Card';
import { Button } from './ui/Button';
import { AuthContext } from '../context/AuthContext';
import { ArrowUp, ArrowDown, MapPin, Briefcase } from 'lucide-react';
import { cn } from '../utils/cn';
import api from '../services/api';

export const SalaryCard = ({ data, onVote }) => {
  const { user } = useContext(AuthContext);
  const [votes, setVotes] = useState(Math.max(data.votes || 0, 0));
  const [status, setStatus] = useState(data.status || 'PENDING');
  const [voting, setVoting] = useState(false);

  const handleVote = async (dir) => {
    if (!user || voting) return;

    setVoting(true);
    try {
      const { data: result } = await api.post('/vote', {
        salaryId: data.id,
        vote: dir,
      });

      // Update from server response
      setVotes(Math.max(result.votes, 0));
      setStatus(result.status);

      if (onVote) onVote(data.id, result);
    } catch (err) {
      console.error('Vote failed:', err);
      alert(err.response?.data?.error || 'Vote failed');
    } finally {
      setVoting(false);
    }
  };

  const location = [data.city, data.country].filter(Boolean).join(', ');

  return (
    <Card className="hover:shadow-md transition-shadow group">
      <CardContent className="p-5">
        <div className="flex items-start justify-between">
          <div className="space-y-1">
            <h3 className="font-semibold text-lg text-gray-900 group-hover:text-primary-600 transition-colors">
              {data.jobTitle}
            </h3>
            <p className="font-medium text-gray-600 flex items-center gap-1.5 text-sm">
              <Briefcase className="w-4 h-4" /> {data.company || 'Anonymous'}
            </p>
            <div className="flex items-center gap-3 mt-3 text-sm text-gray-500">
              {data.experienceYears != null && (
                <span className="flex items-center gap-1 bg-gray-100 px-2 py-0.5 rounded-md">
                  {data.experienceYears} YOE
                </span>
              )}
              {data.level && (
                <span className="flex items-center gap-1 bg-gray-100 px-2 py-0.5 rounded-md">
                  {data.level}
                </span>
              )}
              {location && (
                <span className="flex items-center gap-1">
                  <MapPin className="w-4 h-4 text-gray-400" /> {location}
                </span>
              )}
            </div>
            {data.techStack && data.techStack.length > 0 && (
              <div className="flex flex-wrap gap-1 mt-2">
                {data.techStack.map((tech, i) => (
                  <span key={i} className="text-xs bg-primary-50 text-primary-700 px-2 py-0.5 rounded-md">{tech}</span>
                ))}
              </div>
            )}
          </div>
          
          <div className="text-right flex flex-col items-end gap-3">
            <div className="text-2xl font-bold tracking-tight text-gray-900">
              LKR {Number(data.grossSalary).toLocaleString()}
            </div>

            <span className={cn(
              "text-xs font-semibold px-2 py-0.5 rounded-full",
              status === 'APPROVED'
                ? "bg-green-100 text-green-700"
                : "bg-yellow-100 text-yellow-700"
            )}>
              {status}
            </span>
            
            <div className="flex items-center gap-2 bg-gray-50 rounded-lg p-1 border border-gray-100">
              <button
                onClick={() => handleVote(1)}
                disabled={!user || voting}
                className={cn(
                  "p-1.5 rounded-md transition-colors",
                  "text-gray-400 hover:text-gray-900 hover:bg-gray-200",
                  (!user || voting) && "opacity-50 cursor-not-allowed"
                )}
                title={!user ? "Login to vote" : "Upvote"}
              >
                <ArrowUp className="w-4 h-4" />
              </button>
              <span className={cn(
                "min-w-[1.5rem] text-center text-sm font-medium",
                votes > 0 ? "text-primary-600" : "text-gray-600"
              )}>
                {votes}
              </span>
              <button
                onClick={() => handleVote(-1)}
                disabled={!user || voting}
                className={cn(
                  "p-1.5 rounded-md transition-colors",
                  "text-gray-400 hover:text-gray-900 hover:bg-gray-200",
                  (!user || voting) && "opacity-50 cursor-not-allowed"
                )}
                title={!user ? "Login to vote" : "Downvote"}
              >
                <ArrowDown className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

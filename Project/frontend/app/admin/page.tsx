'use client';

import { useEffect, useState } from 'react';
import { reportApi } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import EventForm from '@/components/events/EventForm';

export default function AdminPage() {
  const { isAdmin } = useAuth();
  const [report, setReport] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    if (isAdmin) {
      fetchReport();
    }
  }, [isAdmin]);

  const fetchReport = async () => {
    try {
      const response = await reportApi.getOverallReport();
      setReport(response.data);
    } catch (error) {
      console.error('Error fetching report:', error);
    } finally {
      setLoading(false);
    }
  };

  if (!isAdmin) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center text-red-600">Access denied. Admin privileges required.</div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Loading...</div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
        >
          {showForm ? 'Hide Form' : 'Create Event'}
        </button>
      </div>

      {showForm && (
        <div className="mb-8">
          <EventForm onSuccess={() => {
            setShowForm(false);
            fetchReport();
          }} />
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-sm font-semibold text-gray-700 mb-2">Total Events</h3>
          <p className="text-3xl font-bold text-blue-600">{report?.totalEvents || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-sm font-semibold text-gray-700 mb-2">Total Bookings</h3>
          <p className="text-3xl font-bold text-green-600">{report?.totalBookings || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-sm font-semibold text-gray-700 mb-2">Total Income</h3>
          <p className="text-3xl font-bold text-purple-600">${report?.totalIncome?.toFixed(2) || '0.00'}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-sm font-semibold text-gray-700 mb-2">Active Events</h3>
          <p className="text-3xl font-bold text-orange-600">{report?.activeEvents || 0}</p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">System Analytics</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="p-4 bg-gray-50 rounded">
            <p className="text-sm text-gray-600">Completed Events</p>
            <p className="text-2xl font-semibold text-gray-900">{report?.completedEvents || 0}</p>
          </div>
          <div className="p-4 bg-gray-50 rounded">
            <p className="text-sm text-gray-600">Average Revenue per Event</p>
            <p className="text-2xl font-semibold text-gray-900">
              ${report?.totalEvents > 0 ? (report.totalIncome / report.totalEvents).toFixed(2) : '0.00'}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

'use client';

import { useAuth } from '@/contexts/AuthContext';
import { useEffect, useState } from 'react';
import { eventApi, bookingApi } from '@/lib/api';
import Link from 'next/link';

export default function DashboardPage() {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    totalEvents: 0,
    myBookings: 0,
    upcomingEvents: 0,
  });

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const [events, bookings, upcoming] = await Promise.all([
        eventApi.getAll(),
        bookingApi.getMyBookings(),
        eventApi.getUpcoming(),
      ]);

      setStats({
        totalEvents: events.data.length,
        myBookings: bookings.data.length,
        upcomingEvents: upcoming.data.length,
      });
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">
        Welcome, {user?.fullName}!
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-700 mb-2">Total Events</h3>
          <p className="text-4xl font-bold text-blue-600">{stats.totalEvents}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-700 mb-2">My Bookings</h3>
          <p className="text-4xl font-bold text-green-600">{stats.myBookings}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-700 mb-2">Upcoming Events</h3>
          <p className="text-4xl font-bold text-purple-600">{stats.upcomingEvents}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Link
          href="/events"
          className="bg-blue-600 text-white p-6 rounded-lg hover:bg-blue-700 transition-colors"
        >
          <h3 className="text-xl font-semibold mb-2">Browse Events</h3>
          <p className="text-blue-100">Explore upcoming events and trips</p>
        </Link>
        <Link
          href="/bookings"
          className="bg-green-600 text-white p-6 rounded-lg hover:bg-green-700 transition-colors"
        >
          <h3 className="text-xl font-semibold mb-2">My Bookings</h3>
          <p className="text-green-100">View and manage your bookings</p>
        </Link>
      </div>
    </div>
  );
}

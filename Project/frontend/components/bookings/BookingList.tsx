'use client';

import { useEffect, useState } from 'react';
import { bookingApi } from '@/lib/api';
import BookingCard from './BookingCard';

export default function BookingList() {
  const [bookings, setBookings] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const response = await bookingApi.getMyBookings();
      setBookings(response.data);
    } catch (error) {
      console.error('Error fetching bookings:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-center py-8">Loading your bookings...</div>;
  }

  if (bookings.length === 0) {
    return (
      <div className="text-center py-8 text-gray-600">
        No bookings yet. Browse events to make your first booking!
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {bookings.map((booking) => (
        <BookingCard key={booking.id} booking={booking} onUpdate={fetchBookings} />
      ))}
    </div>
  );
}

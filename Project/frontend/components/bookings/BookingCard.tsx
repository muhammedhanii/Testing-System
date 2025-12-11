'use client';

import { useState } from 'react';
import QRCode from 'react-qr-code';
import { bookingApi } from '@/lib/api';

interface BookingCardProps {
  booking: any;
  onUpdate: () => void;
}

export default function BookingCard({ booking, onUpdate }: BookingCardProps) {
  const [showQR, setShowQR] = useState(false);
  const [cancelling, setCancelling] = useState(false);

  const handleCancel = async () => {
    if (!confirm('Are you sure you want to cancel this booking?')) return;

    setCancelling(true);
    try {
      await bookingApi.cancel(booking.id);
      alert('Booking cancelled successfully');
      onUpdate();
    } catch (error: any) {
      alert(error.response?.data?.message || 'Cancellation failed');
    } finally {
      setCancelling(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'CONFIRMED':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      case 'ATTENDED':
        return 'bg-blue-100 text-blue-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <div className="flex justify-between items-start mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">{booking.event.title}</h3>
          <p className="text-sm text-gray-600">Booking Code: {booking.bookingCode}</p>
        </div>
        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(booking.status)}`}>
          {booking.status}
        </span>
      </div>

      <div className="space-y-2 text-sm text-gray-700 mb-4">
        <div>
          <strong>Date:</strong> {new Date(booking.event.startDate).toLocaleDateString()}
        </div>
        <div>
          <strong>Location:</strong> {booking.event.location}
        </div>
        <div>
          <strong>Amount Paid:</strong> ${booking.amountPaid}
        </div>
        <div>
          <strong>Booked On:</strong> {new Date(booking.bookingDate).toLocaleDateString()}
        </div>
      </div>

      <div className="flex gap-2">
        {booking.status === 'CONFIRMED' && (
          <>
            <button
              onClick={() => setShowQR(!showQR)}
              className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              {showQR ? 'Hide QR Code' : 'Show QR Code'}
            </button>
            <button
              onClick={handleCancel}
              disabled={cancelling}
              className="flex-1 bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 disabled:opacity-50"
            >
              {cancelling ? 'Cancelling...' : 'Cancel Booking'}
            </button>
          </>
        )}
      </div>

      {showQR && booking.qrCodePath && (
        <div className="mt-4 flex justify-center">
          <div className="p-4 bg-white border-2 border-gray-200 rounded-lg">
            <img 
              src={`data:image/png;base64,${booking.qrCodePath}`} 
              alt="QR Code" 
              className="w-48 h-48"
            />
            <p className="text-center text-sm text-gray-600 mt-2">Scan at entry</p>
          </div>
        </div>
      )}
    </div>
  );
}

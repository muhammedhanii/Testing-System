"use client";

import { useState } from "react";
import { bookingApi } from "@/lib/api";
import { useAuth } from "@/contexts/AuthContext";

interface EventCardProps {
  event: any;
  onUpdate: () => void;
}

export default function EventCard({ event, onUpdate }: EventCardProps) {
  const [booking, setBooking] = useState(false);
  const { isAuthenticated } = useAuth();

  // Normalize event data to handle both ActivityDTO and Event entity formats
  const eventData = {
    id: event.id || event.activityId,
    title: event.title || event.name,
    description: event.description,
    startDate: event.startDate || event.activityDate,
    location: event.location,
    type: event.type,
    price: event.price,
    capacity: event.capacity,
    availableSeats: event.availableSeats,
    imageUrl: event.imageUrl,
  };

  const handleBook = async () => {
    if (!isAuthenticated) {
      alert("Please login to book");
      return;
    }

    setBooking(true);
    try {
      if (!eventData.id) {
        throw new Error("Event ID not found");
      }
      await bookingApi.create(eventData.id);
      alert("Booking successful!");
      onUpdate();
    } catch (error: any) {
      alert(error.response?.data?.message || "Booking failed");
    } finally {
      setBooking(false);
    }
  };

  const formatDate = (date: string) => {
    return new Date(date).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="group bg-white/80 backdrop-blur-sm rounded-3xl shadow-xl border border-white/30 overflow-hidden hover:shadow-2xl hover:bg-white/90 transform hover:scale-[1.02] transition-all duration-300">
      {/* Image Section */}
      {eventData.imageUrl ? (
        <div className="relative overflow-hidden">
          <img
            src={eventData.imageUrl}
            alt={eventData.title}
            className="w-full h-56 object-cover group-hover:scale-110 transition-transform duration-500"
          />
        </div>
      ) : (
        <div className="w-full h-56 bg-gradient-to-br from-blue-400 to-slate-400 flex items-center justify-center">
          <svg
            className="w-16 h-16 text-white/80"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
            />
          </svg>
        </div>
      )}

      <div className="p-6 space-y-4">
        {/* Header */}
        <div className="flex items-start justify-between">
          <h3 className="text-xl font-bold text-gray-900 leading-tight group-hover:text-blue-700 transition-colors duration-300">
            {eventData.title}
          </h3>
          <span
            className={`px-3 py-1 text-xs font-semibold rounded-full ${
              eventData.type === "EVENT"
                ? "bg-blue-100 text-blue-800"
                : "bg-slate-100 text-slate-800"
            }`}
          >
            {eventData.type}
          </span>
        </div>

        {/* Description */}
        <p className="text-gray-600 leading-relaxed line-clamp-2">
          {eventData.description}
        </p>

        {/* Event Details */}
        <div className="space-y-3">
          <div className="flex items-center text-gray-700">
            <div className="w-8 h-8 bg-gradient-to-br from-blue-100 to-blue-200 rounded-lg flex items-center justify-center mr-3">
              <svg
                className="w-4 h-4 text-blue-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                />
              </svg>
            </div>
            <span className="font-medium">
              {formatDate(eventData.startDate)}
            </span>
          </div>

          <div className="flex items-center text-gray-700">
            <div className="w-8 h-8 bg-gradient-to-br from-emerald-100 to-emerald-200 rounded-lg flex items-center justify-center mr-3">
              <svg
                className="w-4 h-4 text-emerald-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                />
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                />
              </svg>
            </div>
            <span className="font-medium">{eventData.location}</span>
          </div>

          <div className="flex items-center text-gray-700">
            <div className="w-8 h-8 bg-gradient-to-br from-purple-100 to-purple-200 rounded-lg flex items-center justify-center mr-3">
              <svg
                className="w-4 h-4 text-purple-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"
                />
              </svg>
            </div>
            <span className="font-medium">
              <span
                className={
                  eventData.availableSeats < 10
                    ? "text-red-600 font-bold"
                    : "text-gray-700"
                }
              >
                {eventData.availableSeats}
              </span>
              <span className="text-gray-500">
                {" "}
                / {eventData.capacity} seats
              </span>
            </span>
          </div>

          <div className="flex items-center text-gray-700">
            <div className="w-8 h-8 bg-gradient-to-br from-pink-100 to-pink-200 rounded-lg flex items-center justify-center mr-3">
              <svg
                className="w-4 h-4 text-pink-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
            </div>
            <span className="font-bold text-lg text-gray-900">
              ${eventData.price}
            </span>
          </div>
        </div>

        {/* Availability Indicator */}
        {eventData.availableSeats < 10 && eventData.availableSeats > 0 && (
          <div className="bg-gradient-to-r from-orange-50 to-red-50 border border-orange-200 rounded-xl p-3">
            <div className="flex items-center">
              <svg
                className="w-5 h-5 text-orange-500 mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
                />
              </svg>
              <span className="text-sm font-semibold text-orange-800">
                Only {eventData.availableSeats} seats left!
              </span>
            </div>
          </div>
        )}

        {/* Book Button */}
        <button
          onClick={handleBook}
          disabled={booking || eventData.availableSeats === 0}
          className={`w-full py-3 px-6 rounded-xl font-semibold text-lg shadow-lg hover:shadow-xl transform hover:scale-[1.02] focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:cursor-not-allowed transition-all duration-300 ${
            eventData.availableSeats === 0
              ? "bg-gray-400 text-gray-200 cursor-not-allowed"
              : booking
              ? "bg-gray-500 text-white cursor-wait"
              : "bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500"
          }`}
        >
          <span className="flex items-center justify-center">
            {eventData.availableSeats === 0 ? (
              <>
                <svg
                  className="w-5 h-5 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728L5.636 5.636m12.728 12.728L18 21l-2.636-2.636M6 6l2.636 2.636"
                  />
                </svg>
                Sold Out
              </>
            ) : booking ? (
              <>
                <svg
                  className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  ></circle>
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
                Booking...
              </>
            ) : (
              <>
                <svg
                  className="w-5 h-5 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
                Book Now
              </>
            )}
          </span>
        </button>
      </div>
    </div>
  );
}

package com.aiu.trips.service;

import com.aiu.trips.dto.FeedbackDTO;
import com.aiu.trips.dto.ReportDTO;
import com.aiu.trips.dto.ReportFilterDTO;
import com.aiu.trips.dto.SystemStatisticsDTO;
import com.aiu.trips.enums.ExportFormat;
import com.aiu.trips.enums.ReportType;
import com.aiu.trips.model.Booking;
import com.aiu.trips.model.Event;
import com.aiu.trips.repository.BookingRepository;
import com.aiu.trips.repository.EventRepository;
import com.aiu.trips.service.interfaces.IReportsAnalytics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService implements IReportsAnalytics {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReportExportService reportExportService;

    public Map<String, Object> getEventReport(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Booking> bookings = bookingRepository.findByEvent_Id(eventId);

        Map<String, Object> report = new HashMap<>();
        report.put("eventId", event.getId());
        report.put("eventTitle", event.getTitle());
        report.put("totalCapacity", event.getCapacity());
        report.put("availableSeats", event.getAvailableSeats());
        report.put("bookedSeats", event.getCapacity() - event.getAvailableSeats());
        report.put("totalParticipants", bookings.size());
        report.put("totalIncome", bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getAmountPaid)
                .sum());
        report.put("cancelledBookings", bookings.stream()
                .filter(b -> "CANCELLED".equals(b.getStatus()))
                .count());

        return report;
    }

    public Map<String, Object> getOverallReport() {
        List<Event> events = eventRepository.findAll();
        List<Booking> bookings = bookingRepository.findAll();

        Map<String, Object> report = new HashMap<>();
        report.put("totalEvents", events.size());
        report.put("totalBookings", bookings.size());
        report.put("totalIncome", bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getAmountPaid)
                .sum());
        report.put("activeEvents", events.stream()
                .filter(e -> "ACTIVE".equals(e.getStatus()))
                .count());
        report.put("completedEvents", events.stream()
                .filter(e -> "COMPLETED".equals(e.getStatus()))
                .count());

        return report;
    }

    @Override
    public ReportDTO generateReport(ReportType reportType, ReportFilterDTO filters, ExportFormat format) {
        ReportDTO reportDTO = new ReportDTO();
        Map<String, Object> reportData;
        String reportTitle;

        // Generate report based on type
        if (reportType == ReportType.REVENUE) {
            reportData = getRevenueReport();
            reportTitle = "Revenue Report";
        } else if (reportType == ReportType.PARTICIPANTS) {
            reportData = getAttendanceReport();
            reportTitle = "Participants Report";
        } else if (reportType == ReportType.FEEDBACK) {
            if (filters != null && filters.getEventId() != null) {
                reportData = getEventReport(filters.getEventId());
                reportTitle = "Feedback Report - Event " + filters.getEventId();
            } else {
                reportData = getOverallReport();
                reportTitle = "Overall Feedback Report";
            }
        } else {
            reportData = getOverallReport();
            reportTitle = "General Report";
        }

        // Set report metadata
        reportDTO.setReportType(reportType);
        reportDTO.setData(reportData);
        reportDTO.setGeneratedAt(java.time.LocalDateTime.now());

        return reportDTO;
    }

    @Override
    public byte[] exportReport(Long reportId, ExportFormat format) {
        // For now, generate and export overall report
        // In a full implementation, you would store reports and retrieve by ID
        Map<String, Object> reportData = getOverallReport();
        String reportTitle = "AIU Trips & Events - Overall Report";
        
        return reportExportService.exportReport(reportData, format, reportTitle);
    }

    /**
     * Export a specific report to the given format
     */
    public byte[] exportReportData(Map<String, Object> reportData, ExportFormat format, String reportTitle) {
        return reportExportService.exportReport(reportData, format, reportTitle);
    }

    @Override
    public SystemStatisticsDTO getStatistics() {
        SystemStatisticsDTO stats = new SystemStatisticsDTO();
        
        List<Event> events = eventRepository.findAll();
        List<Booking> bookings = bookingRepository.findAll();

        stats.setTotalEvents(events.size());
        stats.setTotalBookings((long) bookings.size());
        stats.setActiveEvents(events.stream()
                .filter(e -> "ACTIVE".equals(e.getStatus()))
                .count());
        stats.setTotalRevenue(bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getAmountPaid)
                .sum());
        stats.setAverageAttendance(calculateAverageAttendance());

        return stats;
    }

    private Map<String, Object> getRevenueReport() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<String, Object> report = new HashMap<>();
        
        double totalRevenue = bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getAmountPaid)
                .sum();
        
        double averageBookingValue = bookings.isEmpty() ? 0 : 
                totalRevenue / bookings.stream()
                        .filter(b -> "CONFIRMED".equals(b.getStatus()))
                        .count();

        report.put("totalRevenue", totalRevenue);
        report.put("totalBookings", bookings.size());
        report.put("confirmedBookings", bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .count());
        report.put("averageBookingValue", averageBookingValue);
        report.put("cancelledRevenueLoss", bookings.stream()
                .filter(b -> "CANCELLED".equals(b.getStatus()))
                .mapToDouble(Booking::getAmountPaid)
                .sum());

        return report;
    }

    private Map<String, Object> getAttendanceReport() {
        List<Event> events = eventRepository.findAll();
        Map<String, Object> report = new HashMap<>();
        
        long totalCapacity = events.stream()
                .mapToLong(e -> e.getCapacity() != null ? e.getCapacity() : 0)
                .sum();
        
        long totalBooked = events.stream()
                .mapToLong(e -> {
                    int available = e.getAvailableSeats() != null ? e.getAvailableSeats() : 0;
                    int capacity = e.getCapacity() != null ? e.getCapacity() : 0;
                    return capacity - available;
                })
                .sum();

        double utilizationRate = totalCapacity > 0 ? 
                (double) totalBooked / totalCapacity * 100 : 0;

        report.put("totalCapacity", totalCapacity);
        report.put("totalBooked", totalBooked);
        report.put("utilizationRate", utilizationRate);
        report.put("averageAttendance", calculateAverageAttendance());
        report.put("totalEvents", events.size());

        return report;
    }

    private double calculateAverageAttendance() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            return 0;
        }

        long totalBooked = events.stream()
                .mapToLong(e -> {
                    int available = e.getAvailableSeats() != null ? e.getAvailableSeats() : 0;
                    int capacity = e.getCapacity() != null ? e.getCapacity() : 0;
                    return capacity - available;
                })
                .sum();

        return (double) totalBooked / events.size();
    }

    @Override
    public void collectFeedback(FeedbackDTO feedbackData) {
        // Placeholder implementation
    }
}

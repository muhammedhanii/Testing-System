package com.aiu.trips.controller;

import com.aiu.trips.chain.RequestHandler;
import com.aiu.trips.command.*;
import com.aiu.trips.enums.ExportFormat;
import com.aiu.trips.service.AdvancedAnalyticsService;
import com.aiu.trips.service.ReportService;
import com.aiu.trips.service.interfaces.IReportsAnalytics;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ReportController - Uses Command Pattern and Chain of Responsibility for all
 * operations
 */
@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {

    @Autowired
    private ControllerCommandInvoker commandInvoker;

    @Autowired
    private IReportsAnalytics reportService;

    @Autowired
    private ReportService reportServiceImpl;

    @Autowired
    private AdvancedAnalyticsService analyticsService;

    @Autowired
    @Qualifier("requestHandlerChain")
    private RequestHandler handlerChain;

    @PostMapping("/generate")
    public ResponseEntity<?> generateReport(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new GenerateReportCommand(reportService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/overall")
    public ResponseEntity<?> getOverallReport(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(reportService.getOverallReport());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating overall report: " + e.getMessage());
        }
    }

    @GetMapping("/export/overall")
    public ResponseEntity<byte[]> exportOverallReport(
            @RequestParam(defaultValue = "PDF") String format,
            HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            
            ExportFormat exportFormat = ExportFormat.valueOf(format.toUpperCase());
            Map<String, Object> reportData = reportServiceImpl.getOverallReport();
            byte[] exportedData = reportServiceImpl.exportReportData(
                    reportData, exportFormat, "AIU Trips & Events - Overall Report");

            HttpHeaders headers = new HttpHeaders();
            String filename = "overall_report_" + System.currentTimeMillis();
            
            switch (exportFormat) {
                case PDF:
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", filename + ".pdf");
                    break;
                case CSV:
                    headers.setContentType(MediaType.parseMediaType("text/csv"));
                    headers.setContentDispositionFormData("attachment", filename + ".csv");
                    break;
                case JSON:
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setContentDispositionFormData("attachment", filename + ".json");
                    break;
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(exportedData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error exporting report: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/export/event/{eventId}")
    public ResponseEntity<byte[]> exportEventReport(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "PDF") String format,
            HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            
            ExportFormat exportFormat = ExportFormat.valueOf(format.toUpperCase());
            Map<String, Object> reportData = reportServiceImpl.getEventReport(eventId);
            String reportTitle = "Event Report - " + reportData.get("eventTitle");
            byte[] exportedData = reportServiceImpl.exportReportData(reportData, exportFormat, reportTitle);

            HttpHeaders headers = new HttpHeaders();
            String filename = "event_" + eventId + "_report_" + System.currentTimeMillis();
            
            switch (exportFormat) {
                case PDF:
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", filename + ".pdf");
                    break;
                case CSV:
                    headers.setContentType(MediaType.parseMediaType("text/csv"));
                    headers.setContentDispositionFormData("attachment", filename + ".csv");
                    break;
                case JSON:
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setContentDispositionFormData("attachment", filename + ".json");
                    break;
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(exportedData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error exporting event report: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/analytics/trends")
    public ResponseEntity<?> getBookingTrends(
            @RequestParam(defaultValue = "30") int days,
            HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(analyticsService.getBookingTrends(days));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating trends: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/forecast")
    public ResponseEntity<?> getRevenueForecast(
            @RequestParam(defaultValue = "30") int days,
            HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(analyticsService.forecastRevenue(days));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating forecast: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/categories")
    public ResponseEntity<?> getPopularCategories(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(analyticsService.getPopularCategories());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error analyzing categories: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/attendance")
    public ResponseEntity<?> getAttendancePatterns(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(analyticsService.getAttendancePatterns());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error analyzing attendance: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/peak-periods")
    public ResponseEntity<?> getPeakBookingPeriods(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(analyticsService.getPeakBookingPeriods());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error analyzing peak periods: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/comprehensive")
    public ResponseEntity<?> getComprehensiveAnalytics(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            return ResponseEntity.ok(analyticsService.getComprehensiveAnalytics());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating comprehensive analytics: " + e.getMessage());
        }
    }
}

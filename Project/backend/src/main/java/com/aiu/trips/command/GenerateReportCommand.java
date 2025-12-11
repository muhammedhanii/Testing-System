package com.aiu.trips.command;

import com.aiu.trips.dto.ReportDTO;
import com.aiu.trips.dto.ReportFilterDTO;
import com.aiu.trips.enums.ExportFormat;
import com.aiu.trips.enums.ReportType;
import com.aiu.trips.service.interfaces.IReportsAnalytics;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class GenerateReportCommand implements IControllerCommand {
    private final IReportsAnalytics reportService;

    public GenerateReportCommand(IReportsAnalytics reportService) {
        this.reportService = reportService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            ReportType type = ReportType.valueOf((String) requestData.getOrDefault("reportType", "PARTICIPANTS"));
            ExportFormat format = ExportFormat.valueOf((String) requestData.getOrDefault("format", "PDF"));
            ReportFilterDTO filters = new ReportFilterDTO();
            
            ReportDTO result = reportService.generateReport(type, filters, format);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.aiu.trips.service.interfaces;

import com.aiu.trips.dto.FeedbackDTO;
import com.aiu.trips.dto.ReportDTO;
import com.aiu.trips.dto.ReportFilterDTO;
import com.aiu.trips.dto.SystemStatisticsDTO;
import com.aiu.trips.enums.ExportFormat;
import com.aiu.trips.enums.ReportType;

/**
 * IReportsAnalytics interface as per Controller.pu diagram
 */
public interface IReportsAnalytics {
    ReportDTO generateReport(ReportType reportType, ReportFilterDTO filters, ExportFormat format);

    byte[] exportReport(Long reportId, ExportFormat format);

    SystemStatisticsDTO getStatistics();

    void collectFeedback(FeedbackDTO feedbackData);

    java.util.Map<String, Object> getOverallReport();
}

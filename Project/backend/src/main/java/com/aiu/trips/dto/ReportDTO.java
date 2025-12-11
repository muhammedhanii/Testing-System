package com.aiu.trips.dto;

import com.aiu.trips.enums.ExportFormat;
import com.aiu.trips.enums.ReportType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * ReportDTO for transferring report data
 */
public class ReportDTO {
    private Long reportId;
    private ReportType reportType;
    private String filePath;
    private String description;
    private ExportFormat format;
    private byte[] data;
    private Map<String, Object> reportData;
    private LocalDateTime generatedAt;

    public ReportDTO() {}

    // Getters and Setters
    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ExportFormat getFormat() { return format; }
    public void setFormat(ExportFormat format) { this.format = format; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public Map<String, Object> getReportData() { return reportData; }
    public void setData(Map<String, Object> reportData) { this.reportData = reportData; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}

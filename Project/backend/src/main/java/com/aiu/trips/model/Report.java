package com.aiu.trips.model;

import com.aiu.trips.enums.ExportFormat;
import com.aiu.trips.enums.ReportType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Report entity as per Data_Layer.pu diagram
 */
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column
    private String filePath;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Long generatedBy;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportFormat format;

    @PrePersist
    protected void onCreate() {
        generatedDate = LocalDateTime.now();
    }

    // Constructors
    public Report() {}

    public Report(ReportType type, String filePath, String description, Long generatedBy, ExportFormat format) {
        this.type = type;
        this.filePath = filePath;
        this.description = description;
        this.generatedBy = generatedBy;
        this.format = format;
    }

    // Getters and Setters
    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(Long generatedBy) {
        this.generatedBy = generatedBy;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public void setFormat(ExportFormat format) {
        this.format = format;
    }
}

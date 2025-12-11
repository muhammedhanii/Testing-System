package com.aiu.trips.dto;

/**
 * ReportFilterDTO for filtering reports
 */
public class ReportFilterDTO {
    private String startDate;
    private String endDate;
    private Long activityId;
    private Long eventId;
    private String category;

    public ReportFilterDTO() {}

    // Getters and Setters
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

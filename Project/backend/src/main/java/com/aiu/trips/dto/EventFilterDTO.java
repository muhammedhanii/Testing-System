package com.aiu.trips.dto;

import com.aiu.trips.enums.ActivityStatus;
import com.aiu.trips.enums.ActivityType;

/**
 * EventFilterDTO for filtering events
 */
public class EventFilterDTO {
    private ActivityType type;
    private ActivityStatus status;
    private String location;
    private String startDate;
    private String endDate;

    public EventFilterDTO() {}

    // Getters and Setters
    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }

    public ActivityStatus getStatus() { return status; }
    public void setStatus(ActivityStatus status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}

package com.aiu.trips.builder;

import com.aiu.trips.dto.ActivityDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * IActivityBuilder interface as per Event_Management.pu diagram
 * Builder Pattern - Interface for building activities
 */
public interface IActivityBuilder {
    void reset();
    void setBasicInfo(ActivityDTO data);
    void setSchedule(LocalDateTime date, String location);
    void setCapacity(Integer capacity);
    void setPricing(BigDecimal price);
    void setOrganizer(Long organizerId);
    void setDetails();
    ActivityDTO getResult();
}

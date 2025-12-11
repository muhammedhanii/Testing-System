package com.aiu.trips.builder;

import com.aiu.trips.dto.ActivityDTO;
import com.aiu.trips.enums.ActivityType;
import com.aiu.trips.factory.IModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TripBuilder as per Event_Management.pu diagram
 * Builder Pattern - Concrete builder for trips
 */
@Component
public class TripBuilder implements IActivityBuilder {

    @Autowired
    private IModelFactory modelFactory;

    private ActivityDTO activity;

    @Override
    public void reset() {
        activity = new ActivityDTO();
        activity.setType(ActivityType.TRIP);
    }

    @Override
    public void setBasicInfo(ActivityDTO data) {
        if (activity == null) reset();
        activity.setName(data.getName());
        activity.setDescription(data.getDescription());
        activity.setCategory(data.getCategory());
        
        // Trip-specific fields
        activity.setDestination(data.getDestination());
        activity.setDurationDays(data.getDurationDays());
        activity.setTransportMode(data.getTransportMode());
        activity.setStartLocation(data.getStartLocation());
        activity.setEndLocation(data.getEndLocation());
        activity.setItinerary(data.getItinerary());
    }

    @Override
    public void setSchedule(LocalDateTime date, String location) {
        if (activity == null) reset();
        activity.setActivityDate(date);
        activity.setLocation(location);
    }

    @Override
    public void setCapacity(Integer capacity) {
        if (activity == null) reset();
        activity.setCapacity(capacity);
        activity.setAvailableSeats(capacity);
    }

    @Override
    public void setPricing(BigDecimal price) {
        if (activity == null) reset();
        activity.setPrice(price);
    }

    @Override
    public void setOrganizer(Long organizerId) {
        if (activity == null) reset();
        activity.setOrganizerId(organizerId);
    }

    @Override
    public void setDetails() {
        // Additional trip-specific setup if needed
    }

    @Override
    public ActivityDTO getResult() {
        ActivityDTO result = activity;
        reset(); // Reset for next build
        return result;
    }
}

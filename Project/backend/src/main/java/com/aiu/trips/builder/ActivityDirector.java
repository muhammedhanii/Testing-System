package com.aiu.trips.builder;

import com.aiu.trips.dto.ActivityDTO;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * ActivityDirector as per Event_Management.pu diagram
 * Builder Pattern - Director that orchestrates the building process
 */
@Component
public class ActivityDirector implements IActivityDirector {

    private IActivityBuilder builder;

    @Override
    public void setBuilder(IActivityBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ActivityDTO constructFrom(ActivityDTO data) {
        builder.reset();
        builder.setBasicInfo(data);
        builder.setSchedule(data.getActivityDate(), data.getLocation());
        builder.setCapacity(data.getCapacity());
        builder.setPricing(data.getPrice());
        builder.setOrganizer(data.getOrganizerId());
        builder.setDetails();
        return builder.getResult();
    }

    @Override
    public ActivityDTO makeNormalEvent(ActivityDTO data) {
        builder.reset();
        builder.setBasicInfo(data);
        builder.setSchedule(data.getActivityDate(), data.getLocation());
        builder.setCapacity(50); // Default capacity
        builder.setPricing(data.getPrice() != null ? data.getPrice() : BigDecimal.ZERO);
        builder.setOrganizer(data.getOrganizerId());
        builder.setDetails();
        return builder.getResult();
    }

    @Override
    public ActivityDTO makeNormalTrip(ActivityDTO data) {
        builder.reset();
        builder.setBasicInfo(data);
        builder.setSchedule(data.getActivityDate(), data.getLocation());
        builder.setCapacity(30); // Default capacity for trips
        builder.setPricing(data.getPrice() != null ? data.getPrice() : BigDecimal.ZERO);
        builder.setOrganizer(data.getOrganizerId());
        builder.setDetails();
        return builder.getResult();
    }

    @Override
    public ActivityDTO makeHighCapacityEvent(ActivityDTO data, Integer capacity) {
        builder.reset();
        builder.setBasicInfo(data);
        builder.setSchedule(data.getActivityDate(), data.getLocation());
        builder.setCapacity(capacity);
        builder.setPricing(data.getPrice() != null ? data.getPrice() : BigDecimal.ZERO);
        builder.setOrganizer(data.getOrganizerId());
        builder.setDetails();
        return builder.getResult();
    }

    @Override
    public ActivityDTO makeHighCapacityTrip(ActivityDTO data, Integer capacity) {
        builder.reset();
        builder.setBasicInfo(data);
        builder.setSchedule(data.getActivityDate(), data.getLocation());
        builder.setCapacity(capacity);
        builder.setPricing(data.getPrice() != null ? data.getPrice() : BigDecimal.ZERO);
        builder.setOrganizer(data.getOrganizerId());
        builder.setDetails();
        return builder.getResult();
    }
}

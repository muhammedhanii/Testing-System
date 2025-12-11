package com.aiu.trips.builder;

import com.aiu.trips.dto.ActivityDTO;

/**
 * IActivityDirector interface as per Event_Management.pu diagram
 * Builder Pattern - Director interface
 */
public interface IActivityDirector {
    void setBuilder(IActivityBuilder builder);
    ActivityDTO constructFrom(ActivityDTO data);
    ActivityDTO makeNormalEvent(ActivityDTO data);
    ActivityDTO makeNormalTrip(ActivityDTO data);
    ActivityDTO makeHighCapacityEvent(ActivityDTO data, Integer capacity);
    ActivityDTO makeHighCapacityTrip(ActivityDTO data, Integer capacity);
}

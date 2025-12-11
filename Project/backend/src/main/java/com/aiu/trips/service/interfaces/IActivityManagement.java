package com.aiu.trips.service.interfaces;

import com.aiu.trips.dto.ActivityDTO;
import com.aiu.trips.enums.ActivityType;
import com.aiu.trips.model.Event;
import java.util.List;

/**
 * IActivityManagement interface as per Controller.pu diagram
 */
public interface IActivityManagement {
    ActivityDTO createActivity(ActivityDTO data, ActivityType type);

    ActivityDTO updateActivity(Long id, ActivityDTO data);

    void deleteActivity(Long id);

    List<ActivityDTO> getAllActivities();

    Integer manageCapacity(Long activityId);

    // Additional methods for event operations
    List<Event> getUpcomingEvents();
}

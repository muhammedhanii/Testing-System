package com.aiu.trips.state;

import com.aiu.trips.enums.ActivityStatus;
import com.aiu.trips.model.Activity;

/**
 * UpcomingState as per Event_Management.pu diagram
 * State Pattern - Represents upcoming activity state
 */
public class UpcomingState implements ActivityState {

    @Override
    public Activity publish(Activity activity) {
        // Already in upcoming state
        return activity;
    }

    @Override
    public Activity complete(Activity activity) {
        activity.setStatus(ActivityStatus.COMPLETED);
        return activity;
    }

    @Override
    public Activity cancel(Activity activity) {
        activity.setStatus(ActivityStatus.CANCELLED);
        return activity;
    }
}

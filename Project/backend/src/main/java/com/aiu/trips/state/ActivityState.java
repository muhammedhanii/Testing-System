package com.aiu.trips.state;

import com.aiu.trips.model.Activity;

/**
 * ActivityState interface as per Event_Management.pu diagram
 * State Pattern - Interface for activity states
 */
public interface ActivityState {
    Activity publish(Activity activity);
    Activity complete(Activity activity);
    Activity cancel(Activity activity);
}

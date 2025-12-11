package com.aiu.trips.state;

import com.aiu.trips.model.Activity;

/**
 * CancelledState as per Event_Management.pu diagram
 * State Pattern - Represents cancelled activity state
 */
public class CancelledState implements ActivityState {

    @Override
    public Activity publish(Activity activity) {
        throw new IllegalStateException("Cannot publish a cancelled activity");
    }

    @Override
    public Activity complete(Activity activity) {
        throw new IllegalStateException("Cannot complete a cancelled activity");
    }

    @Override
    public Activity cancel(Activity activity) {
        // Already cancelled
        return activity;
    }
}

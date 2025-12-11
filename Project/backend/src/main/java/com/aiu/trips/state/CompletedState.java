package com.aiu.trips.state;

import com.aiu.trips.model.Activity;

/**
 * CompletedState as per Event_Management.pu diagram
 * State Pattern - Represents completed activity state
 */
public class CompletedState implements ActivityState {

    @Override
    public Activity publish(Activity activity) {
        throw new IllegalStateException("Cannot publish a completed activity");
    }

    @Override
    public Activity complete(Activity activity) {
        // Already completed
        return activity;
    }

    @Override
    public Activity cancel(Activity activity) {
        throw new IllegalStateException("Cannot cancel a completed activity");
    }
}

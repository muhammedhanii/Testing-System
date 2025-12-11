package com.aiu.trips.state;

import com.aiu.trips.enums.ActivityStatus;
import com.aiu.trips.model.Activity;
import org.springframework.stereotype.Component;

/**
 * ActivityLifecycle as per Event_Management.pu diagram
 * State Pattern - Context that encapsulates valid transitions
 */
@Component
public class ActivityLifecycle {

    private ActivityState state;

    public ActivityLifecycle() {
        this.state = new UpcomingState();
    }

    public void setState(ActivityState state) {
        this.state = state;
    }

    public ActivityState getState() {
        return state;
    }

    public Activity publish(Activity activity) {
        Activity result = state.publish(activity);
        updateStateFromActivity(activity);
        return result;
    }

    public Activity complete(Activity activity) {
        Activity result = state.complete(activity);
        updateStateFromActivity(activity);
        return result;
    }

    public Activity cancel(Activity activity) {
        Activity result = state.cancel(activity);
        updateStateFromActivity(activity);
        return result;
    }

    /**
     * Update internal state based on activity status
     */
    private void updateStateFromActivity(Activity activity) {
        if (activity.getStatus() == ActivityStatus.UPCOMING) {
            this.state = new UpcomingState();
        } else if (activity.getStatus() == ActivityStatus.COMPLETED) {
            this.state = new CompletedState();
        } else if (activity.getStatus() == ActivityStatus.CANCELLED) {
            this.state = new CancelledState();
        }
    }

    /**
     * Initialize state from activity
     */
    public void initializeFromActivity(Activity activity) {
        updateStateFromActivity(activity);
    }
}

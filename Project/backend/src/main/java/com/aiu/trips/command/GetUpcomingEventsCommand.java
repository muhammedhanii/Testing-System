package com.aiu.trips.command;

import com.aiu.trips.command.IControllerCommand;
import com.aiu.trips.service.interfaces.IActivityManagement;
import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * GetUpcomingEventsCommand - Implements Command Pattern for retrieving upcoming
 * events
 */
public class GetUpcomingEventsCommand implements IControllerCommand {

    private final IActivityManagement activityService;

    public GetUpcomingEventsCommand(IActivityManagement activityService) {
        this.activityService = activityService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> data) {
        try {
            return ResponseEntity.ok(activityService.getUpcomingEvents());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.aiu.trips.command;

import com.aiu.trips.service.interfaces.IActivityManagement;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class GetAllActivitiesCommand implements IControllerCommand {
    private final IActivityManagement activityService;

    public GetAllActivitiesCommand(IActivityManagement activityService) {
        this.activityService = activityService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            return ResponseEntity.ok(activityService.getAllActivities());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

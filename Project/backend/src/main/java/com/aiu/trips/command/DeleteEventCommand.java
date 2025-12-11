package com.aiu.trips.command;

import com.aiu.trips.service.interfaces.IActivityManagement;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class DeleteEventCommand implements IControllerCommand {
    private final IActivityManagement activityService;

    public DeleteEventCommand(IActivityManagement activityService) {
        this.activityService = activityService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            Long id = Long.valueOf(requestData.get("id").toString());
            activityService.deleteActivity(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

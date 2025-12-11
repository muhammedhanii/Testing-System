package com.aiu.trips.command;

import com.aiu.trips.dto.ActivityDTO;
import com.aiu.trips.service.interfaces.IActivityManagement;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class UpdateEventCommand implements IControllerCommand {
    private final IActivityManagement activityService;

    public UpdateEventCommand(IActivityManagement activityService) {
        this.activityService = activityService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            Long id = Long.valueOf(requestData.get("id").toString());
            ActivityDTO activityDTO = mapToActivityDTO(requestData);
            ActivityDTO result = activityService.updateActivity(id, activityDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ActivityDTO mapToActivityDTO(Map<String, Object> data) {
        ActivityDTO dto = new ActivityDTO();
        if (data.containsKey("name")) dto.setName((String) data.get("name"));
        if (data.containsKey("description")) dto.setDescription((String) data.get("description"));
        if (data.containsKey("location")) dto.setLocation((String) data.get("location"));
        return dto;
    }
}

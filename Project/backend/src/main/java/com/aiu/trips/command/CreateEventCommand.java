package com.aiu.trips.command;

import com.aiu.trips.dto.ActivityDTO;
import com.aiu.trips.enums.ActivityType;
import com.aiu.trips.service.interfaces.IActivityManagement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class CreateEventCommand implements IControllerCommand {
    private final IActivityManagement activityService;

    public CreateEventCommand(IActivityManagement activityService) {
        this.activityService = activityService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            ActivityDTO activityDTO = mapToActivityDTO(requestData);
            ActivityType type = ActivityType.valueOf((String) requestData.getOrDefault("type", "EVENT"));
            ActivityDTO result = activityService.createActivity(activityDTO, type);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ActivityDTO mapToActivityDTO(Map<String, Object> data) {
        ActivityDTO dto = new ActivityDTO();
        // Map title as name (frontend sends 'title', backend expects 'name')
        dto.setName((String) data.getOrDefault("title", data.get("name")));
        dto.setDescription((String) data.get("description"));
        dto.setLocation((String) data.get("location"));
        dto.setPrice(convertToLong(data.get("price")));
        // Set capacity for available seats initialization
        Object capacityObj = data.get("capacity");
        if (capacityObj != null) {
            int capacity = (capacityObj instanceof Integer integer) ? integer
                    : Integer.parseInt(capacityObj.toString());
            dto.setCapacity(capacity);
        }
        // Map startDate (frontend sends 'startDate' as ISO string)
        Object startDateObj = data.get("startDate");
        if (startDateObj != null) {
            dto.setActivityDate(parseDateTime(startDateObj.toString()));
        }
        return dto;
    }

    private java.time.LocalDateTime parseDateTime(String dateString) {
        try {
            // Handle ISO format: "2025-12-12T18:41"
            if (dateString.contains("T")) {
                return java.time.LocalDateTime.parse(dateString);
            }
            // Fallback for other formats
            return java.time.LocalDateTime.now();
        } catch (Exception ignored) {
            return java.time.LocalDateTime.now();
        }
    }

    private java.math.BigDecimal convertToLong(Object value) {
        if (value == null)
            return java.math.BigDecimal.ZERO;
        if (value instanceof java.math.BigDecimal bd)
            return bd;
        if (value instanceof Long l)
            return java.math.BigDecimal.valueOf(l);
        if (value instanceof Integer i)
            return java.math.BigDecimal.valueOf(i.longValue());
        if (value instanceof Double d)
            return java.math.BigDecimal.valueOf(d);
        return new java.math.BigDecimal(value.toString());
    }
}

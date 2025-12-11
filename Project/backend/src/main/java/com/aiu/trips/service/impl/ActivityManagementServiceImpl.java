package com.aiu.trips.service.impl;

import com.aiu.trips.builder.ActivityDirector;
import com.aiu.trips.builder.EventBuilder;
import com.aiu.trips.builder.IActivityBuilder;
import com.aiu.trips.builder.TripBuilder;
import com.aiu.trips.dto.ActivityDTO;
import com.aiu.trips.enums.ActivityStatus;
import com.aiu.trips.enums.ActivityType;
import com.aiu.trips.model.Event;
import com.aiu.trips.repository.EventRepository;
import com.aiu.trips.service.EventService;
import com.aiu.trips.service.interfaces.IActivityManagement;
import com.aiu.trips.state.ActivityLifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ActivityManagementService - Uses Builder, State, and Factory patterns
 */
@Service
public class ActivityManagementServiceImpl implements IActivityManagement {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ActivityDirector activityDirector;

    @Autowired
    private EventBuilder eventBuilder;

    @Autowired
    private TripBuilder tripBuilder;

    @Autowired
    private ActivityLifecycle activityLifecycle;

    @Autowired
    private EventService eventService;

    @Override
    @Transactional
    public ActivityDTO createActivity(ActivityDTO data, ActivityType type) {
        // Use Builder Pattern via Director
        IActivityBuilder builder = (type == ActivityType.EVENT) ? eventBuilder : tripBuilder;
        activityDirector.setBuilder(builder);

        ActivityDTO builtActivity = activityDirector.constructFrom(data);

        // Convert DTO to Entity and save
        Event entity = convertToEntity(builtActivity);
        entity = eventRepository.save(entity);

        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public ActivityDTO updateActivity(Long id, ActivityDTO data) {
        Event entity = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found: " + id));

        updateEntityFromDTO(entity, data);
        entity = eventRepository.save(entity);
        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        Event entity = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found: " + id));

        entity.setStatus(com.aiu.trips.enums.EventStatus.CANCELLED);
        eventRepository.save(entity);
    }

    @Override
    public List<ActivityDTO> getAllActivities() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer manageCapacity(Long activityId) {
        Event entity = eventRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found: " + activityId));
        return entity.getAvailableSeats();
    }

    private Event convertToEntity(ActivityDTO dto) {
        Event event = new Event();
        event.setTitle(dto.getName());
        event.setDescription(dto.getDescription());
        event.setStartDate(dto.getActivityDate());
        event.setLocation(dto.getLocation());
        event.setCapacity(dto.getCapacity());
        event.setAvailableSeats(dto.getCapacity()); // Initialize available seats to capacity
        event.setPrice(dto.getPrice() != null ? dto.getPrice().doubleValue() : 0.0);
        event.setType(com.aiu.trips.enums.EventType.valueOf(dto.getType().name()));
        event.setStatus(com.aiu.trips.enums.EventStatus.ACTIVE);
        return event;
    }

    private void updateEntityFromDTO(Event entity, ActivityDTO dto) {
        entity.setTitle(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getActivityDate());
        entity.setLocation(dto.getLocation());
        entity.setCapacity(dto.getCapacity());
        entity.setPrice(dto.getPrice() != null ? dto.getPrice().doubleValue() : entity.getPrice());
    }

    private ActivityDTO convertToDTO(Event entity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setActivityId(entity.getId());
        dto.setName(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setActivityDate(entity.getStartDate());
        dto.setLocation(entity.getLocation());
        dto.setCapacity(entity.getCapacity());
        dto.setAvailableSeats(entity.getAvailableSeats());
        dto.setPrice(java.math.BigDecimal.valueOf(entity.getPrice()));
        dto.setType(ActivityType.valueOf(entity.getType().name()));
        return dto;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }
}

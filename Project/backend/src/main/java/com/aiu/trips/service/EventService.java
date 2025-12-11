package com.aiu.trips.service;

import com.aiu.trips.constants.AppConstants;
import com.aiu.trips.enums.EventStatus;
import com.aiu.trips.enums.EventType;
import com.aiu.trips.exception.ResourceNotFoundException;
import com.aiu.trips.model.Event;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.EventRepository;
import com.aiu.trips.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public Event createEvent(Event event, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));
        
        event.setCreatedBy(user);
        Event savedEvent = eventRepository.save(event);
        
        // Notify all users about new event
        notificationService.notifyAllUsers(
            "New " + event.getType().name().toLowerCase() + " available: " + event.getTitle(),
            "INFO"
        );
        
        return savedEvent;
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EVENT_NOT_FOUND + id));
        
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setType(eventDetails.getType());
        event.setStartDate(eventDetails.getStartDate());
        event.setEndDate(eventDetails.getEndDate());
        event.setLocation(eventDetails.getLocation());
        event.setPrice(eventDetails.getPrice());
        event.setCapacity(eventDetails.getCapacity());
        event.setImageUrl(eventDetails.getImageUrl());
        
        Event updatedEvent = eventRepository.save(event);
        
        // Notify users about update
        notificationService.notifyEventParticipants(
            id,
            "Event updated: " + event.getTitle(),
            "INFO"
        );
        
        return updatedEvent;
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EVENT_NOT_FOUND + id));
        
        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
        
        // Notify participants about cancellation
        notificationService.notifyEventParticipants(
            id,
            "Event cancelled: " + event.getTitle(),
            "WARNING"
        );
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EVENT_NOT_FOUND + id));
    }

    public List<Event> getEventsByType(EventType type) {
        return eventRepository.findByType(type);
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByStartDateAfter(LocalDateTime.now());
    }

    public List<Event> getEventsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));
        return eventRepository.findByCreatedBy_Id(user.getId());
    }
}

package com.aiu.trips.dto;

import com.aiu.trips.enums.ActivityCategory;
import com.aiu.trips.enums.ActivityType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ActivityDTO for transferring activity data
 */
public class ActivityDTO {
    private Long activityId;
    private String name;
    private String description;
    private LocalDateTime activityDate;
    private String location;
    private Integer capacity;
    private Integer availableSeats;
    private BigDecimal price;
    private ActivityCategory category;
    private ActivityType type;
    private Long organizerId;
    
    // Event-specific fields
    private List<String> speakers;
    private String topic;
    private String venue;
    private String agenda;
    
    // Trip-specific fields
    private String destination;
    private Integer durationDays;
    private String transportMode;
    private String startLocation;
    private String endLocation;
    private String itinerary;

    // Constructors
    public ActivityDTO() {}

    // Getters and Setters
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDateTime activityDate) { this.activityDate = activityDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public ActivityCategory getCategory() { return category; }
    public void setCategory(ActivityCategory category) { this.category = category; }

    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }

    public List<String> getSpeakers() { return speakers; }
    public void setSpeakers(List<String> speakers) { this.speakers = speakers; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getAgenda() { return agenda; }
    public void setAgenda(String agenda) { this.agenda = agenda; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }

    public String getItinerary() { return itinerary; }
    public void setItinerary(String itinerary) { this.itinerary = itinerary; }
}

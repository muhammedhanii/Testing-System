package com.aiu.trips.model;

import com.aiu.trips.enums.EventStatus;
import com.aiu.trips.enums.EventType;
import jakarta.persistence.*;
// Lombok temporarily removed due to Java 25 compatibility
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    
    public Event() {}
    
    public Event(Long id, String title, String description, EventType type, LocalDateTime startDate, LocalDateTime endDate, String location, Double price, Integer capacity, Integer availableSeats, String imageUrl, User createdBy, LocalDateTime createdAt, EventStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.price = price;
        this.capacity = capacity;
        this.availableSeats = availableSeats;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.status = status;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type; // EVENT, TRIP
    
    @Column(nullable = false)
    private LocalDateTime startDate;
    
    @Column
    private LocalDateTime endDate;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    private Integer availableSeats;
    
    @Column
    private String imageUrl;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column
    private LocalDateTime createdAt;
    
    @Column
    @Enumerated(EnumType.STRING)
    private EventStatus status; // ACTIVE, CANCELLED, COMPLETED
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        availableSeats = capacity;
        status = EventStatus.ACTIVE;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }
}

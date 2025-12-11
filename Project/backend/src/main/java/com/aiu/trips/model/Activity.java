package com.aiu.trips.model;

import com.aiu.trips.enums.ActivityCategory;
import com.aiu.trips.enums.ActivityStatus;
import com.aiu.trips.enums.ActivityType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstract Activity entity as per Data_Layer.pu diagram
 * Base class for Event and Trip
 */
@Entity
@Table(name = "activities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activity_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime activityDate;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status;

    @Column(nullable = false)
    private Long organizerId;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private ActivityType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (availableSeats == null) {
            availableSeats = capacity;
        }
        if (status == null) {
            status = ActivityStatus.UPCOMING;
        }
    }

    // Constructors
    public Activity() {}

    public Activity(String name, String description, LocalDateTime activityDate, String location,
                   Integer capacity, BigDecimal price, ActivityCategory category, Long organizerId) {
        this.name = name;
        this.description = description;
        this.activityDate = activityDate;
        this.location = location;
        this.capacity = capacity;
        this.availableSeats = capacity;
        this.price = price;
        this.category = category;
        this.organizerId = organizerId;
        this.status = ActivityStatus.UPCOMING;
    }

    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ActivityCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

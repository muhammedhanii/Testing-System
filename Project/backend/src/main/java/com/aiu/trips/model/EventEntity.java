package com.aiu.trips.model;

import com.aiu.trips.enums.ActivityCategory;
import com.aiu.trips.enums.ActivityType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Event entity as per Data_Layer.pu diagram
 * Subclass of Activity
 */
@Entity
@DiscriminatorValue("EVENT")
public class EventEntity extends Activity {

    @ElementCollection
    @CollectionTable(name = "event_speakers", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "speaker")
    private List<String> speakers = new ArrayList<>();

    @Column
    private String topic;

    @Column
    private String venue;

    @Column(length = 2000)
    private String agenda;

    // Constructors
    public EventEntity() {
        super();
    }

    public EventEntity(String name, String description, LocalDateTime activityDate, String location,
                      Integer capacity, BigDecimal price, ActivityCategory category, Long organizerId,
                      List<String> speakers, String topic, String venue, String agenda) {
        super(name, description, activityDate, location, capacity, price, category, organizerId);
        this.speakers = speakers != null ? speakers : new ArrayList<>();
        this.topic = topic;
        this.venue = venue;
        this.agenda = agenda;
        super.setType(ActivityType.EVENT);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void setEventType() {
        super.setType(ActivityType.EVENT);
    }

    // Getters and Setters
    public List<String> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<String> speakers) {
        this.speakers = speakers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }
}

package com.aiu.trips.controller;

import com.aiu.trips.config.TestConfig;
import com.aiu.trips.enums.EventStatus;
import com.aiu.trips.enums.EventType;
import com.aiu.trips.enums.UserRole;
import com.aiu.trips.model.Event;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.EventRepository;
import com.aiu.trips.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for EventController
 * Tests event CRUD operations using Command pattern and Builder pattern
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestConfig.class)
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setEmail("admin@aiu.edu");
        testUser.setPassword(passwordEncoder.encode("admin123"));
        testUser.setFullName("Admin User");
        testUser.setPhoneNumber("555-0200");
        testUser.setRole(UserRole.ADMIN);
        testUser = userRepository.save(testUser);
    }

    @Test
    void testGetAllEvents_Success() throws Exception {
        // Create test events
        Event event1 = createTestEvent("Tech Conference", EventType.EVENT);
        Event event2 = createTestEvent("Mountain Trip", EventType.TRIP);
        eventRepository.save(event1);
        eventRepository.save(event2);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testCreateEvent_Success() throws Exception {
        String eventJson = String.format("""
            {
                "title": "New Conference",
                "description": "A new conference",
                "type": "EVENT",
                "startDate": "%s",
                "location": "Main Hall",
                "price": 50.0,
                "capacity": 100
            }
            """, LocalDateTime.now().plusDays(30).toString());

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Conference"))
                .andExpect(jsonPath("$.capacity").value(100));
    }

    @Test
    void testUpdateEvent_Success() throws Exception {
        // Create event
        Event event = createTestEvent("Original Title", EventType.EVENT);
        event = eventRepository.save(event);

        String updateJson = """
            {
                "title": "Updated Title",
                "description": "Updated description",
                "price": 75.0
            }
            """;

        mockMvc.perform(put("/api/events/" + event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteEvent_Success() throws Exception {
        // Create event
        Event event = createTestEvent("To Delete", EventType.EVENT);
        event = eventRepository.save(event);

        mockMvc.perform(delete("/api/events/" + event.getId()))
                .andExpect(status().isOk());

        // Verify deleted
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetEventById_Success() throws Exception {
        Event event = createTestEvent("Test Event", EventType.EVENT);
        event = eventRepository.save(event);

        mockMvc.perform(get("/api/events/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    private Event createTestEvent(String title, EventType type) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription("Test description");
        event.setType(type);
        event.setStartDate(LocalDateTime.now().plusDays(30));
        event.setEndDate(LocalDateTime.now().plusDays(30).plusHours(4));
        event.setLocation("Test Location");
        event.setPrice(50.0);
        event.setCapacity(100);
        event.setCreatedBy(testUser);
        return event;
    }
}

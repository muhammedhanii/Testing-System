package com.aiu.trips.controller;

import com.aiu.trips.config.TestConfig;
import com.aiu.trips.enums.UserRole;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AuthController
 * Tests registration and login endpoints using Command pattern
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestConfig.class)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterNewUser_Success() throws Exception {
        String registerJson = """
            {
                "email": "newuser@aiu.edu",
                "password": "password123",
                "fullName": "New User",
                "phoneNumber": "555-0100",
                "role": "STUDENT"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newuser@aiu.edu"));
    }

    @Test
    void testRegisterDuplicateEmail_Failure() throws Exception {
        // Create existing user
        User existingUser = new User();
        existingUser.setEmail("existing@aiu.edu");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setFullName("Existing User");
        existingUser.setPhoneNumber("555-0101");
        existingUser.setRole(UserRole.STUDENT);
        userRepository.save(existingUser);

        String registerJson = """
            {
                "email": "existing@aiu.edu",
                "password": "password123",
                "fullName": "New User",
                "phoneNumber": "555-0102",
                "role": "STUDENT"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithValidCredentials_Success() throws Exception {
        // Create user
        User user = new User();
        user.setEmail("testuser@aiu.edu");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFullName("Test User");
        user.setPhoneNumber("555-0103");
        user.setRole(UserRole.STUDENT);
        userRepository.save(user);

        String loginJson = """
            {
                "email": "testuser@aiu.edu",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("testuser@aiu.edu"));
    }

    @Test
    void testLoginWithInvalidPassword_Failure() throws Exception {
        // Create user
        User user = new User();
        user.setEmail("testuser@aiu.edu");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFullName("Test User");
        user.setPhoneNumber("555-0104");
        user.setRole(UserRole.STUDENT);
        userRepository.save(user);

        String loginJson = """
            {
                "email": "testuser@aiu.edu",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithNonExistentUser_Failure() throws Exception {
        String loginJson = """
            {
                "email": "nonexistent@aiu.edu",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }
}

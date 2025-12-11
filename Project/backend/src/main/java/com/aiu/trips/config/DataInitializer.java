package com.aiu.trips.config;

import com.aiu.trips.enums.UserRole;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data Initializer - Creates essential admin user
 * Runs before DatabaseSeeder (Order 1)
 */
@Component
@Order(1)
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user if not exists
        if (!userRepository.existsByEmail("admin@aiu.edu")) {
            User admin = new User();
            admin.setEmail("admin@aiu.edu");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setPhoneNumber("1234567890");
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created: admin@aiu.edu / admin123");
        }
    }
}

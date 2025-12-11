package com.aiu.trips.service.impl;

import com.aiu.trips.dto.UserDTO;
import com.aiu.trips.enums.UserRole;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.UserRepository;
import com.aiu.trips.security.JwtUtil;
import com.aiu.trips.service.interfaces.IAuthenticationUserManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserManagementServiceImpl implements IAuthenticationUserManagement {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserDTO register(UserDTO userData) {
        User user = new User();
        user.setEmail(userData.getEmail());
        String fullName = userData.getFirstName() + " " + (userData.getLastName() != null ? userData.getLastName() : "");
        user.setFullName(fullName.trim());
        user.setPhoneNumber(userData.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        user.setRole(UserRole.STUDENT);
        
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    public Map<String, Object> login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(email, user.getRole().name());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        response.put("userId", user.getId());
        
        return response;
    }

    @Override
    public void verifyEmail(String token) {
        // Email verification logic
    }

    @Override
    public void resetPassword(String email) {
        // Password reset logic
    }

    @Override
    @Transactional
    public void manageUserRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        String[] names = user.getFullName().split(" ", 2);
        dto.setFirstName(names[0]);
        if (names.length > 1) {
            dto.setLastName(names[1]);
        }
        dto.setRole(user.getRole());
        dto.setIsVerified(true);
        return dto;
    }
}

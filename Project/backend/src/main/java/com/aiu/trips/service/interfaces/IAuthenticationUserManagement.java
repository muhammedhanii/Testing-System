package com.aiu.trips.service.interfaces;

import com.aiu.trips.dto.UserDTO;
import com.aiu.trips.enums.UserRole;

import java.util.Map;

/**
 * IAuthenticationUserManagement interface as per Controller.pu diagram
 */
public interface IAuthenticationUserManagement {
    UserDTO register(UserDTO userData);
    Map<String, Object> login(String email, String password);
    void verifyEmail(String token);
    void resetPassword(String email);
    void manageUserRole(Long userId, UserRole role);
}

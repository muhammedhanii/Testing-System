package com.aiu.trips.command;

import com.aiu.trips.dto.UserDTO;
import com.aiu.trips.service.interfaces.IAuthenticationUserManagement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class RegisterCommand implements IControllerCommand {
    private final IAuthenticationUserManagement authService;

    public RegisterCommand(IAuthenticationUserManagement authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            UserDTO userDTO = mapToUserDTO(requestData);
            UserDTO result = authService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private UserDTO mapToUserDTO(Map<String, Object> data) {
        UserDTO dto = new UserDTO();
        dto.setEmail((String) data.get("email"));
        dto.setPassword((String) data.get("password"));
        dto.setFirstName((String) data.get("firstName"));
        dto.setLastName((String) data.get("lastName"));
        return dto;
    }
}

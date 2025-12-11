package com.aiu.trips.command;

import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * IControllerCommand interface as per Controller.pu diagram
 * Command Pattern - Interface for all controller commands
 */
public interface IControllerCommand {
    ResponseEntity<?> execute(Map<String, Object> requestData);
}

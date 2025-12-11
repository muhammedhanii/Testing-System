package com.aiu.trips.controller;

import com.aiu.trips.chain.RequestHandler;
import com.aiu.trips.command.*;
import com.aiu.trips.service.interfaces.IAuthenticationUserManagement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController - Uses Command Pattern for all authentication operations
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ControllerCommandInvoker commandInvoker;

    @Autowired
    private IAuthenticationUserManagement authService;

    @Autowired
    @Qualifier("requestHandlerChain")
    private RequestHandler handlerChain;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            // Use Chain of Responsibility
            handlerChain.handle(request);

            // Use Command Pattern
            IControllerCommand command = new RegisterCommand(authService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);

            IControllerCommand command = new LoginCommand(authService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

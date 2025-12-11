package com.aiu.trips.command;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * ControllerCommandInvoker as per Controller.pu diagram
 * Command Pattern - Manages a queue of commands to execute sequentially
 */
@Component
public class ControllerCommandInvoker {

    private final Queue<IControllerCommand> commandQueue = new LinkedList<>();

    /**
     * Push a command to the queue
     */
    public void pushToQueue(IControllerCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        commandQueue.offer(command);
    }

    /**
     * Execute the next command in the queue
     */
    public ResponseEntity<?> executeNext(Map<String, Object> requestData) {
        IControllerCommand command = commandQueue.poll();
        if (command == null) {
            throw new IllegalStateException("No command in queue to execute");
        }
        return command.execute(requestData);
    }

    /**
     * Check if there are more commands in the queue
     */
    public boolean hasNext() {
        return !commandQueue.isEmpty();
    }

    /**
     * Clear all commands from the queue
     */
    public void clear() {
        commandQueue.clear();
    }
}

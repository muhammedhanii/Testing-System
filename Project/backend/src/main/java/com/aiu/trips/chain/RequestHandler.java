package com.aiu.trips.chain;

import jakarta.servlet.http.HttpServletRequest;

/**
 * RequestHandler abstract class as per Controller.pu diagram
 * Chain of Responsibility Pattern - Abstract handler
 */
public abstract class RequestHandler {

    protected RequestHandler next;

    /**
     * Set the next handler in the chain
     */
    public RequestHandler setNext(RequestHandler handler) {
        this.next = handler;
        return handler;
    }

    /**
     * Handle the request
     */
    public abstract void handle(HttpServletRequest request) throws Exception;

    /**
     * Pass request to next handler
     */
    protected void handleNext(HttpServletRequest request) throws Exception {
        if (next != null) {
            next.handle(request);
        }
    }
}

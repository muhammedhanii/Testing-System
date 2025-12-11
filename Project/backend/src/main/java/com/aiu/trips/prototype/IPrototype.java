package com.aiu.trips.prototype;

/**
 * IPrototype interface as per Event_Management.pu diagram
 * Prototype Pattern
 */
public interface IPrototype<T> {
    T clone();
}

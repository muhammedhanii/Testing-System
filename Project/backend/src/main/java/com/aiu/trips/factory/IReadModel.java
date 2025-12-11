package com.aiu.trips.factory;

/**
 * IReadModel interface as per Repository_Layer.pu diagram
 */
public interface IReadModel<T> {
    T findById(String id);
}

package com.aiu.trips.factory;

/**
 * IWriteModel interface as per Repository_Layer.pu diagram
 */
public interface IWriteModel<T> {
    T save(T entity);
    T update(T entity);
    boolean delete(String id);
}

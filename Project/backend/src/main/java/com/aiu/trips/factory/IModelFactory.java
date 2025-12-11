package com.aiu.trips.factory;

/**
 * IModelFactory interface as per Model_Factory.pu diagram
 * Factory Pattern - Open-Closed, Generic Factory API
 */
public interface IModelFactory {
    <T> void register(String key, IBaseModel<T> model);
    <T> IBaseModel<T> get(String key);
    <T> IBaseModel<T> getByType(Class<T> type);
    boolean contains(String key);
    java.util.List<String> keys();
}

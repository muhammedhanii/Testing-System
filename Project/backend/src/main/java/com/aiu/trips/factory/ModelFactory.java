package com.aiu.trips.factory;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ModelFactory implementation as per Model_Factory.pu diagram
 * Factory Pattern - Concrete factory with a registry
 */
@Component
public class ModelFactory implements IModelFactory {

    private final Map<String, IBaseModel<?>> registry = new ConcurrentHashMap<>();

    @Override
    public <T> void register(String key, IBaseModel<T> model) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        registry.put(key, model);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IBaseModel<T> get(String key) {
        if (!registry.containsKey(key)) {
            throw new IllegalArgumentException("No model registered for key: " + key);
        }
        return (IBaseModel<T>) registry.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IBaseModel<T> getByType(Class<T> type) {
        return (IBaseModel<T>) registry.values().stream()
            .filter(model -> type.isAssignableFrom(model.getClass()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No model registered for type: " + type.getName()));
    }

    @Override
    public boolean contains(String key) {
        return registry.containsKey(key);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(registry.keySet());
    }
}

package com.aiu.trips.factory;

/**
 * IBaseModel interface as per Repository_Layer.pu diagram
 * Base interface for all repository models
 */
public interface IBaseModel<T> extends IReadModel<T>, IWriteModel<T> {
}

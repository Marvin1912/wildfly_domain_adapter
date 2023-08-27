package com.marvin.common.configuration.jms;

public interface JmsOperation<T> {
    void executeOperation(T obj);
}

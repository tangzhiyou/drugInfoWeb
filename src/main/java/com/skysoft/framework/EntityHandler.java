package com.skysoft.framework;

import java.io.IOException;

import org.apache.http.HttpEntity;

/**
 * Response��Ӧ�崦��ӿ�
 *
 */
public interface EntityHandler<T> {
    public T handleEntity(HttpEntity entity) throws IOException;
}

package com.skysoft.framework;

import java.io.IOException;

import org.apache.http.HttpEntity;

/**
 * Response响应体处理接口
 *
 */
public interface EntityHandler<T> {
    public T handleEntity(HttpEntity entity) throws IOException;
}

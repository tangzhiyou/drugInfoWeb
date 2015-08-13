package com.skysoft.framework;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Response��Ӧ����to�ֽ����鴦����
 *
 */
public class ByteArrayEntityHandler implements EntityHandler<byte[]> {
    public ByteArrayEntityHandler() {
    }

    @Override
    public byte[] handleEntity(HttpEntity entity) throws IOException {
        if (!entity.isStreaming()) {
            return null;
        }
        return EntityUtils.toByteArray(entity);
    }
}

package com.skysoft.framework;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

/**
 * Response响应对象to文件流处理器
 *
 */
public class InputStreamEntityHandler implements EntityHandler<InputStream> {
    /**
     * 是否开启断点下载模式
     */
    private boolean range;
    /**
     * 响应状态码
     */
    private int statusCode;

    private InputStreamEntityHandler() {
    }

    public InputStreamEntityHandler(int statusCode) {
        this.statusCode = statusCode;
    }

    public InputStreamEntityHandler(boolean range, int statusCode) {
        this.range = range;
        this.statusCode = statusCode;
    }

    @Override
    public InputStream handleEntity(HttpEntity entity) throws IOException {
        if (statusCode == 206 || (statusCode == 200 && !range)) {
            return entity.getContent();
        }
        return null;
    }
}

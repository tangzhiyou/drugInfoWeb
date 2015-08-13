package com.skysoft.framework;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

/**
 * Response��Ӧ����to�ļ���������
 *
 */
public class InputStreamEntityHandler implements EntityHandler<InputStream> {
    /**
     * �Ƿ����ϵ�����ģʽ
     */
    private boolean range;
    /**
     * ��Ӧ״̬��
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

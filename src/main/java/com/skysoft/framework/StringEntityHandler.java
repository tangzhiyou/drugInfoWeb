package com.skysoft.framework;

import com.skysoft.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Response响应对象to String处理器
 *
 */
@SuppressWarnings("deprecation")
public class StringEntityHandler implements EntityHandler<String> {
    private String encoding = "UTF-8";

    /**
     * 供外部重置字符串转换编码
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public StringEntityHandler() {
    }

    public StringEntityHandler(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String handleEntity(HttpEntity entity) throws IOException {
        if (entity != null) {
            String contentCharset = EntityUtils.getContentCharSet(entity);
            String html = EntityUtils.toString(entity, encoding);
            String pageCharset = StringUtils.getCharsetFromMeta(html);
            String charset = null;
            if (pageCharset != null) {
                charset = pageCharset;
            } else {
                if (contentCharset != null) {
                    charset = contentCharset;
                } else {
                    charset = encoding;
                }
            }
            html = EntityUtils.toString(entity, charset);
            return html;
        }
        return null;
    }
}

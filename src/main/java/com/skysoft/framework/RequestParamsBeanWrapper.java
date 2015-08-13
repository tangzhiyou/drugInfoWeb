package com.skysoft.framework;

import java.util.List;
import java.util.Map;

/**
 * Request���������װ��
 *
 */
public class RequestParamsBeanWrapper {
    /**
     * ����URL
     */
    private String url;
    /**
     * ����ͷ��Ϣ
     */
    private Map<String, String> headers;
    /**
     * ��ͨ������
     */
    private Map<String, String> params;
    /**
     * �ļ���ı�Ԫ������
     */
    private String fileFormName = "file";
    /**
     * �ļ��ϴ����Ŀ��·��
     */
    private List<String> fileParams;
    /**
     * ��������IP
     */
    private String host;
    /**
     * ����˿ں�
     */
    private int port;
    /**
     * ����Ĭ�ϱ���
     */
    private String encoding = "UTF-8";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getFileFormName() {
        return fileFormName;
    }

    public void setFileFormName(String fileFormName) {
        this.fileFormName = fileFormName;
    }

    public List<String> getFileParams() {
        return fileParams;
    }

    public void setFileParams(List<String> fileParams) {
        this.fileParams = fileParams;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public RequestParamsBeanWrapper() {
    }

    public RequestParamsBeanWrapper(String url, Map<String, String> params) {
        this.url = url;
        this.params = params;
    }
}

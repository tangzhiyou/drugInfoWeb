package com.skysoft.framework;

import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

/**
 * Response响应体包装器
 *
 */
public class Result {
    /**
     * Cookie
     */
    private String cookie;
    /**
     * 响应状态码
     */
    private int statusCode;
    /**
     * 响应头信息
     */
    private HashMap<String, Header> headerMap;
    /**
     * 响应主体对象
     */
    private HttpEntity httpEntity;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HashMap<String, Header> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HashMap<String, Header> headerMap) {
        this.headerMap = headerMap;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public void setHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }

    public void setHeaders(Header[] headers) {
        headerMap = new HashMap<String, Header>();
        for (Header header : headers) {
            headerMap.put(header.getName(), header);
        }
    }

    public Result() {
    }

    public Result(String cookie, int statusCode, HashMap<String, Header> headerMap, HttpEntity httpEntity) {
        this.cookie = cookie;
        this.statusCode = statusCode;
        this.headerMap = headerMap;
        this.httpEntity = httpEntity;
    }
}

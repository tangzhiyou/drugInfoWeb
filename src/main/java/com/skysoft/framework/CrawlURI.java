package com.skysoft.framework;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CrawlURI{

    private String oriUrl; // 原始URL值，主机部分是域名
    private String Url; // URL值，主机部分是IP，为了防止重复主机的出现
    private int urlNo; // URL NUM
    private int statusCode; // 获取URL返回的结果码
    private String charSet; // 此URL对应文章的汉字编码
    private int status; // URL的状态
    private Timestamp lastUpdateTime;// 最后修改时间
    private Date timeToLive;// 过期时间
    private String[] urlRefrences; // 引用的链接
    private int layer; // 爬取的层次,从种子开始，依次为第0层，第1层。。。。
    private int laymm;

    public String getOriUrl() {
        return oriUrl;
    }

    public void setOriUrl(String oriUrl) {
        this.oriUrl = oriUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getUrlNo() {
        return urlNo;
    }

    public void setUrlNo(int urlNo) {
        this.urlNo = urlNo;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Date timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String[] getUrlRefrences() {
        return urlRefrences;
    }

    public void setUrlRefrences(String[] urlRefrences) {
        this.urlRefrences = urlRefrences;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
package com.skysoft.test;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User:  tangzhiyou
 * User:  14-1-22
 * User:  下午10:49
 */
public class TestCFDACookie {
    public static void main(String[] args) throws Exception {
        String currentUrl="http://sp.drugadmin.com/network/registerAction!validateCode.action?sureCode=k166";
        String dataUrl="";
        HttpClient httpClient=new HttpClient();
        HttpClientParams clientParams = new HttpClientParams();
        //设置 USER_AGENT
        clientParams.setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0");
        //设置连接字符集
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        httpClient.getParams().setParameter("","");

        httpClient.setParams(clientParams);

        GetMethod getMethod=new GetMethod(currentUrl);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.executeMethod(getMethod);
        Cookie[] cookies=httpClient.getState().getCookies();
        String tempCookies="";
        for(Cookie c:cookies)
        {
            tempCookies+=c.toString()+";";
        }
        System.out.println(tempCookies);
        byte[] responseBody = getMethod.getResponseBodyAsString().getBytes(
                getMethod.getResponseCharSet());
        String content = new String(responseBody, "utf-8");
        System.out.println(content);
        getMethod.releaseConnection();


    }
    public static  void QueryValidation()
    {
        PostMethod postMethod=new PostMethod("");
        NameValuePair[] data={
                new NameValuePair("",""),
                new NameValuePair("",""),
                new NameValuePair("","")
        };

    }

}

package com.skysoft.framework;

/**
 * User: pinaster
 * Date: 13-11-28
 * Time: 下午3:53
 */


import com.skysoft.util.FileIOStreamTools;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 处理相关数据流
 */
public class NetWorkHandlerData {

    static Logger logger = LoggerFactory.getLogger(NetWorkHandlerData.class);

    /*下载 url 指向的网页*/
    public static String fetchNetWorkData(String url, String characterset) {

//        Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0
        String content = "";
          /* 1.生成 HttpClinet 对象并设置参数*/
        HttpClient httpClient = new HttpClient();
        HttpClientParams clientParams = new HttpClientParams();
        //设置 USER_AGENT
        clientParams.setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0");
        //设置连接字符集
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, characterset);

        httpClient.setParams(clientParams);
        //设置 Http 连接超时 5s
        try {
            httpClient.getHttpConnectionManager().getParams().
                    setConnectionTimeout(50000);
            httpClient.getHttpConnectionManager().getParams()
                    .setSoTimeout(20000);
        }catch (Exception ex)
        {
            httpClient.getHttpConnectionManager().getParams().
                    setConnectionTimeout(50000);
            httpClient.getHttpConnectionManager().getParams()
                    .setSoTimeout(20000);
        }
		  /*2.生成 GetMethod 对象并设置参数*/
        GetMethod getMethod = new GetMethod(url);
        //设置 get 请求超时 5s
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(100,true));

		  /*3.执行 HTTP GET 请求*/
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            //读取为 InputStream，在网页内容数据量大时候推荐使用
            InputStream response = getMethod.getResponseBodyAsStream();
            content = FileIOStreamTools.InputStreamToString(response, characterset);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 释放连接
            getMethod.releaseConnection();
        }
        return content;
    }


    /*下载 url 指向的网页*/
    public static void fetchNetWorkStream(String url, String characterset, String fileName) {
        System.out.println("爬取图片位置:" + url + "文件存放在的路径:" + fileName);
        InputStream response = null;
          /* 1.生成 HttpClinet 对象并设置参数*/
        HttpClient httpClient = new HttpClient();
        HttpClientParams clientParams = new HttpClientParams();
        //设置 USER_AGENT
        clientParams.setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0");
        //设置连接字符集
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        httpClient.setParams(clientParams);
        //设置 Http 连接超时 5s
        try {
            httpClient.getHttpConnectionManager().getParams().
                    setConnectionTimeout(50000);
            httpClient.getHttpConnectionManager().getParams()
                    .setSoTimeout(5000);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("连接服务器出错了");
            e.printStackTrace();
            httpClient.getHttpConnectionManager().getParams().
                    setConnectionTimeout(50000);
            httpClient.getHttpConnectionManager().getParams()
                    .setSoTimeout(5000);
        }
          /*2.生成 GetMethod 对象并设置参数*/
        GetMethod getMethod = new GetMethod(url);
        //设置 get 请求超时 5s

        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(100, true));

          /*3.执行 HTTP GET 请求*/
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 打印服务器返回的状态
            if (statusCode == HttpStatus.SC_OK) {
                //读取为 InputStream，在网页内容数据量大时候推荐使用
                response = getMethod.getResponseBodyAsStream();
//                HttpclientDownLoadFile(fileName, response);
            }
        } catch (Exception e) {
            System.out.println("得到资源出错了！");
            e.printStackTrace();
        } finally {
            // 释放连接
            getMethod.releaseConnection();
        }
    }

    public static byte[] fetchNetWorkAttachment(String url) {
        byte[] response = null;
        HttpClient httpClient = new HttpClient();
        //设置 Http 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().
                setConnectionTimeout(5000);
          /*2.生成 GetMethod 对象并设置参数*/
        GetMethod getMethod = new GetMethod(url);
        //设置 get 请求超时 5s

        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

		  /*3.执行 HTTP GET 请求*/
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            //判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }

            //读取为 InputStream，在网页内容数据量大时候推荐使用
            response = getMethod.getResponseBody();

        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            e.printStackTrace();
        } finally {
            // 释放连接
            getMethod.releaseConnection();
        }

        return response;
    }

    public static String ImitateLoginSite(String url,String characterset,String pageNumber)
    {
        String content="";
        HttpClient httpClient=new HttpClient();
        PostMethod postMethod=new PostMethod(url);
        NameValuePair[] data={
                new NameValuePair("__VIEWSTATE","/wEPDwULLTEzODQxNzY5NjMPZBYCAgEPZBYGAgEPDxYCHgRUZXh0BQ3nu7RD6ZO257+Y54mHZGQCBQ8PFgIfAAVpPGZvbnQgY29sb3I9J2JsYWNrJz7ns7vnu5/kuK3nrKblkIjmnaHku7Y8L2ZvbnQ+IOe7tEPpk7bnv5jniYc8Zm9udCBjb2xvcj0nYmxhY2snPiDnmoTllYblk4HmnInvvJo8L2ZvbnQ+ZGQCCQ8PFgQeC1JlY29yZGNvdW50AowBHg5DdXN0b21JbmZvVGV4dAWRAeaAu+iusOW9leaVsO+8mjxmb250IGNvbG9yPSJibHVlIj48Yj4xNDA8L2I+PC9mb250PiDmgLvpobXmlbDvvJo8Zm9udCBjb2xvcj0iYmx1ZSI+PGI+MTA8L2I+PC9mb250PiDlvZPliY3pobXvvJo8Zm9udCBjb2xvcj0icmVkIj48Yj4xPC9iPjwvZm9udD5kZGTTH+FVIATkq4vm6Z9r3eqdIM2F/w=="),
                new NameValuePair("__EVENTVALIDATION","/wEWAwLV6/uwCALd5eLQCQLmjL2EB8fO+J+HuoaczuXONjQGC4Us7zRM"),
                new NameValuePair("__EVENTTARGET","myPager"),
                new NameValuePair("__EVENTARGUMENT",pageNumber),
                new NameValuePair("ContentType","application/x-www-form-urlencoded")
        };
        postMethod.setRequestBody(data);
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(postMethod);
            int code=postMethod.getStatusCode();

                content=new String(postMethod.getResponseBodyAsString().getBytes(characterset));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return content;

    }

    public static String SearchGDS(String url,String characterset)
    {
        HttpClient httpClient=new HttpClient();
        PostMethod postMethod=new PostMethod(url);
        NameValuePair[] data={
                new NameValuePair("__VIEWSTATE","/wEPDwULLTEzODQxNzY5NjNkZEc4gDy0wp5ERjILg2b7lTTH3F+w"),
                new NameValuePair("__EVENTVALIDATION","/wEWAwKK7u6vCQLd5eLQCQLmjL2EBxmZU7jWYoh9371phOcBPCjfgdVD"),
                new NameValuePair("gdsBtn","%C9%CC%C6%B7%CB%D1%CB%F7"),
                new NameValuePair("keyword","%CA%B7%B9%FA%B9%AB%D2%A9%BE%C6"),
                new NameValuePair("ContentType","application/x-www-form-urlencoded"),
                new NameValuePair("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36")
        };
        postMethod.setRequestBody(data);
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(postMethod);
            int code=postMethod.getStatusCode();

            String info=new String(postMethod.getResponseBodyAsString().getBytes("UTF-8"));
            System.out.println(info);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

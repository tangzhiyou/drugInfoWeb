package com.skysoft.util;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * @author pinaster.tang@hotmail.com
 * @date 2015年4月12日
 */
public class JsoupUtil {
    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    private static final String LANGUAGE = "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3";
    private static final String CONNECTION = "keep-alive";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:36.0) Gecko/20100101 Firefox/36.0";

    public static String getContentByPost(String url) {
        Connection conn = Jsoup.connect(url)
                .header("Accept", ACCEPT)
                .header("Accept-Language", LANGUAGE)
                .header("Connection", CONNECTION)
                .header("Content-Type", "text/html;charset=utf-8")
                .timeout(3000)
                .header("User-Agent", USER_AGENT)
                .ignoreContentType(true);
        String html = "";
        try {
            html = conn.post().html();

            html = html.replaceAll("[\n\r]", "");
        } catch (Exception e) {
            System.out.println("获取URL：" + url + "页面出错");
        }
        return html;
    }

    public static String getContentByGet(String url) {
        Connection conn = Jsoup.connect(url)
                .header("Accept", ACCEPT)
                .header("Accept-Language", LANGUAGE)
                .header("Connection", CONNECTION)
                .timeout(3000)
                .header("User-Agent", USER_AGENT)
                .ignoreContentType(true);
        String html = "";
        try {
            html = conn.get().html();
            html = html.replaceAll("[\n\r]", "");
        } catch (Exception e) {
            System.out.println("获取URL：" + url + "页面出错");
        }
        return html;
    }

    public static String getHttpURLConnectionByGet(String url, boolean isProxy) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.asiainfo.com", 8080));
        String html = "";
        HttpURLConnection httpUrlConnetion = null;
        try {
            URL website = new URL(url);
            if (isProxy) {
                httpUrlConnetion = (HttpURLConnection) website.openConnection(proxy);
            } else {
                httpUrlConnetion = (HttpURLConnection) website.openConnection();
            }

            httpUrlConnetion.setConnectTimeout(1000 * 30);
            httpUrlConnetion.setReadTimeout(1000 * 30);
            httpUrlConnetion.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpUrlConnetion.setRequestProperty("Connection", "keep-alive");
            httpUrlConnetion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
            httpUrlConnetion.setRequestMethod("GET");
            httpUrlConnetion.connect();
            html = IOUtils.toString(httpUrlConnetion.getInputStream(), "UTF-8");
            html = html.replaceAll("[\n\r]", "");
        } catch (Exception e) {
            System.out.println("获取URL：" + url + "页面出错");
        }
        return html;
    }

    public static String getHttpURLConnectionByPost(String url, boolean isProxy) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.asiainfo.com", 8080));
        String html = "";
        HttpURLConnection httpUrlConnetion = null;
        try {
            URL website = new URL(url);
            if (isProxy) {
                httpUrlConnetion = (HttpURLConnection) website.openConnection(proxy);
            } else {
                httpUrlConnetion = (HttpURLConnection) website.openConnection();
            }
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            httpUrlConnetion.setDoInput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpUrlConnetion.setDoOutput(true);
            // Post 请求不能使用缓存
            httpUrlConnetion.setUseCaches(false);
            httpUrlConnetion.setConnectTimeout(1000 * 30);
            httpUrlConnetion.setReadTimeout(1000 * 30);
            httpUrlConnetion.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpUrlConnetion.setRequestProperty("Connection", "keep-alive");
            httpUrlConnetion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
            httpUrlConnetion.setRequestMethod("POST");
            httpUrlConnetion.connect();
            html = IOUtils.toString(httpUrlConnetion.getInputStream(), "UTF-8");
            html = html.replaceAll("[\n\r]", "");
        } catch (Exception e) {
            System.out.println("获取URL：" + url + "页面出错");
        }
        return html;
    }

    public static LinkedHashMap<String, String> extractTableText(String content, String cssQuery) {
        Document document = Jsoup.parse(content);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        Document doc = Jsoup.parse(document.select(cssQuery).html());
        Element elementsByTag = doc.select("table").get(0);
        Elements rows = elementsByTag.select("tr");
        for (Element row : rows) {

            Elements cols = row.select("td");
            String text = "";
            for (Element col : cols) {
                text += col.text() + ":";
            }
            String[] val = text.split(":");
            if (val.length == 1) {
                valueMap.put(val[0], "");
                continue;
            } else if (val.length > 1) {
                valueMap.put(val[0], val[1]);
            }
        }
        return valueMap;
    }
}

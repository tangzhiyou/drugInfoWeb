package com.skysoft;

import com.skysoft.framework.HttpClientUtils;
import com.skysoft.util.GerneralUtils;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description: 百度美女图片爬取
 */
public class BaiduImageCrawler {
    private static final String TAG1 = "美女";
    private static final String TAG2 = "全部";
    private static final int PAGE_SIZE = 60;
    private static String START_URL = "http://image.baidu.com/i?tn=listjson&word=liulan&oe=utf-8&ie=utf8&tag1=" +
            URLEncoder.encode(TAG1) + "&tag2=" + URLEncoder.encode(TAG2) + "&sorttype=0&pn=0&rn=" + PAGE_SIZE + "&ia=0";

    private static final Map<String, String> headers = new HashMap<String, String>();
    private static final Map<String, String> headerMap = new HashMap<String, String>();

    static {
        headers.put("Host", "image.baidu.com");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Referer", "http://image.baidu.com/i?tn=list&word=liulan#%E7%BE%8E%E5%A5%B3&%E5%85%A8%E9%83%A8&0&1");
        headers.put("Cookie", "BAIDUID=CF997091228659B1642E88501048EA7E:FG=1; fb=0; Hm_lvt_1acd62d7cea079d029eb54e5db7549d1=1363613801; Hm_lpvt_1acd62d7cea079d029eb54e5db7549d1=1363614116; PMS_Cache=1363613812342");
        headers.put("Connection", "keep-alive");
        headers.put("Cache-Control", "max-age=0");

        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20100101 Firefox/12.0");
        headerMap.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headerMap.put("Accept", "image/png,image/*;q=0.8,*/*;q=0.5");
        headerMap.put("Referer", "http://image.baidu.com/i?tn=list&word=liulan");
        headerMap.put("Cookie", "BAIDUID=CF997091228659B1642E88501048EA7E:FG=1");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Cache-Control", "max-age=0");
    }
    public static void main(String[] args) throws ClientProtocolException, IOException {
        int totalCount = getTotalCount();
        int totalPage = totalCount % PAGE_SIZE == 0 ? totalCount / PAGE_SIZE : (totalCount / PAGE_SIZE) + 1;
        //所有分页链接
        List<String> pageURLs = getPageURLs(totalPage);
        //所有图片链接
        List<String> imageURLList = getAllImageURLs(pageURLs);
        //多线程下载图片,开500个线程同时下载
        List<List> list = GerneralUtils.splitList(imageURLList, 500);
        for (List subList : list) {
            new BaiduImageDownloadThread(subList, headerMap).start();
        }
    }

    /**
     * @Title: getAllImageURLs
     * @Description: 提取所有图片链接
     * @param @param pageURLs
     * @param @return
     * @param @throws ClientProtocolException
     * @param @throws IOException 
     * @return List<String> 
     * @throws
     */
    private static List<String> getAllImageURLs(List<String> pageURLs) throws ClientProtocolException, IOException {
        List<String> imageURLList = new ArrayList<String>();
        for (int i = 0; i < pageURLs.size(); i++) {
            String pageURL = pageURLs.get(i);
            String json = HttpClientUtils.getHTML(pageURL, headers);
            String data = json.replaceAll(".*,\"data\":\\[([^\\[]+)\\].*", "$1");
            List<String> imgURLList = getImageUrl(data);
            imageURLList.addAll(imgURLList);
        }
        return imageURLList;
    }

    /**
     * @Title: getImageUrl
     * @Description: 从JSON数据中提取图片链接
     * @param @param data
     * @param @return 
     * @return List<String> 
     * @throws
     */
    public static List<String> getImageUrl(String data) {
        List<String> imgList = new ArrayList<String>();
        StringBuffer reg = new StringBuffer();
        reg.append("\"image_url\":\"([^\"]+)\"");
        Pattern pattern = Pattern.compile(reg.toString());
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String url = matcher.group(1);
            imgList.add(url);
        }
        return imgList;
    }

    /**
     * @Title: getPageURLs
     * @Description: 获取每页的Ajax请求链接
     * @param @param totalPage
     * @param @return 
     * @return List<String> 
     * @throws
     */
    private static List<String> getPageURLs(int totalPage) {
        List<String> pageURLs = new ArrayList<String>();
        for (int i = 1; i <= totalPage; i++) {
            int start = (i - 1) * PAGE_SIZE;
            START_URL = START_URL.replaceAll("&pn=\\d+", "&pn=" + start);
            pageURLs.add(START_URL);
        }
        return pageURLs;
    }

    /**
     * @Title: getTotalCount
     * @Description: 获取图片总张数
     * @param @return
     * @param @throws ClientProtocolException
     * @param @throws IOException 
     * @return int
     * @throws
     */
    private static int getTotalCount() throws ClientProtocolException,
            IOException {
        String json = HttpClientUtils.getHTML(START_URL, headers);
        return Integer.parseInt(json.replaceAll(".*,\"totalNum\":(\\d+),.*", "$1"));
    }

}

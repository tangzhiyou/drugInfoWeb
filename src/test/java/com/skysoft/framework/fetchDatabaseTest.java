package com.skysoft.framework;

import com.google.common.net.UrlEscapers;
import com.skysoft.domain.Druggds;
import com.skysoft.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.net.URLCodec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Created by tangzy on 2015/8/14.
 */
public class fetchDatabaseTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateAndBindDatabase() throws Exception {
//        Queue<String> persistentQueue=null;
//        fetchDatabase worker=null;
//        // 创建url数据库文件
//        String urlDir = "Resources/BDBdatas/url";
//        File urlFile = new File(urlDir);
//        if (!urlFile.exists() || !urlFile.isDirectory()) {
//            urlFile.mkdirs();
//        }
//        String fetchDataDir = "Resources/BDBdatas/fetchData";
//        File fetchDataFile = new File(fetchDataDir);
//        if (!fetchDataFile.exists() || !fetchDataFile.isDirectory()) {
//            fetchDataFile.mkdirs();
//        }
//        // 创建爬取数据库文件
//        persistentQueue = new BdbPersistentQueue(urlDir, "fetchData",
//                String.class);
//
//        worker = new fetchDatabase();
//        worker.createAndBindDatabase("Resources/BDBdatas/fetchData");
//        worker.writeData("aa");
//        System.out.println(String.valueOf(worker.map.get(0)));
    }

    @Test
    public void testsfdaurl(){
        String host="http://app1.sfda.gov.cn/datasearch/face3/";
        String html= null;
        try {
            html = HttpClientUtils.getHTML("http://app1.sfda.gov.cn/datasearch/face3/base.jsp?tableId=25&tableName=TABLE25&title=%B9%FA%B2%FA%D2%A9%C6%B7&bcId=124356560303886909015737447882");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> contents= HtmlParserTool.extractHtmlLabel(html, "div#content");
        long totalPage=0;
        long totalNum=0;

        List<String> tables=HtmlParserTool.extractHtmlLabel(contents.get(0), "table");
        String text=tables.get(4);
        String ext=text.substring(text.indexOf("<td width=\"160\">"), text.indexOf("</td>"));
        ext= HtmlRegexpUtil.filterHtml(ext);
        List<String> totalValues=StringUtils.getNumbericFromString(ext);
        totalPage=Long.valueOf(totalValues.get(1));
        totalNum=Long.valueOf(totalValues.get(2));

        Map<String, String> headers=new HashMap<String,String>();
        headers.put("Host","app1.sfda.gov.cn");
        headers.put("Connection","keep-alive");
        headers.put("cache-control","no-cache");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.125 Safari/537.36");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept","*/*");
        headers.put("Referer","http://app1.sfda.gov.cn/datasearch/face3/base.jsp?tableId=25&tableName=TABLE25&title=%B9%FA%B2%FA%D2%A9%C6%B7&bcId=124356560303886909015737447882");
        headers.put("Cookie","JSESSIONID=A5628F03D8F8DC56530DFF1F51248606.7; __utmz=59192815.1439557795.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); _gscu_1358151024=39557794bq45j369; _gscs_1358151024=t39561618wx78d669|pv:1; _gscbrs_1358151024=1; __utma=59192815.494353602.1439557795.1439557795.1439561619.2; __utmb=59192815; __utmc=59192815; _gscu_1586185021=39557939cnh10x19; _gscs_1586185021=t39560524ji7n2418|pv:17; _gscbrs_1586185021=1");
        headers.put("Accept-Language","zh-CN,zh;q=0.8");
        headers.put("Accept-Encoding","gzip, deflate");
        Map<String, String> params=new HashMap<String,String>();
        params.put("tableId","25");
        params.put("bcId","124356560303886909015737447882");
        params.put("tableName","TABLE25");
        params.put("viewtitleName","COLUMN167");
        params.put("viewsubTitleName","COLUMN166,COLUMN170,COLUMN821");
        params.put("curstart","1");
        params.put("tableView","%E5%9B%BD%E4%BA%A7%E8%8D%AF%E5%93%81");


        for (int i = 1; i <=totalPage; i++) {
            params.put("curstart",String.valueOf(i));
            Result result= null;
            String content=null;
            StringEntityHandler entityHandler = new StringEntityHandler();
            try {
                try {
                    Thread.sleep(1500);
                    System.out.println("---------------程序休息1.5s------------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = HttpClientUtils.post("http://app1.sfda.gov.cn/datasearch/face3/search.jsp", headers, params);

                content=entityHandler.handleEntity(result.getHttpEntity());
            } catch (IOException e) {
                System.out.println("--------------error--------------");
                e.printStackTrace();
                continue;
            }
            List<String> tables01=HtmlParserTool.extractHtmlLabel(content, "table");
            List<String> urls=HtmlParserTool.extractAttributeHref(tables01.get(2),"a");
            for(String str:urls)
            {
                String url=str.substring(str.indexOf("\'")+1, str.lastIndexOf("\'"));
                System.out.println(host+url);
            }

        }
    }
    @Test
    public void testExtractSfda()
    {
        String url="http://app1.sfda.gov.cn/datasearch/face3/content.jsp?tableId=25&tableName=TABLE25&tableView=%B9%FA%B2%FA%D2%A9%C6%B7&Id=187764";
//        String url="http://app1.sfda.gov.cn/datasearch/face3/content.jsp?tableId=25&tableName=TABLE25&tableView=国产药品&Id=187764";
        try {
            String html=HttpClientUtils.getHTML(url);
            List<String> tables=HtmlParserTool.extractHtmlLabel(html, "table");
            Document doc = Jsoup.parse(tables.get(0));
//            Element infoTable = doc.getElementsByAttributeValue("class", "table002").first();
            Element infoTable = doc.select("table").first();
            Elements tableLineInfos = infoTable.select("tr");
            for (Element lineInfo : tableLineInfos) {
                System.out.println(lineInfo.select("td").last().text().trim());
//                for(Element td: lineInfo.select("td")) {
//                    String lineInfoContent =td.text().trim();
//                    System.out.println("jsoup is :" + lineInfoContent);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDruggdsWithBLOBs()
    {
//        List<String> fieldNameList= generateBean.getObjectProperty(DruggdsWithBLOBs.class.getSuperclass());
//
//        for (String str:fieldNameList)
//        {
//            System.out.println(str);
//        }
    }
}
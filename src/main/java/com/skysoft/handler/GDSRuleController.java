package com.skysoft.handler;

import com.skysoft.framework.HtmlParserTool;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.framework.fetchDatabase;
import com.skysoft.service.CFDAService;
import com.skysoft.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User:  tangzhiyou
 * User:  14-1-22
 * User:  下午1:13
 */
@Controller
@RequestMapping("/fetchGDS")
public class GDSRuleController {
    private static fetchDatabase worker;
    static Logger logger = LoggerFactory.getLogger(GDSRuleController.class);
    @Autowired
    private CFDAService cfdaService;

    @RequestMapping("fetchCFDA")
    public String initialize() {
        worker = new fetchDatabase();
        worker.createAndBindDatabase("Resources/BDBdatas/fetchData");
        return "fetchCFDA";
    }

    @RequestMapping(value = "/parserData", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String HandlerData(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
//
//        String keyword = null;
        int pageCount = 0;
//        try {
//            keyword = URLEncoderTools.GBKURLencode("维C银翘片");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        List<String> keywodes= FileIOStreamTools.readerDataList("conf/ProductName.txt",null);

        for (String keyes:keywodes)
        {
            String currentUrl = "http://search.anccnet.com/searchResult2.aspx?__VIEWSTATE=%2FwEPDwULLTEzODQxNzY5NjNkZEc4gDy0wp5ERjILg2b7lTTH3F%2Bw&__EVENTVALIDATION=%2FwEWAwKK7u6vCQLd5eLQCQLmjL2EBxmZU7jWYoh9371phOcBPCjfgdVD&gdsBtn=%C9%CC%C6%B7%CB%D1%CB%F7&keyword=" + keyes;
            System.out.println("当前当在爬取的URL地址"+currentUrl);
            String context = NetWorkHandlerData.startFetchData(currentUrl, "gb2312");
            String bodyContent = HtmlParserTool.extractHtmlLabel(context, "body");
            pageCount = extractPageCount(bodyContent);
            String results = HtmlParserTool.extractHtmlLabel(bodyContent, "ol#results");
            if (pageCount == -1) {
                extractText(results, "");
            } else if (pageCount >= 1) {
                extractText(results, "");
                extractPageText("http://search.anccnet.com/searchResult2.aspx?gdsBtn=%C9%CC%C6%B7%CB%D1%CB%F7&keyword="+keyes,"",pageCount);
            }

            if (worker.size() >= 1) {
                cfdaService.saveBeans(worker.map, "drug_gds");
            }
        }
        return "successful";
    }

    //抽取内容
    public static void extractText(String context, String contexturl) {
        if (StringUtils.isBlank(context))
        {
            return;
        }
        Document doc = Jsoup.parse(context);
        Elements values = doc.select("div[class=result]");
        for (Element link : values) {
            String HtmlText = link.outerHtml();
            extractVlues(HtmlText);
        }
    }

    public static void extractVlues(String context) {
        List<Object> objectList = null;
        int count = 0;
        Document doc = Jsoup.parse(context);
        objectList = new ArrayList<Object>();

        Elements values = doc.select("dl>dd");
        for (Element link : values) {
            String HtmlText = link.text();
            objectList.add(count++, HtmlText);
        }
        saveContent(objectList);
    }

    public static void extractPageText(String context, String contexturl, int pageCount) {
        if (StringUtils.isBlank(context))
        {
            return;
        }
        for (int i = 2; i <= pageCount; i++) {
            String text = NetWorkHandlerData.ImitateLoginSite(context, "utf-8", String.format("%s", i));
            String results = HtmlParserTool.extractHtmlLabel(text, "ol#results");
            extractText(results, "");

        }
    }

    public static int extractPageCount(String content) throws Exception {

        String myPager = HtmlParserTool.extractHtmlLabel(content, "div#myPager");
        if (StringUtils.isBlank(myPager.replaceAll("<([^>]*)>","")))
        {
            return -1;
        } else {
            String pageCount = HtmlRegexpUtil.interceptContent("总页数：<font color=\"blue\">", "</font>", new StringBuffer(myPager));
            pageCount = pageCount.replaceAll("<([^>]*)>", "").replaceAll("总页数：", "");
            return Integer.parseInt(pageCount);
        }
    }

    //保持数据库
    private static void saveContent(List<Object> listText) {
        Object data = null;
        try {
            data = ReflectUtil.ReflectToEntity(ReflectUtil.packageName("conf/DefaultRule.xml"), ReflectUtil.methodList("conf/DefaultRule.xml"), listText);
            worker.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("当前爬取容器的数量**************************:"
                + worker.size());
    }
}

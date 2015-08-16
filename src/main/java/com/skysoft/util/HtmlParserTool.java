package com.skysoft.util;

/**
 * User: pinaster
 * Date: 13-11-28
 * Time: 下午3:52
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML解析数据库 使用JSOUP截取字符串
 */
public class HtmlParserTool {

    public static List<String> extractHtmlLabel(String content, String filterLable) {
        Document doc = Jsoup.parse(content);
        List<String> htmlTextList=new ArrayList<String>();
        Elements values = doc.select(filterLable);
        for (Element link : values) {
            String htmlText = link.outerHtml();
            htmlTextList.add(htmlText);
        }
        return htmlTextList;
    }

    public static List<String> extractHtmlText(String content, String filterLable) {
        Document doc = Jsoup.parse(content);
        List<String> htmlTextList=new ArrayList<String>();
        Elements values = doc.select(filterLable);
        for (Element link : values) {
            String htmlText = link.text();
            htmlTextList.add(htmlText);
        }
        return htmlTextList;
    }

    public static List<String> extractAttributeHref(String content, String filterLable) {
        Document doc = Jsoup.parse(content);
        List<String> htmlTextList=new ArrayList<String>();
        Elements values = doc.select(filterLable);
        String linkHref = "";
        for (Element link : values) {
            //如果是图片
            if (filterLable.equals("img")) { //img
                linkHref = link.attr("src");
            } else if (filterLable.equals("a")) { //a
                linkHref = link.attr("href");
            }
            htmlTextList.add(linkHref);
        }
        return htmlTextList;
    }

    public static String extractSpecificContent(String s, String s1, String useful) {
        return null;
    }

    public static List<String> extractTable(String content, String filterLable)
    {
        List<String> values=new ArrayList<String>();
        Document doc = Jsoup.parse(content);
        Element infoTable = doc.select(filterLable).first();
        Elements infoTrs = infoTable.select("tr");
        for (Element infotds : infoTrs) {
            values.add(infotds.select("td").last().text().trim());
        }
        return values;
    }
}

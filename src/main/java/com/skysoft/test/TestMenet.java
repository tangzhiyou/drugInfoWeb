package com.skysoft.test;

import com.skysoft.framework.HtmlParserTool;
import com.skysoft.framework.NetWorkHandlerData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * User: pinaster
 * Date: 14-1-6
 * Time: 下午2:00
 */
public class TestMenet {
    public static void main(String[] args) {
        String currentUrl="http://www.menet.com.cn/information/index.htm";
        String content= NetWorkHandlerData.fetchNetWorkData(currentUrl, "UTF-8");
        String text= HtmlParserTool.extractHtmlLabel(content, "body");
        String imgText=null;
        Document doc = Jsoup.parse(text);
        Elements values =  doc.select("a[href]");
        String linkHref = "";
        for (Element link : values) {
            //如果是图片
             System.out.println(link.outerHtml());
        }
        System.out.println("图片地址:"+imgText);
    }
}

package com.skysoft.framework;

/**
 * User: pinaster
 * Date: 13-11-28
 * Time: 下午3:52
 */

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * HTML解析数据库 使用JSOUP截取字符串
 */
public class HtmlParserTool {

    public static String extractHtmlLabel(String content, String filterLable) {
        Document doc = Jsoup.parse(content);
        String HtmlText = "";
        StringBuffer sbHtmlText = null;
        Elements values = doc.select(filterLable);
        sbHtmlText = new StringBuffer();
        for (Element link : values) {
            HtmlText = link.outerHtml();
            sbHtmlText.append(HtmlText);
        }
        return sbHtmlText.toString();
    }

    public static String extractHtmlText(String content, String filterLable) {
        Document doc = Jsoup.parse(content);
        StringBuffer sbHtmlText = null;
        Elements values = doc.select(filterLable);
        sbHtmlText = new StringBuffer();
        for (Element link : values) {
            String HtmlText = link.text();
            sbHtmlText.append(HtmlText);
        }
        return sbHtmlText.toString();
    }

    public static String extractAttributeHref(String content, String filterLable) {
        Document doc = Jsoup.parse(content);
        StringBuffer sbHtmlText = null;
        Elements values = doc.select(filterLable);
        sbHtmlText = new StringBuffer();
        String linkHref = "";
        for (Element link : values) {
            //如果是图片
            if (filterLable.equals("img")) {
                linkHref = link.attr("src");
            } else if (filterLable.equals("a")) { //如果是a 链接
                linkHref = link.attr("href");
            }
            sbHtmlText.append(linkHref);
        }
        return sbHtmlText.toString();
    }

    // 自定义截取相关内容
    public static String extractSpecificContent(String StartRegExp,
            String EndRegExp, String Content) throws Exception {
        StringBuffer sbConent = null;
        int startPos = 0, endPos = 0;
        sbConent = new StringBuffer();
        Pattern spattern = Pattern.compile(StartRegExp);
        Matcher smatcher = spattern.matcher(Content);
        boolean sresult = smatcher.find();
        while (sresult) {
            startPos = smatcher.start();
            sresult = smatcher.find();
        }
        System.out.println("startPos****:" + startPos);
        Pattern epattern = Pattern.compile(EndRegExp);
        Matcher ematcher = epattern.matcher(Content);
        boolean eresult = ematcher.find();
        while (eresult) {
            endPos = ematcher.start();
            eresult = ematcher.find();
        }
        System.out.println("endPos****:" + endPos);
        if (startPos == -1 || endPos == 0 || endPos == -1) {
            sbConent.append("");
        } else {
            sbConent.append(Content.substring(startPos, endPos));
        }
        return sbConent.toString();
    }
}

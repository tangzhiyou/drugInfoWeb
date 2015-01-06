package com.skysoft.test;

import com.skysoft.framework.HtmlParserTool;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.util.FileIOStreamTools;
import org.jsoup.Jsoup;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: pinaster
 * Date: 13-12-11
 * Time: 下午3:57
 */
public class TestCDE {
    public static void main(String[] args)
    {
        /*String content=NetWorkHandlerData.fetchNetWorkData("http://www.pharmnet.com.cn/search/mprice/detail/df/00.html","gb2312");
        String TableText=HtmlParserTool.extractHtmlLabel(content,"table[class=bian221]");
        String H1Text=HtmlParserTool.extractHtmlText(TableText,"h1>a");
        List<String> listText=HtmlRegexpUtil.ExtractTableText(TableText,"",H1Text);
        System.out.println(listText.size());
        for (String text:listText)
        {
            System.out.println(text);
        }

        System.out.println(H1Text);*/
/*
        Map<String, String> webGather = FileIOStreamTools.parserRuleXml("conf/DefaultRule.xml");
        Iterator iter = webGather.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();

            if (key.equals("characterset")) {
                String characterset = entry.getValue().toString();
                System.out.println(characterset);
            }
            if (key.equals("batchUpdate")) {
                int batchUpdate = Integer.parseInt(entry.getValue().toString());
                System.out.println(batchUpdate);
            }
            if (key.equals("Datatable")) {
                String Datatable = entry.getValue().toString();
                System.out.println(Datatable);
            }
            if (key.equals("ImportFile")) {
                String ImportFile = entry.getValue().toString();
                System.out.println(ImportFile);
            }
            if (key.equals("MappingXML")) {
                String MappingXML = entry.getValue().toString();
                System.out.println(MappingXML);
            }
        }*/
        Set<String> links = FileIOStreamTools.readerDataSet("conf/urldata.txt", "");
        for (String link : links) {
            System.out.println(link);
        }

    }
}

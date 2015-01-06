package com.skysoft.util;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: pinaster
 * Date: 13-12-18
 * Time: 下午5:03
 */
public class XMLEscapeUtils {
    private final static String prefixRlue = "\\$\\{";
    private final static String suffixRlue = "\\}";

    public static void main(String[] args) {
        System.out.println(FormatXMLValue());
    }

    public static String FormatXMLValue() {
        Document document = null;// 读取XML文件

        document=getRootFileDocument("conf/DefaultRule.xml");

        String content = document.asXML();
//        System.out.println(content);
        Map properties = FileIOStreamTools.readerDataHashMap("conf/DefaultRule.properties", null);

        Iterator iter = properties.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String keys = String.format("%s", entry.getKey());
            String values = String.format("%s", entry.getValue());
            if (keys.contains("startregexp"))
            {
                content=content.replaceAll(prefixRlue+keys+suffixRlue,HtmlRegexpUtil.EscapeString(values));
//                System.out.println("当前的url"+values+"内容"+content);

            }else if (keys.contains("endregexp"))
            {
                content=content.replaceAll(prefixRlue+keys+suffixRlue,HtmlRegexpUtil.EscapeString(values));

            }
        }
//        XPathParserXml.TestGenXML(content);
        return content;
    }

    public static String FormNewColumnXML()
    {
        Document document = null;// 读取XML文件

        document=getRootFileDocument("conf/DefaultRule.xml");

        String content = document.asXML();
        Map properties = FileIOStreamTools.readerDataHashMap("conf/NewsColumn.properties", null);

        Iterator iter = properties.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String keys = String.format("%s", entry.getKey());
            String values = String.format("%s", entry.getValue());

            if (keys.contains("url"))
            {
                content=content.replaceAll(prefixRlue+keys+suffixRlue,HtmlRegexpUtil.EscapeString(values));

            }else if (keys.contains("startregexp"))
            {
                content=content.replaceAll(prefixRlue+keys+suffixRlue,HtmlRegexpUtil.EscapeString(values));

            }else if (keys.contains("endregexp"))
            {
                content=content.replaceAll(prefixRlue+keys+suffixRlue,HtmlRegexpUtil.EscapeString(values));
            }
        }
        return content;
    }
    




    public static List<Object> ExtractCustomText(StringBuffer sbcontent,List<String> Regex) throws Exception {

        boolean isfetch=true;
        int count=0;
        List<Object> values=new ArrayList<Object>();

        for (String re:Regex)
        {
            if (re.equals(";"))
            {
                values.add(count++,"");
                continue;
            }
            String[] tt=re.split(";");
            String text=HtmlRegexpUtil.interceptContent(tt[0],tt[1],sbcontent);
            if (isfetch)
            {
                if (IsFetchData(text))
                {
                    //没有记录，终止爬取
                    break;
                }
                isfetch=false;
            }
            if (StringUtils.isNotBlank(text)) {
                text=HtmlRegexpUtil.filterHtml(text).replaceAll("\\s*", "");
                values.add(count++,text);
            }
            
        }
        
        for(int i=0;i<values.size();i++)
        {
            if (i==2) {
                values.set(i, new Date());
            }
            if (i==6) {
                values.set(i, 0);
            }
            System.out.println(String.format("%s", values.get(i)));
        }
        return values;
    }

    public static boolean IsFetchData(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return true;
        }
        return false;
    }





    public static Map<String, String> parserRuleXml(String ruletext) throws DocumentException {

        HashMap<String, String> confValue = new HashMap<String, String>();
//        File inputXml = new File(ruletext);
//        SAXReader saxReader = new SAXReader();
        Document document = getRootFileDocument(ruletext);
        Element employees =  getSingleNode("//webGather",document);
        for (Iterator<Element> i = employees.elementIterator(); i
                .hasNext(); ) {
            Element employee = (Element) i.next();
            confValue.put(employee.getName(), employee.getText());
        }
        return confValue;
    }

    public static List<String> GetRegexUtils(String fileName) {
        List<String> Regex = new ArrayList<String>();
//        SAXReader saxReader = new SAXReader();
        Document doc =getRootStringDocument(fileName);

        Element element =  getSingleNode("//attributes",doc);

        List<Element> eleColumn = element.elements("basic");
        for (Element ele : eleColumn) {
            String StartRegExp = ele.attribute("StartRegExp").getValue();
            String EndRegExp = ele.attribute("EndRegExp").getValue();
            String name = ele.attribute("name").getValue();
            Regex.add(StartRegExp + ";" + EndRegExp);
        }
        return Regex;
    }

    public static Document getRootStringDocument(String strText)
    {
        SAXReader saxReader =null;
        Document doc = null;
        try
        {
            saxReader = new SAXReader();
            doc = DocumentHelper.parseText(strText);

        } catch (DocumentException e)
        {
            e.printStackTrace();
        }
        return doc;
    }
    public static Element getSingleNode(String expression,Document document)
    {
        return  (Element) document.selectSingleNode(expression);
    }
    public static List<Element> getSelectNodes(String expression,Document document)
    {
        return document.selectNodes(expression);
    }
    public static Document getRootFileDocument(String fileName)
    {
        SAXReader saxReader =null;
        Document doc = null;
        try
        {
            saxReader = new SAXReader();
            doc = saxReader.read(new File(fileName));
        } catch (DocumentException e)
        {
            e.printStackTrace();
        }
        return doc;
    }

    public static void overwriteFileXML(Document document,String filePath,String host)
    {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 利用格式化类对编码进行设置
        format.setEncoding("GBK");
        FileOutputStream output = null;
        XMLWriter writer=null;
        try {
            output = new FileOutputStream(filePath);
            writer = new XMLWriter(output, format);
            writer.write(document);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }finally {
            if(writer!=null)
            {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }


    }

}

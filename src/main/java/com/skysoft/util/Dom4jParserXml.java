package com.skysoft.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

/**
 * Created by tangzy on 2015/1/17.
 */
public class Dom4jParserXml {
    public static Document getRootFileDocument(Class<?> clazz,String filePath)
    {
        SAXReader saxReader =null;
        Document document = null;
        InputStream input=null;
        try
        {
            saxReader = new SAXReader();
            input=clazz.getClassLoader().getResourceAsStream(filePath);
            document = saxReader.read(input);
        } catch (DocumentException ex)
        {
            ex.printStackTrace();
        }finally {
            if(input!=null)
            {
                try {
                    input.close();
                } catch (IOException e) {

                }
            }
        }
        return document;
    }

    public static Element getSingleNode(String expression,Document document)
    {
        return  (Element) document.selectSingleNode(expression);
    }

    public static List<Element> getNodes(String expression,Element element)
    {
        return element.selectNodes(expression);
    }

    public static String getAttributeValue(Element element,String attribute)
    {
        return element.attribute(attribute).getValue();
    }
    public static void writeFormatFileXML(Document document,String filePath,String encoding) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 利用格式化类对编码进行设置
        format.setEncoding(encoding);
        FileOutputStream output = null;
        XMLWriter writer = null;
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
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

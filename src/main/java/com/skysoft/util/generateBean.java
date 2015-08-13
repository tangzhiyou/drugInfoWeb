package com.skysoft.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class generateBean {
    static final Logger LOGGER = LoggerFactory.getLogger(generateBean.class);

    public static Map<Integer, String> methodname(String fileName) {
        Document document = Dom4jParserXml.getRootFileDocument(generateBean.class, fileName);

        String kind = Dom4jParserXml.getSingleNode("//databases/choice", document).getText();
        Element element = Dom4jParserXml.getSingleNode("//databases/" + kind
                + "/mapper", document);

        HashMap<Integer, String> methodMap = new HashMap<Integer, String>();

        List<Element> elements = Dom4jParserXml.getNodes("property", element);
        int count = 0;
        for (Element ele : elements) {
            String propertyName = Dom4jParserXml.getAttributeValue(ele, "name");
            methodMap.put(count++, propertyName);
        }
        return methodMap;
    }

    /**
     * 反射成一个对象
     *
     * @param fileName
     * @return
     */
    public static <T> T newInstance(String fileName) {

        Document document = Dom4jParserXml.getRootFileDocument(generateBean.class, fileName);


        String kind = Dom4jParserXml.getSingleNode("//databases/choice", document).getText();
        Element element = Dom4jParserXml.getSingleNode("//databases/" + kind
                + "/mapper", document);
        String className = Dom4jParserXml.getAttributeValue(element, "class");
        T ret = null;
        try {
            ret = (T) Class.forName(className).newInstance();

        } catch (Exception ex) {
            LOGGER.error("反射一个对象出错", ex);
        }
        return ret;
    }


}

package com.skysoft.util;

import java.io.File;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class generateBean {
	
	/**
	 * 得到所有的方法Map集合
	 * @param fileName
	 * @return
	 */
	static Logger logger=LoggerFactory.getLogger(generateBean.class);
	/*public static Map<Integer, String> methodname(String fileName)
	{
//		XMLEscapeUtils.init();
        List<String> Regex= new ArrayList<String>();
        SAXReader saxReader = new SAXReader();
		Document doc = null;
		try
		{
			doc = saxReader.read(new File(fileName));

		} catch (DocumentException e)
		{
			logger.error("解析XML格式出错", e);
		}

		String kind = doc.selectSingleNode("//databases/choice").getText();
		Element element = (Element) doc.selectSingleNode("//databases/" + kind
				+ "/mapper");
		HashMap<Integer, String> hashtbl = new HashMap<Integer, String>();
		List list1 = element.selectNodes("property");
		Iterator<Element> iterator1 = list1.iterator();
		int count = 0;
		while (iterator1.hasNext())
		{
			Element element1 = iterator1.next();
			String propertyName = element1.attribute("name").getValue();
            String StartRegExp= element1.attribute("StartRegExp").getValue();
            String EndRegExp= element1.attribute("EndRegExp").getValue();
            Regex.add(StartRegExp+":"+EndRegExp);
//            System.out.println("总规则为"+StartRegExp+":"+EndRegExp);
			hashtbl.put(count, propertyName);
			count++;
		}
		return hashtbl;
	}*/

    public static  List<String> GetRegexUtils(String fileName)
    {
//        XMLEscapeUtils.init();
        List<String> Regex= new ArrayList<String>();
        Document doc = XMLEscapeUtils.getRootFileDocument(fileName);

        /*String kind = doc.selectSingleNode("//databases/choice").getText();
        Element element = (Element) doc.selectSingleNode("//databases/" + kind
                + "/mapper");
        HashMap<Integer, String> hashtbl = new HashMap<Integer, String>();
        List list1 = element.selectNodes("property");
        Iterator<Element> iterator1 = list1.iterator();
        while (iterator1.hasNext())
        {
            Element element1 = iterator1.next();
            String StartRegExp= element1.attribute("StartRegExp").getValue();
            String EndRegExp= element1.attribute("EndRegExp").getValue();
            Regex.add(StartRegExp+";"+EndRegExp);
//            System.out.println("总规则为"+StartRegExp+":"+EndRegExp);
        }*/


//        Element element = (Element) doc.selectSingleNode("//attributes");
        Element element = XMLEscapeUtils.getSingleNode("//attributes",doc);
        List<Element> eleColumn= element.elements("basic");
        for (Element ele:eleColumn)
        {
//            System.out.println(ele.getName());
            String StartRegExp= ele.attribute("StartRegExp").getValue();
            String EndRegExp= ele.attribute("EndRegExp").getValue();
            String name=ele.attribute("name").getValue();
            Regex.add(StartRegExp+";"+EndRegExp);
        }
        return Regex;
    }



	/**
	 * 反射成一个对象
	 * @param fileName
	 * @return
	 */
	public static <T> T newInstance(String fileName)
	{

		Document doc=XMLEscapeUtils.getRootFileDocument(fileName);


		String kind = XMLEscapeUtils.getSingleNode("//databases/choice", doc).getText();
		Element element = XMLEscapeUtils.getSingleNode("//databases/" + kind
				+ "/mapper",doc);
		String className = element.attribute("class").getValue();

		T ret = null;
		try
		{
			ret = (T) Class.forName(className).newInstance();

		} catch (Exception e)
		{
			logger.error("反射一个对象出错", e);
		}
		return ret;
	}



    /**
     * 得到所有的方法Map集合
     * @param fileName
     * @return
     */
    public static Map<Integer, String> methodname(String fileName)
    {
        Map<Integer,String> methodMap=new HashMap<Integer, String>();
        int count=0;
        SAXReader saxReader = new SAXReader();
        Document doc =XMLEscapeUtils.getRootFileDocument(fileName);


        Element element = XMLEscapeUtils.getSingleNode("//attributes",doc);
        List<Element> eleColumn= element.elements("basic");
        for (Element ele:eleColumn)
        {
            String name=ele.attribute("name").getValue();
            methodMap.put(count++,name);
        }
        return methodMap;
    }
    public static void main(String[] args)
    {
        methodname("conf/DefaultRule.xml");
    }

}

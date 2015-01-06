package com.skysoft.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: pinaster
 * Date: 13-11-12
 * Time: 下午7:36
 */
public class GenerateRULE {
   public  static void main(String[] args)
   {
        //读取xml文件
	   //请输入规则文件名
	   Scanner sc=new Scanner(System.in);
	   System.out.println("请输入规则名:DefaultRule.xml");
       String fileName="conf/"+sc.nextLine();
//       SAXReader saxReader = new SAXReader();
       Document doc = XMLEscapeUtils.getRootFileDocument(fileName);

       String kind = XMLEscapeUtils.getSingleNode("//databases/choice", doc).getText();

       String pathName=GenerateRULE.class.getProtectionDomain().getCodeSource().getLocation().getPath();
       // *.java
       String rootPath = pathName.substring(0, pathName.indexOf("/target"));
       String JavaPath= rootPath+"/src/main/java/com/skysoft/domain/"+kind+".java";
       handlerData(kind, 0,JavaPath);
       // *.*Mapper.xml
       String MappersPath=rootPath+"/src/main/resources/mappers/"+kind+"Mapper.xml";
       parserXml(MappersPath);
       handlerData(kind,1,MappersPath);

   }
   
   
   public static void parserXml(String fileName) {
//		File inputXml = new File(fileName);
//		SAXReader saxReader = new SAXReader();
		try {
			Document document = XMLEscapeUtils.getRootFileDocument(fileName);
			List<Element> eleList =  XMLEscapeUtils.getSelectNodes("//mapper/insert",document);

			System.out.println(eleList.size());
	        Iterator<Element> eleIter = eleList.iterator();
	        while (eleIter.hasNext()) {
				Element element1 = eleIter.next();
				String propertyName = element1.attribute("id").getValue();
				String propertyType = element1.attribute("parameterType").getValue();
				if (propertyName.equals("insert")) {
					element1.addAttribute("parameterType", "java.util.List");  
				}
				System.out.println(propertyName+":"+propertyType);
				
			}
	        XMLEscapeUtils.overwriteFileXML(document,fileName,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

   

    // 读取配置文件
    public static void handlerData(String fileName,int type,String filepath)
    {
        if (type==0)
        {
             //read *.java
            String content=FileIOStreamTools.readerFileContent(filepath,null);
            int pos=content.indexOf("domain;");
            StringBuffer sbReplace=new StringBuffer(content);
            sbReplace.insert(pos+7," import java.io.Serializable;");
            String Rexp="public class "+fileName;
            System.out.println(Rexp);
            String temp=sbReplace.toString().replaceAll(Rexp, Rexp + " implements Serializable");
            FileIOStreamTools.writeDtatFile(filepath, temp);
        }else if (type==1)
        {
            // read *Mapper.xml
            // 思路写在这里
            // 先把文件截取三部分，头部，待替换，尾部

            //修改xml结点元素
           // generateXML(filepath);

            String content=FileIOStreamTools.readerFileContent(filepath, null);
            int startpos = content.indexOf("<insert id=\"insert\"");
            // 头部内容
            String headContent=content.substring(0,startpos);
            int endpos=content.substring(startpos).indexOf("</insert>");
            // 尾部内容
            String tailContent=content.substring(endpos+startpos+9);
            // 替换内容
            String ReplaceText=content.substring(startpos,endpos+startpos+9);

            StringBuffer sbforeach=new StringBuffer(ReplaceText);

            String ReplaceTemp = sbforeach.toString().replaceAll("#\\{", "#{item.");
            // 先添加foeach
            int pos=ReplaceTemp.indexOf("values ");
            StringBuffer sbfor=new StringBuffer(ReplaceTemp);

            sbfor.insert(pos+7,"<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">");

            int end=sbfor.toString().lastIndexOf(")");
            sbfor.insert(end+2,"</foreach>");
            FileIOStreamTools.writeDtatFile(filepath, headContent + sbfor.toString() + tailContent);

         }
    }




    public static void generateXML(String filePath){
        Document xmlDoc = null;
        //            xmlDoc = sax.read(new File(filePath));
        xmlDoc=XMLEscapeUtils.getRootFileDocument(filePath);
        Element document = xmlDoc.getRootElement();//根节点
//            List<Element> eleList = document.selectNodes("//mapper/insert");// 用xpath查找节点book的属性
        List<Element> eleList=XMLEscapeUtils.getSelectNodes("//mapper/insert",xmlDoc);
        Iterator eleIter = eleList.iterator();
        while (eleIter.hasNext())
        {
            Element element = (Element) eleIter.next();
            String id=String.format("%s", element.attributeValue("id"));
            if (id.equals("insert")) {
                System.out.println(element.attributeValue("parameterType"));
                element.setAttributeValue("parameterType", "java.util.List");
            }
        }

    }

}


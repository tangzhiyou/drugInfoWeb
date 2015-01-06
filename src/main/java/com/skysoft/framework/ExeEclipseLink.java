package com.skysoft.framework;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.skysoft.util.HtmlRegexpUtil;
import com.skysoft.util.XMLEscapeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User:pinaster1129
 * Date:13-12-25
 * Time:下午10:52
 */
public class ExeEclipseLink {
    public static void main(String[] args) {
        new ExeEclipseLink().initDatabase();
//        Object beans=null;
//        List<Object> listText=new ArrayList<Object>();
//        listText.add(0,"中华人ArtTitle民共和国");
//        listText.add(1,"中华人Keywordes民共和国");
//        listText.add(2,new Date());
//        listText.add(3,"中华人ArtSource民共和国");
//        listText.add(4,"中华人民ArtSummary和国");
//        listText.add(5,"中华人民ArtContent共和国");
//        listText.add(6,1);
//        try {
//            beans=ReflectUtils.toEntityList(XPathParserXml.getPackageName("templates/dborm.xml"), listText, generateBean.methodname("templates/dborm.xml"));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        SaveNewSinfoDao((drugNews)beans);
    }
    public void initDatabase()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("drugInfoWeb");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }
    public static void SaveNewSinfoDao(Object beans)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("drugInfoWeb");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(beans);
        em.getTransaction().commit();
        em.close();
        
    }
    
    public static String ReadFileContent(String filePath)
    {
        StringBuffer sbContent=null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    filePath), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String urlText;
            sbContent=new StringBuffer();
            while ((urlText = br.readLine()) != null) {
                sbContent.append(urlText);
            }
            br.close();
            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sbContent.toString();
    }
    public static void handlerData(String text)
    {
        //得到文本的内容 
        text = HtmlParserTool.extractHtmlLabel(text, "body");
//        text = XMLEscapeUtils.replaceIllegalCharacter(text);

        StringBuffer sbcontent = new StringBuffer(text);
        
        //得到规则的格式
//        List<String> regexes= HtmlRegexpUtil.GetRegexUtils(XMLEscapeUtils.FormatXMLValue());
        //得到正确的文本
        List<Object> listText=null;
        try {
//            listText=XMLEscapeUtils.ExtractCustomText(sbcontent, regexes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        String filePath=ExeEclipseLink.class.getResource(".").getPath();
//        filePath = filePath.substring(0, filePath.indexOf("/bin/"))
//                + "\\commons\\src\\main\\resources\\templates\\dborm.xml";
        //反射到一个对明，并且给方法 赋值
        
        Object beans=null;
        try {
//            beans=ReflectUtils.toEntityList(XPathParserXml.getPackageName("templates/dborm.xml"), listText, generateBean.methodname("templates/dborm.xml"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //把数据把保存到数据库中..
        ExeEclipseLink.SaveNewSinfoDao(beans);
    }
}

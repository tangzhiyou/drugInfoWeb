//package com.skysoft.main;
//
//import com.skysoft.util.FileIOStreamTools;
//import com.skysoft.util.FreeMarkertUtil;
//import com.skysoft.util.XPathParserXml;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.*;
//import java.util.*;
//
///**
// * User: pinaster
// * Date: 13-12-23
// * Time: 下午1:14
// */
//public class FreeMarkerEntity {
//    public static void main(String[] args) throws Exception {
//
//        new FreeMarkerEntity().AutoGenerateEntity();
//    }
//
//    public void AutoGenerateEntity() throws Exception{
//
//        InputStream in =FreeMarkerEntity.class.getResourceAsStream("/templates/dborm.xml");
//        //下面是FreeMarker的输入接口，这里与上面的in流建立关系，
//        InputSource ins=new InputSource(in);
//        Map<String, Object> data = new HashMap<String, Object>();
//
//        String filePath=FreeMarkerEntity.class.getResource(".").getPath();
//        filePath=filePath.substring(0, filePath.indexOf("/bin/"))+"\\commons\\src\\main\\resources\\templates\\dborm.xml";
//
//        String text= FileIOStreamTools.ReadFileContent(filePath);
//        text.replaceAll("\\r\\n", "");
//        // NotNull
//        if (XPathParserXml.ImportPackages(text, "<NotNull/>"))
//        {
//            data.put("NotNull","import com.sun.istack.internal.NotNull;");
//        }
//
//        // DATE
//        if (XPathParserXml.ImportPackages(text,"<temporal>\\w+</temporal>"))
//        {
//            data.put("DATE","import org.springframework.format.annotation.DateTimeFormat;\n" +
//                    "import java.util.Date;");
//        }
//        System.out.println(filePath);
//        String templatePath=filePath.substring(0,filePath.indexOf("\\templates\\")+10);
//        String fileName=FreeMarkerEntity.class.getResource(".").getPath();
//        fileName=fileName.substring(0, fileName.indexOf("/bin/"))+"\\commons\\src\\main\\java\\com\\skyinfo\\domain\\"+XPathParserXml.getClassName(filePath).concat(".java");
//        data.put("doc", freemarker.ext.dom.NodeModel.parse(ins));
//        data.put("list",XPathParserXml.XMLtoPOJO());
//        FreeMarkertUtil.analysisTemplate(templatePath, "dborm.ftl", fileName, data);
//    }
//    public void temp() throws Exception
//    {
//      //得FreeMarker配置对象
//        Configuration cfg = new Configuration();
//        cfg.setEncoding(Locale.getDefault(), "UTF-8");
//        cfg.setClassForTemplateLoading(this.getClass(),"/templates");
//        //得FreeMarker的关键对象———模板
//        Template temp = cfg.getTemplate("dborm.ftl");
//
//        InputStream in =FreeMarkerEntity.class.getResourceAsStream("/templates/dborm.xml");
//        //下面是FreeMarker的输入接口，这里与上面的in流建立关系，
//        InputSource ins=new InputSource(in);
//        Map<String, Object> data = new HashMap<String, Object>();
//
//        String text= FileIOStreamTools.ReadFileContent("/home/pinaster/IdeaProjects/AutoGenerateEntity/src/main/resources/templates/dborm.xml");
//        text.replaceAll("\\r\\n", "");
//        // NotNull
//        if (XPathParserXml.ImportPackages(text,"<NotNull/>"))
//        {
//            data.put("NotNull","import com.sun.istack.internal.NotNull;");
//        }
//
//        // DATE
//        if (XPathParserXml.ImportPackages(text,"<temporal>\\w+</temporal>"))
//        {
//            data.put("DATE","org.springframework.format.annotation.DateTimeFormat;");
//        }
//
//        //这里读取xml文件，并处理成root对象
//        data.put("doc", freemarker.ext.dom.NodeModel.parse(ins));
//        data.put("list",XPathParserXml.XMLtoPOJO());
//
//        //建立内存字符串流
//        Writer out = new OutputStreamWriter(System.out);
//        //模板开始按模板中的要求把用户输入的数据进行转换，并输出到字符串流中
//        temp.process(data, out);
//        out.flush();
//    }
//}

package com.skysoft.util;

import com.skysoft.domain.AnnotationFields;
import freemarker.template.utility.StringUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* User: pinaster Date: 13-12-23 Time: 下午2:10
*/
public class XPathParserXml {
    public static void main(String[] args) {
/*        try {
            System.out.println(new File(".").getCanonicalFile());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String filePath = XPathParserXml.class.getResource(".").getPath();
        filePath = filePath.substring(0, filePath.indexOf("/bin"))
                + "\\commons\\src\\main\\resources\\templates";
        System.out.println(filePath);
        System.out.println(XPathParserXml.class.getResource(".").getPath());

        System.out.println(System.getProperty("user.dir"));*/
        try {
            System.out.println(getPackageName("templates/dborm.xml"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 导入的相关包处理
    public static Boolean ImportPackages(String content, String Regex) {
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        if (result1) {
            sb.append(matcher.group());
        }
        if (StringUtils.isBlank(sb)) {
            return false;
        }
        return true;
    }

    public static List<AnnotationFields> XMLtoPOJO() throws Exception {
        // ID
        List<AnnotationFields> afieldses = new ArrayList<AnnotationFields>();
//        String filePath = XPathParserXml.class.getResource(".").getPath();
//        filePath = filePath.substring(0, filePath.indexOf("/bin/"))
//                + "\\commons\\src\\main\\resources\\templates\\dborm.xml";
        afieldses.add(getIDField("templates/dborm.xml"));
        Document doc = XMLEscapeUtils.getRootFileDocument("templates/dborm.xml");
        Element element = XMLEscapeUtils.getSingleNode("//entity/attributes", doc);
        List<Element> nodesList = element.selectNodes("basic");
        String propertyName;
        for (Element columnlist : nodesList) {
            AnnotationFields Fields = new AnnotationFields();
            propertyName = columnlist.attribute("name").getValue();
            Fields.setJavaField(propertyName);
            System.out.print("属性名:" + propertyName);
            for (Iterator it = columnlist.elementIterator(); it.hasNext();) {
                Element elecolumn = (Element) it.next();
                if (elecolumn.getName().equals("lob")) {
                    continue;
                }

                if (elecolumn.getName().equals("temporal")) {
                    Fields.setDataType("");
                    Fields.setJpaField("@Temporal(TemporalType.TIMESTAMP)\n"
                            + "    @DateTimeFormat(style = \"M-\")");
                    Fields.setJavaType("Date");
                    continue;

                }
                propertyName = elecolumn.attribute("name").getValue();
                Fields.setDataField(propertyName);
                String definitionName = elecolumn.valueOf("@column-definition");
                if (definitionName.toUpperCase().equals("LONGTEXT")) {
                    Fields.setDataType("LONGTEXT");
                    Fields.setJpaField("@Lob\n"
                            + "    @Basic(fetch = FetchType.LAZY)");
                    Fields.setJavaType("String");
                } else if (definitionName.toUpperCase().equals("BLOB")) {
                    Fields.setDataType("BLOB");
                    Fields.setJpaField("@Lob\n"
                            + "    @Basic(fetch = FetchType.LAZY)");
                    Fields.setJavaType("byte[]");
                }
                else if (definitionName.toUpperCase().equals("INTEGER")) {
                    Fields.setDataType("");
                    Fields.setJpaField("");
                    Fields.setJavaType("int");

                }else {
                    Fields.setDataType("");
                    Fields.setJpaField("");
                    Fields.setJavaType("String");
                }

            }
            afieldses.add(Fields);
        }
        return afieldses;
    }

    public static AnnotationFields getIDField(String filePath) throws Exception {
        AnnotationFields afields = new AnnotationFields();
        Document doc = XMLEscapeUtils.getRootFileDocument(filePath);
        Element database = XMLEscapeUtils.getSingleNode("//id",doc);
        String propertyName = database.attribute("name").getValue();
        afields.setJavaField(propertyName);
        database = XMLEscapeUtils.getSingleNode("//id/column",doc);
        propertyName = database.attribute("name").getValue();
        afields.setDataField(propertyName);
        database = XMLEscapeUtils.getSingleNode("//id/generated-value",doc);
        propertyName = database.attribute("strategy").getValue();
        afields.setDataType("");
        afields.setJavaType("long");
        afields.setJpaField("@Id\n"
                + "    @GeneratedValue(strategy = GenerationType.SEQUENCE)");
        return afields;
    }

    public static String getClassName(String filePath) throws Exception {
        Document doc = XMLEscapeUtils.getRootFileDocument(filePath);
        Element database = XMLEscapeUtils.getSingleNode("//entity",doc);
        String propertyName = database.attribute("class").getValue();
        return propertyName;
    }

    // 获得反射类的全名
    public static String getPackageName(String filePath) throws Exception {
        Document doc = XMLEscapeUtils.getRootFileDocument(filePath);
        String ClassNamePath = XMLEscapeUtils.getSingleNode("//entity/package",doc)
                .getText();
        Element database = XMLEscapeUtils.getSingleNode("//entity",doc);
        String propertyName = database.attribute("class").getValue();
        return ClassNamePath + "." + propertyName;
    }

    public String getFieldType(String value) throws Exception {

        Document doc = XMLEscapeUtils.getRootFileDocument(value);

        Element element = XMLEscapeUtils.getSingleNode("//entity/attributes",doc);
        List<Element> list1 = element.elements("basic");
        Iterator<Element> iterator1 = list1.iterator();
        int count = 0;
        while (iterator1.hasNext()) {
            Element element1 = iterator1.next();
            String propertyName = element1.attribute("name").getValue();
            String fetchName = element1.attribute("fetch").getValue();

            count++;
        }

        String regString = "varchar|varchar2|char";
        Pattern pStr = Pattern.compile(regString);
        Matcher mStr = pStr.matcher(value);
        if (mStr.find()) {
            return "String";
        }

        String regNumber = "number";
        Pattern pNum = Pattern.compile(regNumber);
        Matcher mNum = pNum.matcher(value);
        if (mNum.find()) {
            return "double";
        }

        return null;
    }
    public static String CharacterEncoding(String filePath)throws Exception
    {
        Document doc = XMLEscapeUtils.getRootFileDocument(filePath);
        String characterset = XMLEscapeUtils.getSingleNode("//webGather/characterset",doc)
                .getText();
        return characterset;
    }
    public static String websitedomain(String filePath)throws Exception
    {
        Document doc = XMLEscapeUtils.getRootFileDocument(filePath);
        String websitedomain = XMLEscapeUtils.getSingleNode("//webGather/SiteDomain",doc)
                .getText();
        return websitedomain;

    }
    public static void TestGenXML(String content)
    {
        Document doc =XMLEscapeUtils.getRootStringDocument(content);
        Element element = XMLEscapeUtils.getSingleNode("//drugnewsinfo/mapper", doc);
        List<Element> nodesList = element.elements("property");
        for (Element columnlist : nodesList)
        {
            String StartRegExp = columnlist.attribute("StartRegExp").getValue();
            String EndRegExp = columnlist.attribute("EndRegExp").getValue();
            System.out.println(StartRegExp+"************:"+EndRegExp);
        }

    }

}

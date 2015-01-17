package com.skysoft.test;

import com.skysoft.util.FileIOStreamTools;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: pinaster
 * Date: 13-12-18
 * Time: 下午3:17
 */
public class TestJavaXml {
    public static void main(String[] args) throws IOException {

    }
    public void init() throws IOException {
//        InputStream instream = this.getClass().getClassLoader().getResourceAsStream("conf/person.xml");
//        String oldXML = new String(TestJavaXml.read(instream), "UTF-8");
        String odlXML= FileIOStreamTools.readerFileContent("conf/person.xml",null);
        String newXML = odlXML.replaceAll("\\$name", "<h1 class=\"style10\">").replaceAll("\\$age","22");
        System.out.println(newXML);
    }


}

package com.skysoft.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * User: pinaster
 * Date: 13-12-24
 * Time: 下午4:50
 */
public class FreeMarkertUtil {

    //templatePath模板文件存放路径
    //templateName 模板文件名称
    //filename 生成的文件名称
    public static void analysisTemplate(String templatePath, String templateName, String fileName, Map<?, ?> root) {
        try {
            Configuration cfg=new Configuration();
            //设置要解析的模板所在的目录，并加载模板文件
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
            //设置包装器，并将对象包装为数据模型
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            //获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
            cfg.setLocale(Locale.getDefault());
            cfg.setDefaultEncoding("UTF-8");
            Template template=cfg.getTemplate(templateName,"UTF-8");
            Writer out =new FileWriter(fileName);
            template.process(root, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
    
    public static Configuration getConfiguration(String templateDir) throws IOException
    {
        return null;
    }
}

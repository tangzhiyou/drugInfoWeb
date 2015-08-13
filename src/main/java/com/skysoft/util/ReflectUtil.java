package com.skysoft.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

public class ReflectUtil {


    public static void executeMethod(Object source, String methodname)
            throws Exception {
        // 获取method对象
        Method m = source.getClass().getMethod(methodname);
        // 执行一个无参数的方法
        m.invoke(source);
    }

    public static void setReflectField(Object source, String methodname,
                                       Object value) throws Exception {
        if (methodname != null && !methodname.equals("")) {
            if (!methodname.startsWith("set")) {// 检查是否以set开头
                methodname = "set" + methodname.substring(0, 1).toUpperCase()
                        + methodname.substring(1, methodname.length());
            }
            Map<String, Method> methods = new HashMap<String, Method>();
            getSetMethodFromObject(methods, source);// 获取方法
            Method m = methods.get(methodname);
            m.invoke(source, value);

        }
    }

    public static void getSetMethodFromObject(Map<String, Method> methods,
                                              Object source) {
        Method[] tempmethods = source.getClass().getMethods();
        for (int i = 0; i < tempmethods.length; i++) {
            if (tempmethods[i].getName().startsWith("set")) {
                methods.put(tempmethods[i].getName(), tempmethods[i]);
            }
        }
    }

    public static Object toEntityList(Object obj, List list, Map tempmap)
            throws Exception {
        String fieldname = null;
        Iterator iter = tempmap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            fieldname = String.format("%s", entry.getValue().toString());
            setReflectField(obj, fieldname,
                    list.get(Integer.parseInt(key.toString())));
        }
        System.out.println("如果得到object对象:" + obj);
        return obj;
    }

    public static Object ReflectToEntity(String className, List<String> methodList, List<Object> values) throws Exception {
        System.out.println("方法名的个数" + methodList.size() + "参数值的个数" + values.size());
        Class<?> searchType = null;
        Object beans = null;

        try {
            searchType = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            beans = searchType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < methodList.size(); i++) {
            String methodName = String.format("%s", methodList.get(i));
            if (!methodName.startsWith("set")) {
                methodName = "set" + methodName.substring(0, 1).toUpperCase()
                        + methodName.substring(1, methodName.length());
            }
            Method m = ReflectionUtils.findMethod(searchType, methodName, null);
            ReflectionUtils.invokeMethod(m, beans, values.get(i));
        }
        System.out.println("得到映射实体成功" + beans);
        return beans;
    }

    public static String packageName(String filePath) {
        Document document = Dom4jParserXml.getRootFileDocument(ReflectUtil.class, filePath);
        String classpackageName = "";

        Element packageName = Dom4jParserXml.getSingleNode("//mapper", document);
        classpackageName = packageName.attribute("class").getValue();
        return classpackageName;
    }

    public static List<String> methodList(String filePath) {
        Document document = Dom4jParserXml.getRootFileDocument(ReflectUtil.class, filePath);
        List<String> methodList = new ArrayList<String>();
        Element element = Dom4jParserXml.getSingleNode("//druggds/mapper", document);

        List<Element> elecolumn = element.elements("property");
        for (Element ele : elecolumn) {
            String propertyName = ele.attribute("name").getValue();
            methodList.add(propertyName);
        }
        return methodList;
    }
}
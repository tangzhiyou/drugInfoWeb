package com.skysoft.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReflectUtil {
    public static void main(String[] args)
    {

         List<Object> listText=new ArrayList<Object>();
        listText.add(0,"北京市,drugName中国人民共和国");
        listText.add(1,"北京市,dosage中国人民共和国");
        listText.add(2,"北京市,dosageComment中国人民共和国");
        listText.add(3,"北京市,specifications中国人民共和国");
        listText.add(4,"北京市,unit中国人民共和国");
        listText.add(5,"北京市,referencePrice中国人民共和国");
        listText.add(6,0);
        Object beans=null;
        try {
            beans=ReflectToEntity(packageName("conf/DefaultRule.xml"),methodList("conf/DefaultRule.xml"),listText);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        drugnewsinfo tt=(drugnewsinfo)beans;

//        System.out.println(tt.getEffectTime()+":"+tt.getDosage()+":"+tt.getDrugName());
//        System.out.println(tt.getArtContent()+":"+tt.getArtSummary()+":"+tt.getArtTitle());

    }

	public static void executeMethod(Object source, String methodname)
			throws Exception
	{
		// 获取method对象
		Method m = source.getClass().getMethod(methodname);
		// 执行一个无参数的方法
		m.invoke(source);
	}

	public static void setReflectField(Object source, String methodname,
			Object value) throws Exception
	{
		if (methodname != null && !methodname.equals(""))
		{
			if (!methodname.startsWith("set"))
			{// 检查是否以set开头
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
			Object source)
	{
		Method[] tempmethods = source.getClass().getMethods();
		for (int i = 0; i < tempmethods.length; i++)
		{
			if (tempmethods[i].getName().startsWith("set"))
			{
				methods.put(tempmethods[i].getName(), tempmethods[i]);
			}
		}
	}

	public static Object toEntityList(Object obj, List list, Map tempmap)
			throws Exception
	{
		String fieldname = null;
		Iterator iter = tempmap.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			fieldname = String.format("%s", entry.getValue().toString());
			setReflectField(obj, fieldname,
					list.get(Integer.parseInt(key.toString())));
		}
		System.out.println("如果得到object对象:" + obj);
		return obj;
	}
    public static Object ReflectToEntity(String className,List<String> methodList,List<Object> values)throws Exception
    {
        System.out.println("方法名的个数"+methodList.size()+"参数值的个数"+values.size());
        Class<?> searchType = null;
        Object beans=null;

        try {
            searchType=Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            beans=searchType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i=0;i<methodList.size();i++)
        {
            String methodName=String.format("%s",methodList.get(i));
            if (!methodName.startsWith("set"))
            {
                methodName = "set" + methodName.substring(0, 1).toUpperCase()
                        + methodName.substring(1, methodName.length());
            }
            Method m=ReflectionUtils.findMethod(searchType,methodName,null);
            ReflectionUtils.invokeMethod(m,beans,values.get(i));
        }
        System.out.println("得到映射实体成功"+beans);
        return beans;
    }

    public static String packageName(String filePath)
    {
        Document doc=XMLEscapeUtils.getRootFileDocument(filePath);
        String classpackageName="";

        Element packageName =  XMLEscapeUtils.getSingleNode("//mapper",doc);
        classpackageName= packageName.attribute("class").getValue();
        return  classpackageName;
    }
    public static List<String> methodList(String filePath)
    {
        Document doc=XMLEscapeUtils.getRootFileDocument(filePath);
        List<String> methodList=new ArrayList<String>();
        Element element =  XMLEscapeUtils.getSingleNode("//druggds/mapper",doc);

        List<Element> elecolumn =element.elements("property");
        for (Element ele:elecolumn)
        {    String propertyName = ele.attribute("name").getValue();
            methodList.add(propertyName);
        }
        return methodList;
    }
}
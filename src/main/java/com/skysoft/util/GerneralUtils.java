package com.skysoft.util;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ͨ�ú���������
 *
 */
@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class GerneralUtils {
    private static final String HEXES = "0123456789ABCDEF";
    private static final Map<String, Integer> monthMap = new HashMap<String, Integer>();

    private static Random random = new Random();

    static {
        monthMap.put("January", 0);
        monthMap.put("February", 1);
        monthMap.put("March", 2);
        monthMap.put("April", 3);
        monthMap.put("May", 4);
        monthMap.put("June", 5);
        monthMap.put("July", 6);
        monthMap.put("August", 7);
        monthMap.put("September", 8);
        monthMap.put("October", 9);
        monthMap.put("November", 10);
        monthMap.put("December", 11);
    }

    /**
     * ������������ע��
     */
    private final static List<Class<?>> PRIMITIVE_CLASSES = new ArrayList<Class<?>>() {
        {
            add(Long.class);
            add(Integer.class);
            add(Short.class);
            add(String.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
            add(java.util.Date.class);
            add(java.sql.Date.class);
            add(java.sql.Timestamp.class);
        }
    };

    public static Map<String, Integer> getMonthmap() {
        return monthMap;
    }

    /**
     * �ж��ַ���Ϊ��(����null��"")
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (null == str || "".equals(str));
    }

    /**
     * �ж��ַ����ǿ�
     *
     * @param str
     * @return
     */
    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    /**
     * �жϼ��Ϸǿ�
     *
     * @param collection
     * @return
     */
    public static boolean isEmptyCollection(Collection collection) {
        return (null == collection || collection.isEmpty());
    }

    /**
     * �жϼ��Ϸǿ�
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmptyCollection(Collection collection) {
        return !isEmptyCollection(collection);
    }

    /**
     * �ж�ĳ�������Ƿ��������Ԫ��
     *
     * @param targetCollection
     * @param original
     * @return
     */
    public static boolean isContains(Collection targetCollection,
                                     Object original) {
        return targetCollection.contains(original);
    }

    /**
     * �ж�MapΪ��
     *
     * @param map
     * @return
     */
    public static boolean isEmptyMap(Map map) {
        return (null == map || map.isEmpty());
    }

    /**
     * �ж�Map�ǿ�
     *
     * @param map
     * @return
     */
    public static boolean isNotEmptyMap(Map map) {
        return !isEmptyMap(map);
    }

    /**
     * �ж�����Ϊ��
     *
     * @param array
     * @return
     */
    public static boolean isEmptyArray(Object[] array) {
        return null == array || array.length == 0;
    }

    /**
     * �ж�����ǿ�
     *
     * @param array
     * @return
     */
    public static boolean isNotEmptyArray(Object[] array) {
        return !isEmptyArray(array);
    }

    /**
     * ȥ���������ظ�Ԫ��
     *
     * @param array
     * @return
     */
    public static Object[] arrayUnique(Object[] array) {
        List<Object> list = new LinkedList<Object>();
        for (int i = 0; i < array.length; i++) {
            if (!list.contains(array[i])) {
                list.add(array[i]);
            }
        }
        return list.toArray();
    }


    /**
     * ɾ��������ָ��Ԫ��
     *
     * @param collection
     * @return
     */
    public static boolean deleteElementFromCollection(Collection<Object> collection, Object targetEl) {
        if (isEmptyCollection(collection)) {
            return false;
        }
        for (Iterator<Object> it = collection.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (obj.equals(targetEl)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * ɾ��Map��ָ��key��Ӧ�ļ�ֵ��
     *
     * @param map
     * @param key
     */
    public static boolean deleteElementFromMap(Map<String, Object> map, String targetKey) {
        if (isEmptyMap(map) || null == targetKey) {
            return false;
        }
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
            String key = it.next().getKey();
            if (key.equals(targetKey)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * ɾ������ĳ����λ�õ�Ԫ�ز�����ɾ�����������
     *
     * @param array
     * @param delItem
     * @return
     */
    public static Object[] deleteElementFromArray(Object[] array, int delItem) {
        if (isEmptyArray(array) || delItem < 0 || delItem >= array.length) {
            return array;
        }
        for (int i = delItem; i < array.length - 1; ++i) {
            array[i] = array[i + 1];
        }
        Object[] arrayTemp = new Object[array.length - 1];
        System.arraycopy(array, 0, arrayTemp, 0, array.length - 1);
        array = null;
        return arrayTemp;
    }

    /**
     * ������������Ԫ�ذ���ָ�������ӷ�ƴ�ӳ�һ���ַ���
     * �����ָ�����ӷ���Ĭ������ΪӢ�Ķ���,
     *
     * @param array     �������
     * @param delimiter ���ӷ�
     * @return
     */
    public static String joinArray(Object[] array, String delimiter) {
        if (isEmptyArray(array)) {
            return null;
        }
        if (isEmptyString(delimiter)) {
            delimiter = ",";
        }
        StringBuffer text = new StringBuffer();
        for (Object element : array) {
            String str = "";
            if (null != element) {
                str = element.toString();
            }
            text.append(str).append(delimiter);
        }
        return text.toString().replaceAll(delimiter + "$", "");
    }

    /**
     * ������������Ԫ�ذ���ָ�������ӷ�ƴ�ӳ�һ���ַ���(����)
     * �����ָ�����ӷ���Ĭ������ΪӢ�Ķ���,
     *
     * @param array     �������
     * @param delimiter ���ӷ�
     * @return
     */
    public static String joinArray(Object[] array) {
        return joinArray(array, null);
    }

    /**
     * ������������Ԫ�ذ���ָ�������ӷ�ƴ�ӳ�һ���ַ���
     * �����ָ�����ӷ���Ĭ������ΪӢ�Ķ���,
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String joinCollection(Collection collection, String delimiter) {
        if (isEmptyCollection(collection)) {
            return null;
        }
        if (isEmptyString(delimiter)) {
            delimiter = ",";
        }
        StringBuffer text = new StringBuffer();
        for (Object element : collection) {
            String str = "";
            if (null != element) {
                str = element.toString();
            }
            text.append(str).append(delimiter);
        }
        return text.toString().replaceAll(delimiter + "$", "");
    }

    /**
     * ������������Ԫ�ذ���ָ�������ӷ�ƴ�ӳ�һ���ַ���(����)
     * �����ָ�����ӷ���Ĭ������ΪӢ�Ķ���,
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String joinCollection(Collection collection) {
        return joinCollection(collection, null);
    }

    /**
     * List����N�ȷ�<br/>
     * �����������������������һ��
     *
     * @param list
     * @param splitCOunt �ָ�������ֳ�splitCOunt��
     * @return
     */
    public static List<List> splitList(List list, int splitCOunt) {
        int start = 0;
        List<List> params = new ArrayList<List>();
        int t = list.size() / splitCOunt;
        int pageSize = list.size() % splitCOunt == 0 ? t : t + 1;
        while (start < list.size() - 1) {
            int n = list.size() - start;
            List subList = null;
            if (n > splitCOunt) {
                int end = start + pageSize;
                if (end > list.size()) {
                    end = list.size();
                }
                subList = list.subList(start, end);
            } else {
                subList = list.subList(start, start + n);
            }
            params.add(subList);
            start += pageSize;
        }
        return params;
    }

    /**
     * �жϴ����ַ����Ƿ��������
     *
     * @param text
     * @return
     */
    public static boolean containsNum(String text) {
        boolean isNumber = false;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                isNumber = true;
                break;
            }
        }
        return isNumber;
    }

    /**
     * �ж��ַ����Ƿ�ȫ���ɴ�д��ĸ���
     *
     * @param str
     * @return
     */
    public static boolean isAllUpperCase(String str) {
        if (isEmptyString(str)) {
            return false;
        }
        String temp = str.replaceAll("[A-Z]", "");
        return temp.length() == 0;
    }

    /**
     * �ж��ַ����Ƿ�ȫ����Сд��ĸ���
     *
     * @param str
     * @return
     */
    public static boolean isAllLowerCase(String str) {
        if (isEmptyString(str)) {
            return false;
        }
        String temp = str.replaceAll("[a-z]", "");
        return temp.length() == 0;
    }

    /**
     * �ж��ַ����Ƿ�ȫ��������(����С����)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isEmptyString(str) || (str.length() > 1 && str.startsWith("0"))) {
            return false;
        }
        //JDK�Դ�����ʵ��
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
        /*//�ж�ASC��ʵ��
        for(int i=str.length();--i >= 0;){
	        int chr = str.charAt(i);   
	        if(chr < 48 || chr > 57) {   
	           return false;   
	        }
	    }
	    return true;*/
    }

    /**
     * �ж��ַ����Ƿ����ת����С��
     *
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        if (isEmptyString(str) || str.indexOf(".") == -1 ||
                str.startsWith(".") || str.endsWith(".") ||
                (str.indexOf(".") != str.lastIndexOf("."))) {
            return false;
        }
        //�ж��Ƿ��з������ַ�
        String temp = str.replaceAll("[\\d|.]*", "");
        if (temp.length() != 0) {
            return false;
        }
        //�ų�����000.12��ʽ
        if (str.indexOf(".") > 1) {
            String firstLetter = str.substring(0, 1);
            if (firstLetter.equals("0")) {
                return false;
            }
        }
        return true;
    }

    /**
     * ���ַ�������ȡ����(��С��),�磺Was $365.00--365.00
     *
     * @param str
     * @return
     */
    public static String getNumberFromString(String str) {
        if (isEmptyString(str)) {
            throw new RuntimeException("Unable to convert " + str + " to a number.");
        }
        return str.replaceAll("[^\\d|.]*", "");
    }

    /**
     * �ַ����޳��ظ���磺aaaaabbbbbccccc-->abc
     * ע�⣺�ݲ�֧�ֽ����ظ����磺aaaaabbbaaaccc-->bac������abac
     *
     * @param str
     * @return
     */
    public static String excludeRepeat(String str) {
        if (isEmptyString(str)) {
            return str;
        }
        return str.replaceAll("(?s)(.)(?=.*\\1)", "");
    }

    /**
     * ����ָ������[min-max)֮��������
     *
     * @param max
     * @param min
     * @return
     */
    public static int generateRandomNumber(int max, int min) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * �ж�ĳ�ַ����Ƿ���������ַ�
     *
     * @param text
     * @return
     */
    public static boolean containsChinese(String text) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    /**
     * �ж��ַ����Ƿ�Ϊ���ڸ�ʽ
     *
     * @param dateString
     * @return
     */
    public static boolean isDate(String dateString) {
        if (GerneralUtils.isEmptyString(dateString)) {
            return false;
        }
        DateFormat df = null;
        for (String pattern : Constant.PATTERNS) {
            df = new SimpleDateFormat(pattern);
            df.setLenient(false);
            Date date = null;
            try {
                date = df.parse(dateString);
                if (null != date) {
                    return true;
                }
            } catch (ParseException e) {
                continue;
            }
        }
        return false;
    }

    /**
     * �ж�ָ�������Ƿ��ǻ�����������
     *
     * @param clazz
     * @return
     */
    public final static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || PRIMITIVE_CLASSES.contains(clazz);
    }

    /**
     * �ַ�������ĸת���ɴ�д
     *
     * @param text
     * @return
     */
    public static String upperFirstLetter(String text) {
        if (GerneralUtils.isEmptyString(text)) {
            return text;
        }
        String first = Character.toString(text.charAt(0)).toUpperCase();
        return new StringBuilder(text.length()).append(first)
                .append(text.substring(1)).toString();
    }

    /**
     * �ַ�������ĸת����Сд
     *
     * @param text
     * @return
     */
    public static String lowerFirstLetter(String text) {
        if (GerneralUtils.isEmptyString(text)) {
            return text;
        }
        String first = Character.toString(text.charAt(0)).toLowerCase();
        return new StringBuilder(text.length()).append(first)
                .append(text.substring(1)).toString();
    }

    /**
     * ��get/set����������ȡ����������
     *
     * @return
     */
    public static String getPropertyNameFromGetSet(String methodName) {
        if (null == methodName) {
            return null;
        }
        if (methodName.length() <= 3
                || (!methodName.startsWith("get") && !methodName
                .startsWith("set"))) {
            return methodName;
        }
        return lowerFirstLetter(methodName.substring(3));
    }

    /**
     * ����������ת�������ݿ��ֶ�����������studentName-->student_name
     *
     * @param propertyName ��������
     * @param prefix       ���ǰ׺
     * @param stuffix      ��Ӻ�׺
     * @return
     */
    public static String splitCamelName(String propertyName, String prefix,
                                        String stuffix) {
        if (GerneralUtils.isEmptyString(propertyName)) {
            return propertyName;
        }
        char[] dest = new char[propertyName.length()];
        propertyName.getChars(0, propertyName.length(), dest, 0);
        StringBuilder builder = new StringBuilder();
        if (isNotEmptyString(prefix)) {
            builder.append(prefix).append("_");
        }
        for (int i = 0; i < dest.length; i++) {
            if (i == 0) {
                builder.append(Character.toLowerCase(dest[i]));
                continue;
            }
            if (Character.isUpperCase(dest[i])) {
                builder.append("_").append(Character.toLowerCase(dest[i]));
            } else {
                builder.append(dest[i]);
            }
        }
        if (isNotEmptyString(stuffix)) {
            builder.append("_").append(stuffix);
        }
        return builder.toString();
    }

    /**
     * ����������ת�������ݿ��ֶ�������(����)����studentName-->student_name
     *
     * @param propertyName ��������
     * @return
     */
    public static String splitCamelName(String propertyName) {
        return splitCamelName(propertyName, null, null);
    }

    /**
     * ���ݿ��ֶ�����ת����������������stu_name-->stuName
     *
     * @param fieldName ���ݿ��ֶ�����
     * @param prefix    ǰ׺
     * @param stuffix   ��׺
     * @return
     */
    public static String splitDBFieldName(String fieldName, String prefix,
                                          String stuffix) {
        if (GerneralUtils.isEmptyString(fieldName)) {
            return fieldName;
        }
        if (isNotEmptyString(prefix)) {
            if (prefix.endsWith("_")) {
                fieldName = fieldName.replaceAll("^" + prefix + "(.*)", "$1");
            } else {
                fieldName = fieldName.replaceAll("^" + prefix + "_(.*)", "$1");
            }
        } else {
            fieldName = fieldName.replaceAll("^_(.*)", "$1");
        }
        if (isNotEmptyString(stuffix)) {
            if (stuffix.startsWith("_")) {
                fieldName = fieldName.replaceAll("(.*)" + stuffix + "$", "$1");
            } else {
                fieldName = fieldName.replaceAll("(.*)_" + stuffix + "$", "$1");
            }
        } else {
            fieldName = fieldName.replaceAll("(.*)_$", "$1");
        }
        if (fieldName.indexOf("_") == -1) {
            return fieldName;
        }
        String[] array = fieldName.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                builder.append(array[i].toLowerCase());
            } else {
                builder.append(upperFirstLetter(array[i]));
            }
        }
        return builder.toString();
    }

    /**
     * ���ݿ��ֶ�����ת������������(����)����stu_name-->stuName
     *
     * @param fieldName ���ݿ��ֶ�����
     * @return
     */
    public static String splitDBFieldName(String fieldName) {
        return splitDBFieldName(fieldName, null, null);
    }

    /**
     * ����ת��������Map��ֵ��
     *
     * @param object Ŀ�����
     * @param toDB   Map��key�Ƿ�ת�������ݿ��ֶ���ʽ
     * @return
     */
    public static Map<String, Object> object2Map(Object object, boolean toDB) {
        if (null == object) {
            return null;
        }
        List<Field> fieldList = new ArrayList<Field>();
        ReflectionUtils.getFields(fieldList, object.getClass(), true);
        if (GerneralUtils.isEmptyCollection(fieldList)) {
            return null;
        }
        // ʹ��LinkedHashMap��Ϊ�˱�֤Ԫ�ز���˳��
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (Field field : fieldList) {
            if (toDB) {
                map.put(splitCamelName(field.getName()),
                        ReflectionUtils.getFieldValue(object, field.getName()));
            } else {
                map.put(field.getName(), ReflectionUtils.getFieldValue(object, field.getName()));
            }
        }
        return map;
    }

    /**
     * ����ת��������Map��ֵ��(����)
     *
     * @param object Ŀ�����
     * @return
     */
    public static Map<String, Object> object2Map(Object object) {
        return object2Map(object, false);
    }

    /**
     * Objectת��������ֵ����
     *
     * @param object       ��ת���Ķ���
     * @param filterFields ��Ҫ���˵���ת�������Լ���
     * @return
     */
    public static Object[] object2Array(Object object, Set<String> filterFields) {
        if (null == object) {
            return null;
        }
        List<Field> fieldList = new ArrayList<Field>();
        ReflectionUtils.getFields(fieldList, object.getClass(), true);
        if (GerneralUtils.isEmptyCollection(fieldList)) {
            return null;
        }
        List<Object> objList = new ArrayList<Object>();
        for (Field field : fieldList) {
            if (GerneralUtils.isNotEmptyCollection(filterFields)) {
                if (filterFields.contains(field.getName())) {
                    continue;
                }
            }
            Object val = ReflectionUtils.getFieldValue(object, field.getName());
            objList.add(val);
        }
        return objList.toArray();
    }

    /**
     * Objectת��������ֵ����(����)
     *
     * @param object ��ת���Ķ���
     * @return
     */
    public static Object[] object2Array(Object object) {
        return object2Array(object, null);
    }

    /**
     * Map��ֵ��ת���ɶ���
     *
     * @param map
     * @param target
     * @return
     */
    public static Object map2Object(Map<String, Object> map, Class target) {
        if (GerneralUtils.isEmptyMap(map) || null == target) {
            return null;
        }
        Object object = null;
        try {
            object = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String propertyName = splitDBFieldName(key);
            if (!ReflectionUtils.hasThisField(target, propertyName)) {
                continue;
            }
            Object value = entry.getValue();
            if (value instanceof String) {
                if (isDate(value.toString())) {
                    value = ReflectionUtils.convertStringToObject(value.toString(), Date.class);
                }
            }
            ReflectionUtils.setFieldValue(object, propertyName, value);
        }
        return object;
    }

    /**
     * Map�ṹ����ĳɺ���
     *
     * @param map
     * @return
     */
    public static List<Map<Integer, Object>> mapVertical2Horizontal(Map<String, List<Object>> map) {
        if (GerneralUtils.isEmptyMap(map)) {
            return null;
        }
        //������
        int index = 0;
        List<Map<Integer, Object>> mapList = new ArrayList<Map<Integer, Object>>();
        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
            List<Object> list = entry.getValue();
            for (int j = 0; j < list.size(); j++) {
                Object val = list.get(j);
                Map<Integer, Object> rowMap = mapList.get(index);
                if (null == rowMap) {
                    rowMap = new HashMap<Integer, Object>();
                    rowMap.put(index, val);
                    mapList.add(rowMap);
                } else {
                    rowMap.put(index, val);
                }
            }
            index++;
        }
        return mapList;
    }

    /**
     * Map�ṹ����ĳɺ���(����)
     *
     * @param map
     * @return
     */
    public static List<Map<String, Object>> mapVerticalToHorizontal(Map<String, List<Object>> map) {
        if (GerneralUtils.isEmptyMap(map)) {
            return null;
        }
        //������
        int index = 0;
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Object> list = entry.getValue();
            for (int j = 0; j < list.size(); j++) {
                Object val = list.get(j);
                Map<String, Object> rowMap = mapList.get(index);
                if (null == rowMap) {
                    rowMap = new LinkedHashMap<String, Object>();
                    rowMap.put(key, val);
                    mapList.add(rowMap);
                } else {
                    rowMap.put(key, val);
                }
            }
            index++;
        }
        return mapList;
    }

    /**
     * Map�ṹ����ĳ�����
     *
     * @param mapList
     * @return
     */
    public static Map<String, List<Object>> mapHorizontal2Vertical(List<Map<String, Object>> mapList) {
        if (GerneralUtils.isEmptyCollection(mapList)) {
            return null;
        }
        Map<String, List<Object>> keywordMap = new HashMap<String, List<Object>>();
        for (Map<String, Object> map : mapList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                String cellVal = val == null ? "" : val.toString();
                if (cellVal.length() == 0) {
                    continue;
                }
                List<Object> columnList = keywordMap.get(key);
                if (GerneralUtils.isEmptyCollection(columnList)) {
                    columnList = new ArrayList<Object>();
                    columnList.add(cellVal);
                    keywordMap.put(key, columnList);
                } else {
                    keywordMap.get(key).add(cellVal);
                }
            }
        }
        return keywordMap;
    }

    /**
     * Map�ṹ����ĳ�����
     *
     * @param mapList
     * @return
     */
    public static Map<Integer, List<Object>> mapHorizontalToVertical(List<Map<Integer, Object>> mapList) {
        if (GerneralUtils.isEmptyCollection(mapList)) {
            return null;
        }
        Map<Integer, List<Object>> keywordMap = new HashMap<Integer, List<Object>>();
        for (Map<Integer, Object> map : mapList) {
            for (Map.Entry<Integer, Object> entry : map.entrySet()) {
                Integer key = entry.getKey();
                Object val = entry.getValue();
                String cellVal = val == null ? "" : val.toString();
                if (cellVal.length() == 0) {
                    continue;
                }
                List<Object> columnList = keywordMap.get(key.intValue());
                if (GerneralUtils.isEmptyCollection(columnList)) {
                    columnList = new ArrayList<Object>();
                    columnList.add(cellVal);
                    keywordMap.put(key.intValue(), columnList);
                } else {
                    keywordMap.get(key.intValue()).add(cellVal);
                }
            }
        }
        return keywordMap;
    }

    /**
     * ���󼯺�ת���ɶ�ά���飬���������������²���
     *
     * @param beanList
     * @return
     */
    public static Object[][] beanListToArrays(Collection beanList, Set<String> filterFields) {
        if (GerneralUtils.isEmptyCollection(beanList)) {
            return null;
        }
        List<Object[]> params = new ArrayList<Object[]>();
        for (Object object : beanList) {
            Object[] arr = object2Array(object, filterFields);
            params.add(arr);
        }
        return list2Array(params);
    }

    /**
     * ���󼯺�ת���ɶ�ά���飬���������������²���(����)
     *
     * @param beanList
     * @return
     */
    public static Object[][] beanListToArrays(Collection beanList) {
        return beanListToArrays(beanList, null);
    }

    /**
     * List<Object[]>ת���ɶ�ά����
     *
     * @param list
     * @return
     */
    public static Object[][] list2Array(List<Object[]> list) {
        if (GerneralUtils.isEmptyCollection(list)) {
            return null;
        }
        Object[][] params = new Object[list.size()][list.get(0).length];
        for (int i = 0; i < list.size(); i++) {
            params[i] = list.get(i);
        }
        return params;
    }

    /**
     * ׷�Ӳ���(��whereParams��Ĳ���ֵ׷����params��ά����ÿ���ĩβ)
     *
     * @param params
     * @param whereParams
     * @return
     */
    public static Object[][] appendParams(Object[][] params, Map<String, Object> whereParams) {
        if (GerneralUtils.isEmptyArray(params)) {
            return null;
        }
        if (GerneralUtils.isEmptyMap(whereParams)) {
            return params;
        }
        List<Object> list = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : whereParams.entrySet()) {
            Object val = entry.getValue();
            list.add(val);
        }
        Object[] appendParam = list.toArray();
        for (Object[] param : params) {
            param = ArrayUtils.mergeArray(param, appendParam);
        }
        return params;
    }

    /**
     * �����ת����List<Map<String,Object>>(columnName:columnValue)
     *
     * @param rs �����
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> resultSet2MapList(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                int columnCount = rsmd.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1);
                    map.put(columnName, rs.getObject(i + 1));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ��ȡ����IP��ַ��ֵ
     *
     * @param ipAddress
     * @return
     */
    public static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    /**
     * �ж�����IP�Ƿ���ָ��������
     *
     * @param userIp
     * @param begin
     * @param end
     * @return
     */
    public static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * �ж��Ƿ�Ϊ����IP
     *
     * @param ipAddress
     * @return
     */
    public static boolean isInnerIP(String ipAddress) {
        long ipNum = getIpNum(ipAddress);
        /**
         * ˽��IP��A�� 10.0.0.0-10.255.255.255 B�� 172.16.0.0-172.31.255.255 C��
         * 192.168.0.0-192.168.255.255 ��Ȼ������127.0.0.1��������ǻ��ص�ַ
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        return isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd)
                || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
    }

    /**
     * ��ȡ��ǰ��������������IP
     *
     * @return
     * @throws SocketException
     */
    public static List<String> getAllIntranetIp() throws SocketException {
        List<String> ipList = null;
        Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
        if (null != netInterfaces) {
            ipList = new ArrayList<String>();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                Enumeration cardipaddress = ni.getInetAddresses();
                InetAddress ip = null;
                String allipaddress = "";
                while (cardipaddress.hasMoreElements()) {
                    ip = (InetAddress) cardipaddress.nextElement();
                    if (ip.getHostAddress().equalsIgnoreCase("127.0.0.1")) {
                        continue;
                    }
                    allipaddress = ip.getHostAddress();
                    if (!"".equals(allipaddress)) {
                        boolean isInnerIp = true;
                        try {
                            isInnerIp = isInnerIP(allipaddress);
                        } catch (NumberFormatException e) {
                            isInnerIp = false;
                        }
                        if (isInnerIp) {
                            ipList.add(allipaddress);
                        }
                    }
                }
            }
        }
        return ipList;
    }

    /**
     * ��ȡ������Ĭ������IP
     *
     * @return
     *//*
	public static String getDefaultExternalIp() {
		ExternalIpAddressFetcher fetcher = new ExternalIpAddressFetcher(
				"http://checkip.dyndns.org/");
		return fetcher.getDefaultExternalIp();
	}*/

    /**
     * �ֽ�����ת����ʮ�������ַ���
     *
     * @param bytes
     * @return
     */
    public static String bytes2Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder hex = new StringBuilder(2 * bytes.length);
        for (final byte b : bytes) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
                    HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    /**
     * �ַ���ת����ʮ�������ַ���
     *
     * @param str str ��ת����ASCII�ַ���
     * @return String ÿ��Byte֮��ո�ָ�����: [61 6C 6B]
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit = 0;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * ʮ������ת���ַ���
     *
     * @param hexStr str Byte�ַ���(Byte֮���޷ָ��� ��:[616C6B])
     * @return String ��Ӧ���ַ���
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n = 0;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * �ֽ�����ת����ʮ�������ַ���
     *
     * @param byte[] b byte����
     * @return String ÿ��Byteֵ֮��ո�ָ�
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * bytes�ַ���ת��Ϊ�ֽ�����
     *
     * @param src src Byte�ַ�����ÿ��Byte֮��û�зָ���
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Byte.decode("0x" + src.substring(i * 2, m)
                    + src.substring(m, n));
        }
        return ret;
    }

    /**
     * String���ַ���ת����unicode��String
     *
     * @param strText strText ȫ���ַ���
     * @return String ÿ��unicode֮���޷ָ���
     * @throws Exception
     */
    public static String strToUnicode(String strText) {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc = 0;
        String strHex = null;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128) {
                str.append("\\u" + strHex);
            } else { // ��λ��ǰ�油00
                str.append("\\u00" + strHex);
            }
        }
        return str.toString();
    }

    /**
     * unicode��Stringת����String���ַ���
     *
     * @param hex hex 16����ֵ�ַ��� ��һ��unicodeΪ2byte��
     * @return String ȫ���ַ���
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // ��λ��Ҫ����00��ת
            String s1 = s.substring(2, 4) + "00";
            // ��λֱ��ת
            String s2 = s.substring(4);
            // ��16���Ƶ�stringתΪint
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // ��intת��Ϊ�ַ�
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    /**
     * ��ȡupdate��SQL���
     *
     * @param targetClass  Ŀ������
     * @param params       SQL���������������(�ֶ�����--����ֵ)
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields,
                                      String tablePrefix, String tableStuf, String tableName) {
        String table = getTableName(targetClass, tablePrefix, tableStuf,
                tableName);
        if (GerneralUtils.isEmptyString(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("update ").append(table).append(" set ");
        List<Field> fieldList = new ArrayList<Field>();
        ReflectionUtils.getFields(fieldList, targetClass, true);
        if (GerneralUtils.isEmptyCollection(fieldList)) {
            return null;
        }
        int count = 0;
        int index = 0;
        for (Field field : fieldList) {
            String fieldName = GerneralUtils.splitCamelName(field.getName());
            index++;
            if (GerneralUtils.isEmptyString(fieldName)) {
                continue;
            }
            if (GerneralUtils.isNotEmptyCollection(filterFields)) {
                if (filterFields.contains(fieldName)) {
                    continue;
                }
            }
            if (index == fieldList.size()) {
                builder.append(fieldName).append("=?");
            } else {
                builder.append(fieldName).append("=?,");
            }
            count++;
        }
        if (count == 0) {
            return null;
        }
        if (GerneralUtils.isNotEmptyMap(params)) {
            builder.append(" where 1=1");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (null != val) {
                    //��keyΪ����������,��ת�������ݿ���ֶ�����ʽ
                    if (key.indexOf("_") == -1) {
                        builder.append(" and ").append(splitCamelName(key)).append("=?");
                    } else {
                        builder.append(" and ").append(key).append("=?");
                    }
                }
            }
        }
        return builder.toString().replace("?, where", "? where");
    }

    /**
     * ��ȡupdate��SQL���( ����1)
     *
     * @param targetClass  Ŀ������
     * @param params       SQL���������������(�ֶ�����--����ֵ)
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields,
                                      String tablePrefix, String tableStuf) {
        return getUpdateSql(targetClass, params, filterFields, tablePrefix,
                tableStuf, null);
    }

    /**
     * ��ȡupdate��SQL���(����2)
     *
     * @param targetClass  Ŀ������
     * @param params       SQL���������������(�ֶ�����--����ֵ)
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields,
                                      String tableName) {
        return getUpdateSql(targetClass, params, filterFields, null, null,
                tableName);
    }

    /**
     * ��ȡupdate��SQL���(����3)
     *
     * @param targetClass  Ŀ������
     * @param params       SQL���������������(�ֶ�����--����ֵ)
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields) {
        return getUpdateSql(targetClass, params, filterFields, null, null, null);
    }

    /**
     * ��ȡupdate��SQL���(����4)
     *
     * @param targetClass  Ŀ������
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Set<String> filterFields) {
        return getUpdateSql(targetClass, null, filterFields, null, null, null);
    }

    /**
     * ��ȡupdate��SQL���(����5)
     *
     * @param targetClass Ŀ������
     * @return
     */
    public static String getUpdateSql(Class targetClass) {
        return getUpdateSql(targetClass, null, null, null, null, null);
    }

    /**
     * ��ȡinsert��SQL���
     *
     * @param targetClass  Ŀ������
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @param tableName    �Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getInsertSql(Class targetClass, Set<String> filterFields, String tablePrefix, String tableStuf, String tableName) {
        String table = getTableName(targetClass, tablePrefix, tableStuf, tableName);
        if (GerneralUtils.isEmptyString(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("insert into ").append(table).append("(");
        List<Field> fieldList = new ArrayList<Field>();
        ReflectionUtils.getFields(fieldList, targetClass, true);
        if (GerneralUtils.isEmptyCollection(fieldList)) {
            return null;
        }
        int count = 0;
        int index = 0;
        for (Field field : fieldList) {
            String fieldName = GerneralUtils.splitCamelName(field.getName());
            index++;
            if (GerneralUtils.isEmptyString(fieldName)) {
                continue;
            }
            if (GerneralUtils.isNotEmptyCollection(filterFields)) {
                if (filterFields.contains(fieldName)) {
                    continue;
                }
            }
            if (index == fieldList.size()) {
                builder.append(fieldName);
            } else {
                builder.append(fieldName).append(",");
            }
            count++;
        }
        if (count == 0) {
            return null;
        }
        builder.append(") values (");
        for (int i = 0; i < count; i++) {
            if (i != count - 1) {
                builder.append("?,");
            } else {
                builder.append("?");
            }
        }
        builder.append(")");
        return builder.toString().replace(",)", ")");
    }

    /**
     * ��ȡinsert��SQL���(����1)
     *
     * @param targetClass  Ŀ������
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @param tableName    �Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getInsertSql(Class targetClass,
                                      Set<String> filterFields, String tableName) {
        return getInsertSql(targetClass, filterFields, null, null, tableName);
    }

    /**
     * ��ȡinsert��SQL���(����2)
     *
     * @param targetClass  Ŀ������
     * @param filterFields ��Ҫ���˵����ֶ�(���������������ֶβ���Ҫ����)
     * @return
     */
    public static String getInsertSql(Class targetClass, Set<String> filterFields) {
        return getInsertSql(targetClass, filterFields, null, null, null);
    }

    /**
     * ��ȡinsert��SQL���(����3)
     *
     * @param targetClass Ŀ������
     * @return
     */
    public static String getInsertSql(Class targetClass) {
        return getInsertSql(targetClass, null, null, null, null);
    }

    /**
     * ��ȡ����Key������ѯ��SQL���
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName Key�����ֶ�����
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName,
                                     String tablePrefix, String tableStuf, String tableName) {
        if (GerneralUtils.isEmptyString(keyFieldName)) {
            keyFieldName = "id"; // Ĭ����������ID�ֶ�Ϊid
        }
        String table = getTableName(targetClass, tablePrefix, tableStuf,
                tableName);
        if (GerneralUtils.isEmptyString(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("select * from ").append(table);
        builder.append(" where ").append(keyFieldName).append("=?");
        return builder.toString();
    }

    /**
     * ��ȡ����Key������ѯ��SQL���(����1)
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName Key�����ֶ�����
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName,
                                     String tablePrefix, String tableStuf) {
        return getByKeySql(targetClass, keyFieldName, tablePrefix, tableStuf,
                null);
    }

    /**
     * ��ȡ����Key������ѯ��SQL���(����2)
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName Key�����ֶ�����
     * @param tableName    �Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName,
                                     String tableName) {
        return getByKeySql(targetClass, keyFieldName, null, null, tableName);
    }

    /**
     * ��ȡ����Key������ѯ��SQL���(����3)
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName Key�����ֶ�����
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName) {
        return getByKeySql(targetClass, keyFieldName, null, null, null);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ����SQL���
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName, String tablePrefix, String tableStuf,
                                           String tableName) {
        if (GerneralUtils.isEmptyString(keyFieldName)) {
            keyFieldName = "id"; // Ĭ����������ID�ֶ�Ϊid
        }
        String table = getTableName(targetClass, tablePrefix, tableStuf,
                tableName);
        if (GerneralUtils.isEmptyString(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("delete from ").append(table);
        builder.append(" where ").append(keyFieldName).append("=?");
        return builder.toString();
    }

    /**
     * ��ȡ����Key�ֶ�ɾ����SQL���(����1)
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName, String tablePrefix, String tableStuf) {
        return getDeleteByKeySql(targetClass, keyFieldName, tablePrefix,
                tableStuf, null);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ����SQL���(����2)
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName, String tableName) {
        return getDeleteByKeySql(targetClass, keyFieldName, null, null,
                tableName);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ����SQL���(����3)
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName) {
        return getDeleteByKeySql(targetClass, keyFieldName, null, null, null);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ����SQL���--(����4) ��ָ��ΨһԼ���ֶ����ƣ�Ĭ����Ϊ��id
     *
     * @param targetClass Ŀ������
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass) {
        return getDeleteByKeySql(targetClass, "id", null, null, null);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ��������¼��SQL���
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param paramsCount  ��������
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount, String tablePrefix,
                                            String tableStuf, String tableName) {
        if (GerneralUtils.isEmptyString(keyFieldName)) {
            keyFieldName = "id"; // Ĭ����������ID�ֶ�Ϊid
        }
        String table = getTableName(targetClass, tablePrefix, tableStuf,
                tableName);
        if (GerneralUtils.isEmptyString(table) || paramsCount <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("delete from ").append(table);
        builder.append(" where ").append(keyFieldName).append(" in(");
        for (int i = 0; i < paramsCount; i++) {
            if (i != paramsCount - 1) {
                builder.append("?,");
            } else {
                builder.append("?");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    /**
     * ��ȡ����Key�ֶ�ɾ��������¼��SQL���
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param paramsCount  ��������
     * @param tablePrefix  ����ǰ׺
     * @param tableStuf    ������׺
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount, String tablePrefix,
                                            String tableStuf) {
        return getDeleteByKeysSql(targetClass, keyFieldName, paramsCount,
                tablePrefix, tableStuf, null);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ��������¼��SQL���
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param paramsCount  ��������
     * @param tableName    �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount, String tableName) {
        return getDeleteByKeysSql(targetClass, keyFieldName, paramsCount, null,
                null, tableName);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ��������¼��SQL���
     *
     * @param targetClass  Ŀ������
     * @param keyFieldName ΨһԼ���ֶ�����
     * @param paramsCount  ��������
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount) {
        return getDeleteByKeysSql(targetClass, keyFieldName, paramsCount, null,
                null, null);
    }

    /**
     * ��ȡ����Key�ֶ�ɾ��������¼��SQL���
     *
     * @param targetClass Ŀ������
     * @param paramsCount ��������
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass, int paramsCount) {
        return getDeleteByKeysSql(targetClass, null, paramsCount, null, null,
                null);
    }

    /**
     * ��ȡ������������������ɾ��SQL���
     * (ע�⣺��ֻ֧��and��=������ʽ������������or like between in > >= < <=��������չ)
     *
     * @param targetClass Ŀ������
     * @param params      Where���ֲ���
     * @param tablePrefix ����ǰ׺
     * @param tableStuf   ������׺
     * @param tableName   �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteSqlWithConditions(Class targetClass,
                                                    Map<String, Object> params, String tablePrefix, String tableStuf,
                                                    String tableName) {
        if (GerneralUtils.isEmptyMap(params) || null == targetClass) {
            return null;
        }
        String table = getTableName(targetClass, tablePrefix, tableStuf, tableName);
        if (GerneralUtils.isEmptyString(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("delete from ").append(table).append(" where 1=1");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (null != val) {
                //��keyΪ����������,��ת�������ݿ���ֶ�����ʽ
                if (key.indexOf("_") == -1) {
                    builder.append(" and ").append(splitCamelName(key)).append("=?");
                } else {
                    builder.append(" and ").append(key).append("=?");
                }
            }
        }
        return builder.toString();
    }

    /**
     * ��ȡ������������������ɾ��SQL���
     * (ע�⣺��ֻ֧��and��=������ʽ������������or like between in > >= < <=��������չ)
     *
     * @param targetClass Ŀ������
     * @param params      Where���ֲ���
     * @param tableName   �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteSqlWithConditions(Class targetClass,
                                                    Map<String, Object> params, String tableName) {
        return getDeleteSqlWithConditions(targetClass, params, null, null, tableName);
    }

    /**
     * ��ȡ������������������ɾ��SQL���
     * (ע�⣺��ֻ֧��and��=������ʽ������������or like between in > >= < <=��������չ)
     *
     * @param targetClass Ŀ������
     * @param params      Where���ֲ���
     * @param tableName   �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getDeleteSqlWithConditions(Class targetClass, Map<String, Object> params) {
        return getDeleteSqlWithConditions(targetClass, params, null, null, null);
    }

    /**
     * ��ȡSelect��ѯ���
     * (��ָ����ʾ�ֶΣ�Ĭ�ϻ���ʾ��������ֶ�)
     *
     * @param targetClass   Ŀ������
     * @param params        ��ѯ����
     * @param displayFields ��ʾ�ֶ�
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params, List<String> displayFields, String tablePrefix, String tableStuf, String tableName) {
        if (null == targetClass) {
            return null;
        }
        String table = getTableName(targetClass, tablePrefix, tableStuf, tableName);
        if (GerneralUtils.isEmptyString(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder("select ");
        if (GerneralUtils.isEmptyCollection(displayFields)) {
            builder.append("*");
        } else {
            for (String displayField : displayFields) {
                if (displayField.indexOf("_") != -1) {
                    builder.append(displayField).append(",");
                } else {
                    builder.append(splitCamelName(displayField)).append(",");
                }
            }
        }
        builder.append(" from ").append(table);
        if (GerneralUtils.isNotEmptyMap(params)) {
            builder.append(" where 1=1");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (null == val) {
                    continue;
                }
                if (key.indexOf("_") != -1) {
                    builder.append(" and ").append(key).append("=?");
                } else {
                    builder.append(" and ").append(splitCamelName(key)).append("=?");
                }
            }
        }
        return builder.toString().replace(", from", " from");
    }

    /**
     * ��ȡSelect��ѯ���
     * (��ָ����ʾ�ֶΣ�Ĭ�ϻ���ʾ��������ֶ�)
     *
     * @param targetClass   Ŀ������
     * @param params        ��ѯ����
     * @param displayFields ��ʾ�ֶ�
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params, List<String> displayFields, String tableName) {
        return getQuerySqlWithConditions(targetClass, params, displayFields, null, null, tableName);
    }

    /**
     * ��ȡSelect��ѯ���
     * (��ָ����ʾ�ֶΣ�Ĭ�ϻ���ʾ��������ֶ�)
     *
     * @param targetClass   Ŀ������
     * @param params        ��ѯ����
     * @param displayFields ��ʾ�ֶ�
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params, List<String> displayFields) {
        return getQuerySqlWithConditions(targetClass, params, displayFields, null, null, null);
    }

    /**
     * ��ȡSelect��ѯ���
     * (��ָ����ʾ�ֶΣ�Ĭ�ϻ���ʾ��������ֶ�)
     *
     * @param targetClass Ŀ������
     * @param params      ��ѯ����
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params) {
        return getQuerySqlWithConditions(targetClass, params, null, null, null, null);
    }

    /**
     * ��ȡSelect��ѯ���
     * (��ָ����ʾ�ֶΣ�Ĭ�ϻ���ʾ��������ֶ�)
     * (��ָ����ѯ������Ĭ��Ϊ�����ѯ)
     *
     * @param targetClass Ŀ������
     * @param params      ��ѯ����
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass) {
        return getQuerySqlWithConditions(targetClass, null, null, null, null, null);
    }

    /**
     * ��ȡ��ѯ���е�SQL���
     *
     * @param targetClass
     * @return
     */
    public static String getQueryAllSql(Class targetClass) {
        String table = getTableName(targetClass, null, null, null);
        StringBuffer buffer = new StringBuffer("select * from ");
        buffer.append(table);
        return buffer.toString();
    }

    /**
     * ��ȡ��ѯ���е�SQL���
     *
     * @param targetClass
     * @return
     */
    public static String getQueryAllSql(String tableName) {
        StringBuffer buffer = new StringBuffer("select * from ");
        buffer.append(tableName);
        return buffer.toString();
    }

    /**
     * ��ȡ����
     *
     * @param targetClass Ŀ������
     * @param tablePrefix ����ǰ׺
     * @param tableStuf   ������׺
     * @param tableName   �û��Զ������(�������ƺͱ�����û�й���ʱ��ָ���˲���)
     * @return
     */
    public static String getTableName(Class targetClass, String tablePrefix,
                                      String tableStuf, String tableName) {
        if (null == targetClass && GerneralUtils.isEmptyString(tableName)) {
            return null;
        }
        String table = "";
        if (GerneralUtils.isEmptyString(tableName)) {
            table = GerneralUtils.splitCamelName(targetClass.getSimpleName(),
                    tablePrefix, tableStuf);
        } else {
            table = tableName;
        }
        return table;
    }

    /**
     * ͳ�Ƽ�����ĳԪ�س��ֵĴ���
     *
     * @param coll    ����
     * @param element ������ĳһԪ��
     * @return
     */
    public static int getOccurCountOfElInColl(Collection coll, Object element) {
        if (GerneralUtils.isEmptyCollection(coll)) {
            return 0;
        }
        return Collections.frequency(coll, element);
    }

    /**
     * ����value��map��������
     *
     * @param map
     */
    public static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map, final boolean asc) {
        if (isEmptyMap(map)) {
            return null;
        }
        List<Map.Entry<String, Integer>> entrys = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(entrys, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                if (asc) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        return entrys;
    }

    /**
     * ����value��map��������(Ĭ������)
     *
     * @param map
     */
    public static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map) {
        return sortMapByValue(map, true);
    }

    /**
     * ���������ַ�����󳤶ȵ���ͬ����
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String searchMaxLenSame(String s1, String s2) {
        if (null == s1 || s2 == null) {
            return "";
        }
        if (s1.length() > s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }
        int n = s1.length();
        int index = 0;
        ok:
        for (; n > 0; n--) {
            for (int i = 0; i < s1.length() - n + 1; i++) {
                String s = s1.substring(i, i + n);
                if (s2.indexOf(s) != -1) {
                    index = i;
                    break ok;
                }
            }
        }
        return s1.substring(index, index + n);
    }

    /**
     * �滻HTML5���ĵ�����
     *
     * @param html5
     * @return
     */
    public static String replaceDocType(String html5) {
        if (GerneralUtils.isEmptyString(html5)) {
            return html5;
        }
        if (html5.indexOf("<!doctype html>") == -1) {
            return html5;
        }
        String html = html5.replace("<!doctype html>", Constant.HTML_DOCTYPY);
        html = html.replaceAll("<html\\s+[^>]+>", "<html>");
        return html;
    }

    /**
     * ����ת��������
     *
     * @param collection
     * @return
     */
    public static Object[] collection2Array(Collection<Object> collection) {
        if (GerneralUtils.isEmptyCollection(collection)) {
            return null;
        }
        return collection.toArray();
    }

    /**
     * ����ת����List
     *
     * @param array
     * @return
     */
    public static List array2List(Object[] array) {
        if (GerneralUtils.isEmptyArray(array)) {
            return null;
        }
        return Arrays.asList(array);
    }

    /**
     * ����ת����Set
     *
     * @param array
     * @return
     */
    public static Set array2Set(Object[] array) {
        if (GerneralUtils.isEmptyArray(array)) {
            return null;
        }
        return new LinkedHashSet(array2List(array));
    }

    /**
     * �޳���β�ַ�(�����һ�����з������һ�����ŵȵ�)
     *
     * @param str     �������ַ���
     * @param regular ������ʽ
     * @return �����޳�����ַ�
     */
    public static String replaceEndsWith(String str, String regular) {
        if (isEmptyString(str)) {
            return null;
        }
        if (GerneralUtils.isEmptyString(regular)) {
            return str;
        }
        return str.replaceAll(regular, "");
    }

    /*
     * ��������ļ��� 
     */
    public static String gernerateRandomFilename() {
        Random rand = new Random();
        long random = rand.nextInt() + System.currentTimeMillis();
        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        StringBuffer buffer = new StringBuffer();
        buffer.append(intYear).append("-").append(intMonth);
        buffer.append("-").append(intDay).append("-").append(random);
        return buffer.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            String fileName = gernerateRandomFilename();
            System.out.println(fileName);
        }
    }
	/*public static void main(String[] args) {
		List list = new ArrayList();
		for (int i = 0; i < 117; i++) {
			list.add(i+1);
		}
		List<List> tList = splitList(list, 10);
		for (List lt : tList) {
			for (Object object : lt) {
				System.out.println(object.toString());
			}
		}
	}*/
}

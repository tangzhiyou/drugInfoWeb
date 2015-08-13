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
 * 通用函数工具类
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
     * 基本数据类型注册
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
     * 判断字符串为空(包含null和"")
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (null == str || "".equals(str));
    }

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    /**
     * 判断集合非空
     *
     * @param collection
     * @return
     */
    public static boolean isEmptyCollection(Collection collection) {
        return (null == collection || collection.isEmpty());
    }

    /**
     * 判断集合非空
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmptyCollection(Collection collection) {
        return !isEmptyCollection(collection);
    }

    /**
     * 判断某集合内是否包含给定元素
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
     * 判断Map为空
     *
     * @param map
     * @return
     */
    public static boolean isEmptyMap(Map map) {
        return (null == map || map.isEmpty());
    }

    /**
     * 判断Map非空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmptyMap(Map map) {
        return !isEmptyMap(map);
    }

    /**
     * 判断数组为空
     *
     * @param array
     * @return
     */
    public static boolean isEmptyArray(Object[] array) {
        return null == array || array.length == 0;
    }

    /**
     * 判断数组非空
     *
     * @param array
     * @return
     */
    public static boolean isNotEmptyArray(Object[] array) {
        return !isEmptyArray(array);
    }

    /**
     * 去除数组中重复元素
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
     * 删除集合中指定元素
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
     * 删除Map中指定key对应的键值对
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
     * 删除数组某索引位置的元素并返回删除后的新数组
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
     * 将数组中所有元素按照指定的连接符拼接成一个字符串
     * 如果不指定连接符，默认设置为英文逗号,
     *
     * @param array     数组对象
     * @param delimiter 连接符
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
     * 将数组中所有元素按照指定的连接符拼接成一个字符串(重载)
     * 如果不指定连接符，默认设置为英文逗号,
     *
     * @param array     数组对象
     * @param delimiter 连接符
     * @return
     */
    public static String joinArray(Object[] array) {
        return joinArray(array, null);
    }

    /**
     * 将集合中所有元素按照指定的连接符拼接成一个字符串
     * 如果不指定连接符，默认设置为英文逗号,
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
     * 将集合中所有元素按照指定的连接符拼接成一个字符串(重载)
     * 如果不指定连接符，默认设置为英文逗号,
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String joinCollection(Collection collection) {
        return joinCollection(collection, null);
    }

    /**
     * List集合N等分<br/>
     * 若不能整除，则最后余数算一份
     *
     * @param list
     * @param splitCOunt 分割个数即分成splitCOunt份
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
     * 判断传入字符串是否包含数字
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
     * 判断字符串是否全部由大写字母组成
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
     * 判断字符串是否全部由小写字母组成
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
     * 判断字符串是否全部是数字(不含小数点)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isEmptyString(str) || (str.length() > 1 && str.startsWith("0"))) {
            return false;
        }
        //JDK自带函数实现
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
        /*//判断ASC码实现
        for(int i=str.length();--i >= 0;){
	        int chr = str.charAt(i);   
	        if(chr < 48 || chr > 57) {   
	           return false;   
	        }
	    }
	    return true;*/
    }

    /**
     * 判断字符串是否可以转换成小数
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
        //判断是否含有非数字字符
        String temp = str.replaceAll("[\\d|.]*", "");
        if (temp.length() != 0) {
            return false;
        }
        //排除类似000.12形式
        if (str.indexOf(".") > 1) {
            String firstLetter = str.substring(0, 1);
            if (firstLetter.equals("0")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从字符串中提取数字(含小数),如：Was $365.00--365.00
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
     * 字符串剔除重复项，如：aaaaabbbbbccccc-->abc
     * 注意：暂不支持交叉重复，如：aaaaabbbaaaccc-->bac而不是abac
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
     * 生成指定区间[min-max)之间的随机数
     *
     * @param max
     * @param min
     * @return
     */
    public static int generateRandomNumber(int max, int min) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 判断某字符串是否包含中文字符
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
     * 判断字符串是否为日期格式
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
     * 判断指定类型是否是基本数据类型
     *
     * @param clazz
     * @return
     */
    public final static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || PRIMITIVE_CLASSES.contains(clazz);
    }

    /**
     * 字符串首字母转换成大写
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
     * 字符串首字母转换成小写
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
     * 从get/set方法名中提取出属性名称
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
     * 骆驼命名法转换成数据库字段命名法，如studentName-->student_name
     *
     * @param propertyName 属性名称
     * @param prefix       添加前缀
     * @param stuffix      添加后缀
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
     * 骆驼命名法转换成数据库字段命名法(重载)，如studentName-->student_name
     *
     * @param propertyName 属性名称
     * @return
     */
    public static String splitCamelName(String propertyName) {
        return splitCamelName(propertyName, null, null);
    }

    /**
     * 数据库字段名称转换成类属性名，如stu_name-->stuName
     *
     * @param fieldName 数据库字段名称
     * @param prefix    前缀
     * @param stuffix   后缀
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
     * 数据库字段名称转换成类属性名(重载)，如stu_name-->stuName
     *
     * @param fieldName 数据库字段名称
     * @return
     */
    public static String splitDBFieldName(String fieldName) {
        return splitDBFieldName(fieldName, null, null);
    }

    /**
     * 对象转换成属性Map键值对
     *
     * @param object 目标对象
     * @param toDB   Map的key是否转换成数据库字段形式
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
        // 使用LinkedHashMap是为了保证元素插入顺序
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
     * 对象转换成属性Map键值对(重载)
     *
     * @param object 目标对象
     * @return
     */
    public static Map<String, Object> object2Map(Object object) {
        return object2Map(object, false);
    }

    /**
     * Object转换成属性值数组
     *
     * @param object       待转换的对象
     * @param filterFields 需要过滤掉不转换的属性集合
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
     * Object转换成属性值数组(重载)
     *
     * @param object 待转换的对象
     * @return
     */
    public static Object[] object2Array(Object object) {
        return object2Array(object, null);
    }

    /**
     * Map键值对转换成对象
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
     * Map结构纵向改成横向
     *
     * @param map
     * @return
     */
    public static List<Map<Integer, Object>> mapVertical2Horizontal(Map<String, List<Object>> map) {
        if (GerneralUtils.isEmptyMap(map)) {
            return null;
        }
        //列索引
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
     * Map结构纵向改成横向(重载)
     *
     * @param map
     * @return
     */
    public static List<Map<String, Object>> mapVerticalToHorizontal(Map<String, List<Object>> map) {
        if (GerneralUtils.isEmptyMap(map)) {
            return null;
        }
        //列索引
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
     * Map结构横向改成纵向
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
     * Map结构横向改成纵向
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
     * 对象集合转换成二维数组，用于批量插入或更新参数
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
     * 对象集合转换成二维数组，用于批量插入或更新参数(重载)
     *
     * @param beanList
     * @return
     */
    public static Object[][] beanListToArrays(Collection beanList) {
        return beanListToArrays(beanList, null);
    }

    /**
     * List<Object[]>转换成二维数组
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
     * 追加参数(将whereParams里的参数值追加至params二维数组每项的末尾)
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
     * 结果集转换成List<Map<String,Object>>(columnName:columnValue)
     *
     * @param rs 结果集
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
     * 获取内网IP地址数值
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
     * 判断内网IP是否在指定区段内
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
     * 判断是否为内网IP
     *
     * @param ipAddress
     * @return
     */
    public static boolean isInnerIP(String ipAddress) {
        long ipNum = getIpNum(ipAddress);
        /**
         * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
         * 192.168.0.0-192.168.255.255 当然，还有127.0.0.1这个网段是环回地址
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
     * 获取当前机器的所有内网IP
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
     * 获取本机的默认外网IP
     *
     * @return
     *//*
	public static String getDefaultExternalIp() {
		ExternalIpAddressFetcher fetcher = new ExternalIpAddressFetcher(
				"http://checkip.dyndns.org/");
		return fetcher.getDefaultExternalIp();
	}*/

    /**
     * 字节数组转换成十六进制字符串
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
     * 字符串转换成十六进制字符串
     *
     * @param str str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
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
     * 十六进制转换字符串
     *
     * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
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
     * 字节数组转换成十六进制字符串
     *
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
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
     * bytes字符串转换为字节数组
     *
     * @param src src Byte字符串，每个Byte之间没有分隔符
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
     * String的字符串转换成unicode的String
     *
     * @param strText strText 全角字符串
     * @return String 每个unicode之间无分隔符
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
            } else { // 低位在前面补00
                str.append("\\u00" + strHex);
            }
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     *
     * @param hex hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    /**
     * 获取update的SQL语句
     *
     * @param targetClass  目标类型
     * @param params       SQL语句限制条件参数(字段名称--参数值)
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
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
                    //若key为类属性名称,则转换成数据库表字段名形式
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
     * 获取update的SQL语句( 重载1)
     *
     * @param targetClass  目标类型
     * @param params       SQL语句限制条件参数(字段名称--参数值)
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields,
                                      String tablePrefix, String tableStuf) {
        return getUpdateSql(targetClass, params, filterFields, tablePrefix,
                tableStuf, null);
    }

    /**
     * 获取update的SQL语句(重载2)
     *
     * @param targetClass  目标类型
     * @param params       SQL语句限制条件参数(字段名称--参数值)
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields,
                                      String tableName) {
        return getUpdateSql(targetClass, params, filterFields, null, null,
                tableName);
    }

    /**
     * 获取update的SQL语句(重载3)
     *
     * @param targetClass  目标类型
     * @param params       SQL语句限制条件参数(字段名称--参数值)
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Map<String, Object> params, Set<String> filterFields) {
        return getUpdateSql(targetClass, params, filterFields, null, null, null);
    }

    /**
     * 获取update的SQL语句(重载4)
     *
     * @param targetClass  目标类型
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @return
     */
    public static String getUpdateSql(Class targetClass,
                                      Set<String> filterFields) {
        return getUpdateSql(targetClass, null, filterFields, null, null, null);
    }

    /**
     * 获取update的SQL语句(重载5)
     *
     * @param targetClass 目标类型
     * @return
     */
    public static String getUpdateSql(Class targetClass) {
        return getUpdateSql(targetClass, null, null, null, null, null);
    }

    /**
     * 获取insert的SQL语句
     *
     * @param targetClass  目标类型
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @param tableName    自定义表名(当类名称和表名称没有规律时请指定此参数)
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
     * 获取insert的SQL语句(重载1)
     *
     * @param targetClass  目标类型
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @param tableName    自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getInsertSql(Class targetClass,
                                      Set<String> filterFields, String tableName) {
        return getInsertSql(targetClass, filterFields, null, null, tableName);
    }

    /**
     * 获取insert的SQL语句(重载2)
     *
     * @param targetClass  目标类型
     * @param filterFields 需要过滤掉的字段(比如主键自增长字段不需要插入)
     * @return
     */
    public static String getInsertSql(Class targetClass, Set<String> filterFields) {
        return getInsertSql(targetClass, filterFields, null, null, null);
    }

    /**
     * 获取insert的SQL语句(重载3)
     *
     * @param targetClass 目标类型
     * @return
     */
    public static String getInsertSql(Class targetClass) {
        return getInsertSql(targetClass, null, null, null, null);
    }

    /**
     * 获取根据Key主键查询的SQL语句
     *
     * @param targetClass  目标类型
     * @param keyFieldName Key主键字段名称
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName,
                                     String tablePrefix, String tableStuf, String tableName) {
        if (GerneralUtils.isEmptyString(keyFieldName)) {
            keyFieldName = "id"; // 默认设置主键ID字段为id
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
     * 获取根据Key主键查询的SQL语句(重载1)
     *
     * @param targetClass  目标类型
     * @param keyFieldName Key主键字段名称
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName,
                                     String tablePrefix, String tableStuf) {
        return getByKeySql(targetClass, keyFieldName, tablePrefix, tableStuf,
                null);
    }

    /**
     * 获取根据Key主键查询的SQL语句(重载2)
     *
     * @param targetClass  目标类型
     * @param keyFieldName Key主键字段名称
     * @param tableName    自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName,
                                     String tableName) {
        return getByKeySql(targetClass, keyFieldName, null, null, tableName);
    }

    /**
     * 获取根据Key主键查询的SQL语句(重载3)
     *
     * @param targetClass  目标类型
     * @param keyFieldName Key主键字段名称
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getByKeySql(Class targetClass, String keyFieldName) {
        return getByKeySql(targetClass, keyFieldName, null, null, null);
    }

    /**
     * 获取根据Key字段删除的SQL语句
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName, String tablePrefix, String tableStuf,
                                           String tableName) {
        if (GerneralUtils.isEmptyString(keyFieldName)) {
            keyFieldName = "id"; // 默认设置主键ID字段为id
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
     * 获取根据Key字段删除的SQL语句(重载1)
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName, String tablePrefix, String tableStuf) {
        return getDeleteByKeySql(targetClass, keyFieldName, tablePrefix,
                tableStuf, null);
    }

    /**
     * 获取根据Key字段删除的SQL语句(重载2)
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName, String tableName) {
        return getDeleteByKeySql(targetClass, keyFieldName, null, null,
                tableName);
    }

    /**
     * 获取根据Key字段删除的SQL语句(重载3)
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass,
                                           String keyFieldName) {
        return getDeleteByKeySql(targetClass, keyFieldName, null, null, null);
    }

    /**
     * 获取根据Key字段删除的SQL语句--(重载4) 不指定唯一约束字段名称，默认认为是id
     *
     * @param targetClass 目标类型
     * @return
     */
    public static String getDeleteByKeySql(Class targetClass) {
        return getDeleteByKeySql(targetClass, "id", null, null, null);
    }

    /**
     * 获取根据Key字段删除多条记录的SQL语句
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param paramsCount  参数个数
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount, String tablePrefix,
                                            String tableStuf, String tableName) {
        if (GerneralUtils.isEmptyString(keyFieldName)) {
            keyFieldName = "id"; // 默认设置主键ID字段为id
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
     * 获取根据Key字段删除多条记录的SQL语句
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param paramsCount  参数个数
     * @param tablePrefix  表名前缀
     * @param tableStuf    表名后缀
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount, String tablePrefix,
                                            String tableStuf) {
        return getDeleteByKeysSql(targetClass, keyFieldName, paramsCount,
                tablePrefix, tableStuf, null);
    }

    /**
     * 获取根据Key字段删除多条记录的SQL语句
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param paramsCount  参数个数
     * @param tableName    用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount, String tableName) {
        return getDeleteByKeysSql(targetClass, keyFieldName, paramsCount, null,
                null, tableName);
    }

    /**
     * 获取根据Key字段删除多条记录的SQL语句
     *
     * @param targetClass  目标类型
     * @param keyFieldName 唯一约束字段名称
     * @param paramsCount  参数个数
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass,
                                            String keyFieldName, int paramsCount) {
        return getDeleteByKeysSql(targetClass, keyFieldName, paramsCount, null,
                null, null);
    }

    /**
     * 获取根据Key字段删除多条记录的SQL语句
     *
     * @param targetClass 目标类型
     * @param paramsCount 参数个数
     * @return
     */
    public static String getDeleteByKeysSql(Class targetClass, int paramsCount) {
        return getDeleteByKeysSql(targetClass, null, paramsCount, null, null,
                null);
    }

    /**
     * 获取含多限制条件参数的删除SQL语句
     * (注意：暂只支持and、=等于形式的限制条件，or like between in > >= < <=后续再扩展)
     *
     * @param targetClass 目标类型
     * @param params      Where部分参数
     * @param tablePrefix 表名前缀
     * @param tableStuf   表名后缀
     * @param tableName   用户自定义表名(当类名称和表名称没有规律时请指定此参数)
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
                //若key为类属性名称,则转换成数据库表字段名形式
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
     * 获取含多限制条件参数的删除SQL语句
     * (注意：暂只支持and、=等于形式的限制条件，or like between in > >= < <=后续再扩展)
     *
     * @param targetClass 目标类型
     * @param params      Where部分参数
     * @param tableName   用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getDeleteSqlWithConditions(Class targetClass,
                                                    Map<String, Object> params, String tableName) {
        return getDeleteSqlWithConditions(targetClass, params, null, null, tableName);
    }

    /**
     * 获取含多限制条件参数的删除SQL语句
     * (注意：暂只支持and、=等于形式的限制条件，or like between in > >= < <=后续再扩展)
     *
     * @param targetClass 目标类型
     * @param params      Where部分参数
     * @param tableName   用户自定义表名(当类名称和表名称没有规律时请指定此参数)
     * @return
     */
    public static String getDeleteSqlWithConditions(Class targetClass, Map<String, Object> params) {
        return getDeleteSqlWithConditions(targetClass, params, null, null, null);
    }

    /**
     * 获取Select查询语句
     * (不指定显示字段，默认会显示表的所有字段)
     *
     * @param targetClass   目标类型
     * @param params        查询参数
     * @param displayFields 显示字段
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
     * 获取Select查询语句
     * (不指定显示字段，默认会显示表的所有字段)
     *
     * @param targetClass   目标类型
     * @param params        查询参数
     * @param displayFields 显示字段
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params, List<String> displayFields, String tableName) {
        return getQuerySqlWithConditions(targetClass, params, displayFields, null, null, tableName);
    }

    /**
     * 获取Select查询语句
     * (不指定显示字段，默认会显示表的所有字段)
     *
     * @param targetClass   目标类型
     * @param params        查询参数
     * @param displayFields 显示字段
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params, List<String> displayFields) {
        return getQuerySqlWithConditions(targetClass, params, displayFields, null, null, null);
    }

    /**
     * 获取Select查询语句
     * (不指定显示字段，默认会显示表的所有字段)
     *
     * @param targetClass 目标类型
     * @param params      查询参数
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass, Map<String, Object> params) {
        return getQuerySqlWithConditions(targetClass, params, null, null, null, null);
    }

    /**
     * 获取Select查询语句
     * (不指定显示字段，默认会显示表的所有字段)
     * (不指定查询参数，默认为整表查询)
     *
     * @param targetClass 目标类型
     * @param params      查询参数
     * @return
     */
    public static String getQuerySqlWithConditions(Class targetClass) {
        return getQuerySqlWithConditions(targetClass, null, null, null, null, null);
    }

    /**
     * 获取查询所有的SQL语句
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
     * 获取查询所有的SQL语句
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
     * 获取表名
     *
     * @param targetClass 目标类型
     * @param tablePrefix 表名前缀
     * @param tableStuf   表名后缀
     * @param tableName   用户自定义表名(当类名称和表名称没有规律时请指定此参数)
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
     * 统计集合中某元素出现的次数
     *
     * @param coll    集合
     * @param element 集合中某一元素
     * @return
     */
    public static int getOccurCountOfElInColl(Collection coll, Object element) {
        if (GerneralUtils.isEmptyCollection(coll)) {
            return 0;
        }
        return Collections.frequency(coll, element);
    }

    /**
     * 根据value对map进行排序
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
     * 根据value对map进行排序(默认升序)
     *
     * @param map
     */
    public static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map) {
        return sortMapByValue(map, true);
    }

    /**
     * 查找两个字符串最大长度的相同部分
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
     * 替换HTML5的文档声明
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
     * 集合转换成数组
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
     * 数组转换成List
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
     * 数组转换成Set
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
     * 剔除结尾字符(如最后一个换行符、最后一个逗号等等)
     *
     * @param str     待处理字符串
     * @param regular 正则表达式
     * @return 返回剔除后的字符
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
     * 生成随机文件名 
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

package com.skysoft.framework;

import org.springframework.util.StringUtils;

/**
 * User: pinaster
 * Date: 14-1-4
 * Time: 下午6:06
 */
public class StringHelper{
    public static void main(String[] args) {
        System.out.println("hello world");
    }

    // 去前后空格
    public static String trim(String text) {
        return StringUtils.trimLeadingWhitespace(text);
    }
    //去所有空格
    public static String trimAll(String text)
    {
        return  StringUtils.trimAllWhitespace(text);
    }
}

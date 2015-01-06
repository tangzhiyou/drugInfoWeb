package com.skysoft.test;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: pinaster
 * Date: 13-12-18
 * Time: 下午12:33
 */
public class testReg {
    public static void main(String[] args) {
       /* String regex = "(x)(y\\w*)(z)";

        String input = "exy123z,xy456z";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        if (m.find()) {
            System.out.println("起始"+m.end(1)+"结束"+m.start(3));
           // System.out.println("截取的字符串"+input.substring(m.end(2))+"位置"+m.end(2));
            //System.out.println(m.group(2));
        }*/
        for (String tt:StringUtils.stripAll(new String[]{" 中华 ", "民 国", "国共和国"}))
        {
            System.out.println(tt);
        }
    }
}

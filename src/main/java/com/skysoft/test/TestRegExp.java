package com.skysoft.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: pinaster
 * Date: 13-12-2
 * Time: 下午4:33
 */
public class TestRegExp {
    public static  void main(String[] args)
    {
       /* String text=FileIOStreamTools.readerContent("conf/divRegExp.txt");
//        <div style="display:block;overflow: hidden; width: 0; height: 0;">
        String RegExp="<div style=\"display:block;overflow: hidden; width: 0; height: 0;\"></div>";
        text=fiterHtmlTag(text,"");
        System.out.println("新文本内容"+text);*/


//        String reg_charset = "<span[^>]*?title=\'([0-9]*[\\s|\\S]*星级酒店)\'[\\s|\\S]*class=\'[a-z]*[\\s|\\S]*[a-z]*[0-9]*\'";
//        String source = "<span title='5 星级酒店' class='dx dx5'>";
//        表达式 "<(\w+)\s*(\w+(=('|").*?\4)?\s*)*>.*?</\1>" 在匹配 "<td id='td1' style="bgcolor:white"></td>" 时

//        "<(\w+)\s*(\w+(=('|").*?\4)?\s*)*>.*?</\1>" 在匹配 "<td id='td1'
        StringBuffer sbConent = new StringBuffer();
        Pattern spattern = Pattern.compile("");
        Matcher smatcher = spattern.matcher("<td id='td1' style=\"bgcolor:white\"></td>");
        boolean sresult = smatcher.find();
        while (sresult) {
            String text = smatcher.group();
            sbConent.append(text);
            sresult = smatcher.find();
        }
        System.out.println(sbConent.toString());




//        String text=fiterHtmlTag("tangzhiyou<a style=\"display:block;overflow: hidden; width: 0; height: 0;\">tMZ355pnD3S药智数据jABquTm7rvxIUQb</a>:hehe","<a[\u4E00-\u9FA5]+");
//        System.out.println(text);
    }

    public static String fiterHtmlTag(String str,String RegExp) {
        Pattern pattern = Pattern.compile(RegExp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        int count=1;
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
            System.out.println(count++);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

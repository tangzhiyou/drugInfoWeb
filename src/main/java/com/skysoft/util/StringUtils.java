package com.skysoft.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 字符串处理工具类
 */
public class StringUtils {
    private static final String[] escapeIgnore = new String[]{"*", "+", "-", ".", "/", "@", "_"};
    private static final String[] encodeURIIgnore = new String[]{"!", "#", "$", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "=", "?", "@", "_", "~"};
    private static final String[] encodeURIComponentIgnore = new String[]{"!", "'", "(", ")", "*", "-", ".", "_", "~"};

    /**
     * @param @param url
     * @return void
     * @throws
     * @Title: encode
     * @Description: URL编码(原理:将字符转换成16进制, 然后前面加前缀%, 对于0-255范围之外字符加前缀%u)
     * 不编码字符包括.-*_,注意空格会被编码成加号+,本函数已修复此BUG
     */
    public static String encode(String url) {
        if (GerneralUtils.isEmptyString(url)) {
            return url;
        }
        String result = URLEncoder.encode(url);
        return result.replace("+", "%20");
    }

    /**
     * @param @param url
     * @return void
     * @throws
     * @Title: decode
     * @Description: URL解码(会将+加号解码成空格)
     */
    public static String decode(String url) {
        if (GerneralUtils.isEmptyString(url)) {
            return url;
        }
        return URLDecoder.decode(url);
    }

    /**
     * @param @param  src
     * @param @return
     * @return String
     * @throws
     * @Title: escape
     * @Description: URL中特殊字符编码(* + - . / @ _ 0-9 a-z A-Z不会被编码)
     */
    public static String escape(String src) {
        if (GerneralUtils.isEmptyString(src)) {
            return src;
        }
        char j = 0;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (int i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            boolean flag = false;
            for (int k = 0; k < escapeIgnore.length; k++) {
                if (escapeIgnore[k].charAt(0) == j) {
                    tmp.append(j);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                    tmp.append(j);
                } else if (j < 256) {
                    tmp.append("%");
                    if (j < 16) {
                        tmp.append("0");
                    }
                    tmp.append(Integer.toString(j, 16));
                } else {
                    tmp.append("%u");
                    tmp.append(Integer.toString(j, 16));
                }
            }
        }
        return tmp.toString();
    }

    /**
     * @param @param  uriString
     * @param @return
     * @return String
     * @throws
     * @Title: encodeURI
     * @Description: 对URI进行完整的编码(!#$&'()*+,-./:;=?@_~0-9a-zA-Z不会被编码)
     */
    public static String encodeURI(String src) {
        if (GerneralUtils.isEmptyString(src)) {
            return src;
        }
        char j = 0;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (int i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            boolean flag = false;
            for (int k = 0; k < encodeURIIgnore.length; k++) {
                if (encodeURIIgnore[k].charAt(0) == j) {
                    tmp.append(j);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                    tmp.append(j);
                } else if (j < 256) {
                    tmp.append("%");
                    if (j < 16) {
                        tmp.append("0");
                    }
                    tmp.append(Integer.toString(j, 16));
                } else {
                    tmp.append("%u");
                    tmp.append(Integer.toString(j, 16));
                }
            }
        }
        return tmp.toString();
    }

    /**
     * @param @param  uriString
     * @param @return
     * @return String
     * @throws
     * @Title: encodeURIComponent
     * @Description: 对URI参数部分进行编码(!'()*-._~0-9a-zA-Z不会被编码)
     * 主要适用于对URL传递的特殊字符或中文参数部分进行编码,而不是把整个URL传递给此函数
     * eg:http://xxxx.do?qu=中文&r=50%,明显参数:中文和50%需要编码,
     * 因此你只需要把中文和50%传递给此函数进行编码
     */
    public static String encodeURIComponent(String src) {
        if (GerneralUtils.isEmptyString(src)) {
            return src;
        }
        char j = 0;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (int i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            boolean flag = false;
            for (int k = 0; k < encodeURIComponentIgnore.length; k++) {
                if (encodeURIComponentIgnore[k].charAt(0) == j) {
                    tmp.append(j);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                    tmp.append(j);
                } else if (j < 256) {
                    tmp.append("%");
                    if (j < 16) {
                        tmp.append("0");
                    }
                    tmp.append(Integer.toString(j, 16));
                } else {
                    tmp.append("%u");
                    tmp.append(Integer.toString(j, 16));
                }
            }
        }
        return tmp.toString();
    }

    /**
     * @param @param  src
     * @param @return
     * @return String
     * @throws
     * @Title: unescape
     * @Description: URL中特殊字符解码，与escape功能相反
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (-1 == pos) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }


    /**
     * 从字符串文本中提取日期字符串,如：xxxxxxx2/14/2013xxxxxxxx -->2/14/2013
     *
     * @param text
     * @return
     */
    public static String getDateTextFromString(String text) {
        if (GerneralUtils.isEmptyString(text)) {
            return null;
        }
        StringBuffer reg = new StringBuffer();
        // (d)d-(M)M-yyyy (d)d/(M)M/yyyy (d)d.(M)M.yyyy
        reg.append("(0?[1-9]|[12][0-9]|3[01])(/|-|.)(0?[1-9]|1[012])(/|-|.)((1|2)\\d{3})|");
        // (M)M-(d)d-yyyy (M)M/(d)d/yyyy (M)M.(d)d.yyyy
        reg.append("(0?[1-9]|1[012])(/|-|.)(0?[1-9]|[12][0-9]|3[01])(/|-|.)((1|2)\\d{3})|");
        // yyyy-(M)M-(d)d yyyy/(M)M/(d)d yyyy.(M)M.(d)d
        reg.append("((1|2)\\d{3})(/|-|.)(0?[1-9]|1[012])(/|-|.)(0?[1-9]|[12][0-9]|3[01])|");
        // yyyy-(d)d-(M)M yyyy/(d)d/(M)M yyyy.(d)d.(M)M
        reg.append("((1|2)\\d{3})(/|-|.)(0?[1-9]|[12][0-9]|3[01])(/|-|.)(0?[1-9]|1[012])");
        Pattern pattern = Pattern.compile(reg.toString());
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 从字符串中提取出数字(可以提取整数和小数)
     *
     * @param text
     * @return
     */
    public static List<String> getNumbericFromString(String text) {
        if (GerneralUtils.isEmptyString(text)) {
            return null;
        }
        List<String> values=new ArrayList<String>();
        StringBuffer reg = new StringBuffer();
        reg.append("0\\.\\d+|[1-9]{1}([0-9]+)?\\.\\d+|\\d+");
        Pattern pattern = Pattern.compile(reg.toString());
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            values.add(matcher.group());
        }
        return values;
    }

    /**
     * 从字符串中提取URL链接,<br/>
     * 如:http://www.google.com.hk
     * http://www.savings.com/popup/detail/coupon-723379.html
     * http://cdn3.node1.savings
     * .com/images/savings/logo/3499907.jpeg?height=30&width=120
     *
     * @param text
     * @return
     */
    public static String getURLFromString(String text) {
        if (GerneralUtils.isEmptyString(text)) {
            return null;
        }
        StringBuffer reg = new StringBuffer();
        reg.append("\\b((ftp|https?)://[-\\w]+(\\.\\w[-\\w]*)+|(?i:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+");
        reg.append("(?-i:com\\b|edu\\b|biz\\b|gov\\b|in(?:t|fo)\\b|mil\\b|net\\b|org\\b|[a-z][a-z]\\b))");
        reg.append("(:\\d+)?(/[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]*(?:[.!,?]+[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]+)*)?");
        Pattern pattern = Pattern.compile(reg.toString());
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 将毫秒转换成多少小时分钟秒的格式 如：12410000-->3小时26分钟50秒
     *
     * @param time
     * @return 格式化后的时间格式
     */
    public static String formatMillSecond(long time, String hourPlaceholder, String minuePlaceholder, String secondPlaceholder) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = new Long(time).intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        StringBuffer buffer = new StringBuffer();
        if (hour > 0) {
            buffer.append(String.valueOf(hour)).append(hourPlaceholder);
        }
        if (minute > 0) {
            buffer.append(String.valueOf(minute)).append(minuePlaceholder);
        }
        if (second >= 0) {
            buffer.append(String.valueOf(second)).append(secondPlaceholder);
        }
        return buffer.toString();
    }

    /**
     * 将多少小时分钟秒的格式字符串转换成毫秒数(与formatMillSecond方法相反) 如：3小时26分钟50秒-->12410000
     *
     * @param str
     * @return
     */
    public static long unFormatMillSecond(String str) {
        if (GerneralUtils.isEmptyString(str)) {
            return 0;
        }
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        if (list.size() != 3) {
            return 0;
        }
        int hour = Integer.parseInt(list.get(0));
        int minue = Integer.parseInt(list.get(1));
        int second = Integer.parseInt(list.get(2));
        return hour * 3600000 + minue * 60000 + second * 1000;
    }

    /**
     * 剔除JS注释
     *
     * @param str
     * @return
     */
    public static String cleanComment(String str) {
        if (GerneralUtils.isEmptyString(str)) {
            return null;
        }
        Pattern pattern = Pattern.compile("<!--[\\w\\W\r\\n]*?-->");
        Matcher matcher = pattern.matcher(str);
        str = matcher.replaceAll("");
        return str;
    }

    /**
     * Unicode字符串转换成中文
     *
     * @param dataStr
     * @return
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    /**
     * 判断字符串是否以数字结尾
     *
     * @param str
     * @return
     */
    public static boolean endsWithNum(String str) {
        if (GerneralUtils.isEmptyString(str)) {
            return false;
        }
        String temp = str.replaceAll("\\d{1}$", "");
        return temp.length() != str.length();
    }

    /**
     * 查找字符串中第一个大写字母的索引位置
     *
     * @param str
     * @return
     */
    public static int findFirstUpperWordIndex(String str) {
        if (GerneralUtils.isEmptyString(str)) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 剔除字符串结尾多余的换行符 如：I like java.\n\n\n\n --> I like java.
     *
     * @param str
     * @return
     */
    public static String removeBreakLineOfEnd(String str) {
        if (GerneralUtils.isEmptyString(str)) {
            return null;
        }
        return str.replaceAll("([^\n]*)\n+$", "$1");
    }

    /**
     * 将返回的JSON数据组装成一个HTML代码字符串
     *
     * @param data      HTML代码片段
     * @param pageTitle HTML页面的title名称
     * @param template  HTML模版
     * @return
     */
    public static String gernerateHTML(String data, String pageTitle, String template) {
        if (GerneralUtils.isEmptyString(data) || GerneralUtils.isEmptyString(template)) {
            return "";
        }
        if (GerneralUtils.isEmptyString(pageTitle)) {
            pageTitle = "New Page";
        }
        return String.format(template, data, pageTitle);
    }

    /**
     * 从网页meta标签中提取出页面编码，<br/>
     * 若页面未指定，默认返回null
     *
     * @param html
     * @return
     */
    public static String getCharsetFromMeta(String html) {
        Pattern pattern = Pattern.compile("<meta\\s+http-equiv=\"Content-Type\"\\s+content=\"[\\s\\S]*?charset=(\\S+?)\"\\s*/>");
        Matcher matcher = pattern.matcher(html);
        String charset = null;
        if (matcher.find()) {
            charset = matcher.group(1);
        }
        return charset;
    }

    /**
     * 替换HTML页面meta部分声明的charset编码
     *
     * @param html          html页面内容
     * @param targetCharset 目标编码
     * @return
     */
    public static String replaceHTMLCharsetFromMeta(String html, String targetCharset) {
        if (GerneralUtils.isEmptyString(html)) {
            return null;
        }
        if (GerneralUtils.isEmptyString(targetCharset)) {
            return html;
        }
        Pattern pattern = Pattern.compile("<meta\\s+http-equiv=\"Content-Type\"\\s+content=\"[\\s\\S]*?charset=(\\S+?)\"\\s*/>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String meta = matcher.group();
            String oldCharset = matcher.group(1);
            String newMeta = meta.replace(oldCharset, targetCharset);
            html = html.replace(meta, newMeta);
        }
        return html;
    }
}

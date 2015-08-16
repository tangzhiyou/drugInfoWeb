package com.skysoft.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: �ַ�����������
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
     * @Description: URL����(ԭ��:���ַ�ת����16����, Ȼ��ǰ���ǰ׺%, ����0-255��Χ֮���ַ���ǰ׺%u)
     * �������ַ�����.-*_,ע��ո�ᱻ����ɼӺ�+,���������޸���BUG
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
     * @Description: URL����(�Ὣ+�ӺŽ���ɿո�)
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
     * @Description: URL�������ַ�����(* + - . / @ _ 0-9 a-z A-Z���ᱻ����)
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
     * @Description: ��URI���������ı���(!#$&'()*+,-./:;=?@_~0-9a-zA-Z���ᱻ����)
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
     * @Description: ��URI�������ֽ��б���(!'()*-._~0-9a-zA-Z���ᱻ����)
     * ��Ҫ�����ڶ�URL���ݵ������ַ������Ĳ������ֽ��б���,�����ǰ�����URL���ݸ��˺���
     * eg:http://xxxx.do?qu=����&r=50%,���Բ���:���ĺ�50%��Ҫ����,
     * �����ֻ��Ҫ�����ĺ�50%���ݸ��˺������б���
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
     * @Description: URL�������ַ����룬��escape�����෴
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
     * ���ַ����ı�����ȡ�����ַ���,�磺xxxxxxx2/14/2013xxxxxxxx -->2/14/2013
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
     * ���ַ�������ȡ������(������ȡ������С��)
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
     * ���ַ�������ȡURL����,<br/>
     * ��:http://www.google.com.hk
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
     * ������ת���ɶ���Сʱ������ĸ�ʽ �磺12410000-->3Сʱ26����50��
     *
     * @param time
     * @return ��ʽ�����ʱ���ʽ
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
     * ������Сʱ������ĸ�ʽ�ַ���ת���ɺ�����(��formatMillSecond�����෴) �磺3Сʱ26����50��-->12410000
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
     * �޳�JSע��
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
     * Unicode�ַ���ת��������
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
     * �ж��ַ����Ƿ������ֽ�β
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
     * �����ַ����е�һ����д��ĸ������λ��
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
     * �޳��ַ�����β����Ļ��з� �磺I like java.\n\n\n\n --> I like java.
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
     * �����ص�JSON������װ��һ��HTML�����ַ���
     *
     * @param data      HTML����Ƭ��
     * @param pageTitle HTMLҳ���title����
     * @param template  HTMLģ��
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
     * ����ҳmeta��ǩ����ȡ��ҳ����룬<br/>
     * ��ҳ��δָ����Ĭ�Ϸ���null
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
     * �滻HTMLҳ��meta����������charset����
     *
     * @param html          htmlҳ������
     * @param targetCharset Ŀ�����
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

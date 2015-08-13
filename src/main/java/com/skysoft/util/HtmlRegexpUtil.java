package com.skysoft.util;

import org.apache.oro.text.regex.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用htmparser +正则表达式处理字符串
 */

public class HtmlRegexpUtil {
    private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

    private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签

    private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

    /**
     *
     */
    public HtmlRegexpUtil() {
        // TODO Auto-generated constructor stub
    }


    /**
     * 基本功能：替换标记以正常显示
     * <p/>
     *
     * @param input
     * @return String
     */
    public String replaceTag(String input) {
        if (!hasSpecialChars(input)) {
            return input;
        }
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
            }

        }
        return (filtered.toString());
    }

    /**
     * 基本功能：判断标记是否存在
     * <p/>
     *
     * @param input
     * @return boolean
     */
    public boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '>':
                        flag = true;
                        break;
                    case '<':
                        flag = true;
                        break;
                    case '"':
                        flag = true;
                        break;
                    case '&':
                        flag = true;
                        break;
                }
            }
        }
        return flag;
    }

    /**
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     * <p/>
     *
     *
     * @param tableText
     * @param str
     * @return String
     */
    public static String filterHtml(String tableText, String str) {
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 基本功能：过滤指定标签
     * <p/>
     *
     * @param str
     * @param tag 指定标签
     * @return String
     */
    public static String fiterHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 基本功能：替换指定的标签
     * <p/>
     *
     * @param str
     * @param beforeTag 要替换的标签
     * @param tagAttrib 要替换的标签属性值
     * @param startTag  新标签开始标记
     * @param endTag    新标签结束标记
     * @return String
     * @如：替换img标签的src属性值为[img]属性值[/img]
     */
    public static String replaceHtmlTag(String str, String beforeTag,
                                        String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag
                    .group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag
                        + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    //自定义截取相关内容
    public static String interceptContent(String StartRegExp, String EndRegExp,
                                          StringBuffer sbText) throws Exception {
        String sbConent = "";
        int startPos = 0, endPos = 0;
        Pattern spattern = Pattern.compile(StartRegExp);
        Matcher smatcher = spattern.matcher(sbText);
        boolean sresult = smatcher.find();
        if (sresult) {
            startPos = smatcher.start();
        }

        if (startPos != 0 || startPos != -1) {
            sbConent = sbText.substring(startPos);
            Pattern epattern = Pattern.compile(EndRegExp);
            Matcher ematcher = epattern.matcher(sbConent);
            boolean eresult = ematcher.find();
            if (eresult) {
                endPos = ematcher.end();
            }
        }

        if (startPos == 0 || startPos == -1 || endPos == 0 || endPos == -1) {

        } else {
            sbConent = sbText.substring(startPos, startPos + endPos);
            sbText.delete(startPos, startPos + endPos);
        }

        return sbConent;
    }

    //验证正确的URL地址
    public static boolean VerificationURL(String Regex, String url) {
        Pattern p = Pattern.compile(Regex, Pattern.CASE_INSENSITIVE);// 匹配<title>开头，</title>结尾的文档
        Matcher m = p.matcher(url);// 开始编译
        // System.out.println(m.find());
        boolean result = m.find();
        return result;
    }

    public void ORO() throws MalformedPatternException {
        String RegExp="<p>【([\u4E00-\u9FA5])*】([//s//S]*)([^》[\u4E00-\u9FA5]+</p>])*";
        org.apache.oro.text.regex.Pattern pattern = new Perl5Compiler().compile(RegExp);
        Perl5Matcher matcher = new Perl5Matcher();
        char[] Content = new char[0];
        PatternMatcherInput matcherInput = new PatternMatcherInput(Content);
        while (matcher.contains(matcherInput, pattern)) {
            MatchResult result = matcher.getMatch();
            //System.out.println(result.toString());
            for(int i = 0; i < result.groups(); i++) {
                System.out.printf("%s : %s\n", i, result.group(i));
            }
        }
    }

    public static String replaceIllegalCharacter(String source) {
        if (source == null)
            return source;
        String reg = "[\\n\\r\\t]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(source);
        return m.replaceAll("");
    }

    public static String EscapeString(String text) {
        return text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("’", "&apos;").replaceAll("\"", "&quot;");
    }

    public void testAppendReplacement() {
        //生成Pattern对象并且编译一个简单的正则表达式"Kelvin"
        Pattern p = Pattern.compile("Kelvin");
        //用Pattern类的matcher()方法生成一个Matcher对象
        Matcher m = p.matcher("Kelvin Li and Kelvin Chan are both working in Kelvin Chen's KelvinSoftShop company");
        StringBuffer sb = new StringBuffer();
        int i = 0;
        //使用find()方法查找第一个匹配的对象
        boolean result = m.find();
        //使用循环将句子里所有的kelvin找出并替换再将内容加到sb里
        while (result) {
            i++;
            m.appendReplacement(sb, "Kevin");
            System.out.println("第" + i + "次匹配后sb的内容是：" + sb);
            //继续查找下一个匹配对象
            result = m.find();
        }
        //最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
        m.appendTail(sb);
        System.out.println("调用m.appendTail(sb)后sb的最终内容是:" + sb.toString());
    }

    // 自定义截取相关内容
    public static String extractSpecificContent(String StartRegExp,
                                                String EndRegExp, String Content) throws Exception {
        StringBuffer sbConent = null;
        int startPos = 0, endPos = 0;
        sbConent = new StringBuffer();
        Pattern spattern = Pattern.compile(StartRegExp);
        Matcher smatcher = spattern.matcher(Content);
        boolean sresult = smatcher.find();
        while (sresult) {
            startPos = smatcher.start();
            sresult = smatcher.find();
        }
        System.out.println("startPos****:" + startPos);
        Pattern epattern = Pattern.compile(EndRegExp);
        Matcher ematcher = epattern.matcher(Content);
        boolean eresult = ematcher.find();
        while (eresult) {
            endPos = ematcher.start();
            eresult = ematcher.find();
        }
        System.out.println("endPos****:" + endPos);
        if (startPos == -1 || endPos == 0 || endPos == -1) {
            sbConent.append("");
        } else {
            sbConent.append(Content.substring(startPos, endPos));
        }
        return sbConent.toString();
    }

    public static List<String> ExtractTableText(String tableText, String contexturl, String h1Text) {
        return null;
    }
}

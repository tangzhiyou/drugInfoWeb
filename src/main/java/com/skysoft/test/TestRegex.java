package com.skysoft.test;

import com.skysoft.framework.HtmlParserTool;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.util.FileIOStreamTools;
import com.skysoft.util.HtmlRegexpUtil;
import com.skysoft.util.generateBean;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: pinaster
 * Date: 13-12-17
 * Time: 下午7:10
 */
public class TestRegex {
    public static void main(String[] args) throws Exception {
        String content= FileIOStreamTools.readerFileContent("conf/pharmnetdata.txt",null);
        content=HtmlParserTool.extractHtmlLabel(content,"body");
        content=replaceIllegalCharacter(content);

        StringBuffer sbcontent=new StringBuffer(content);

//        String tt=HtmlRegexpUtil.interceptContent("<h1 class=\"style10\">","</h1>",sbcontent);
//
//
//        tt=HtmlRegexpUtil.filterHtml(tt);
//        System.out.println(tt);
/*        tt=HtmlRegexpUtil.interceptContent("剂　　型：</td>","</td>\\s*</tr>",sbcontent);
        tt=HtmlRegexpUtil.filterHtml(tt);
        System.out.println(tt);
        tt=HtmlRegexpUtil.interceptContent("规　　格：</td>","</td>\\s*</tr>",sbcontent);
        tt=HtmlRegexpUtil.filterHtml(tt);
        System.out.println(tt);
*/      /*List<String> re=new ArrayList<String>();
        re.add("<h1 class=\"style10\">;</h1>");
        re.add("剂　　型：</td>;</td>\\s*</tr>");
        re.add("剂型说明：</td>;</td>\\s*</tr>");
        re.add("规　　格：</td>;</td>\\s*</tr>");
        re.add("参考价：</td>;</td>\\s*</tr>");
        re.add("执行地区：</td>;</td>\\s*</tr>");
        re.add("生效时间：</td>;</td>\\s*</tr>");
        re.add("生产企业：</td>;</td>\\s*</tr>");
        re.add("政策依据：</td>;</td>\\s*</tr>");
        ExtractCustomText(sbcontent,re);*/
        /*SAXReader reader = new SAXReader();
        Document document = reader.read("conf/DefaultRule.xml");// 读取XML文件
        document.setXMLEncoding("UTF-8");
        //System.out.println(document.asXML());
        String tt=formatXml(document,"utf-8",true);
        System.out.println(tt);*/

        ExtractCustomText(sbcontent, generateBean.GetRegexUtils("conf/DefaultRule.xml"));
    }

    public static List<String> ExtractCustomText(StringBuffer sbcontent,List<String> Regex) throws Exception {

        boolean isfetch=true;
        List<String> values=new ArrayList<String>();

        for (String re:Regex)
        {
            if (re.equals(";"))
            {
                continue;
            }
            String[] tt=re.split(";");
            String text= HtmlRegexpUtil.interceptContent(tt[0], tt[1], sbcontent);
            if (isfetch)
            {
                if (IsFetchData(text))
                {
                    //没有记录，终止爬取
                    break;
                }
                isfetch=false;
            }
            text=HtmlRegexpUtil.filterHtml(text).replaceAll("\\s*", "");
            values.add(text);
        }
        for(String link:values)
        {
            System.out.println(link);
        }
        return values;
    }


    public static  String TestStringBuffer(StringBuffer sbText)
    {
        Pattern patternForTag = Pattern.compile("<h1 class=\"style10\">",Pattern.CASE_INSENSITIVE);
//            Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(sbText);
//        boolean result = matcherForTag.find();
        int spos=0;
        int epos=0;
        if (matcherForTag.find())
        {
            spos=matcherForTag.end();

        }
        String tt=sbText.substring(spos);
        //sbText.delete(0,epos);
        return tt;
    }
    public static String replaceIllegalCharacter(String source) {
        if (source == null)
            return source;
        String reg = "[\n\r\t]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(source);
        return m.replaceAll("");
    }
    public static boolean IsFetchData(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return true;
        }
        return false;
    }
}

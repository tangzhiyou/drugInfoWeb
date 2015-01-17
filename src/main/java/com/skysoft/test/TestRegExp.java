package com.skysoft.test;

import com.skysoft.util.FileIOStreamTools;
import com.skysoft.util.HtmlParserTool;
import com.skysoft.util.HtmlRegexpUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
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


        String content= FileIOStreamTools.readerFileContent("conf/pharmnetdata.txt", null);
        content= HtmlParserTool.extractHtmlLabel(content, "body");
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

//        ExtractCustomText(sbcontent, generateBean.GetRegexUtils("conf/DefaultRule.xml"));




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

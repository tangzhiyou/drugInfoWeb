package com.skysoft.handler;

import com.skysoft.domain.SFDARule;
import com.skysoft.framework.CrawlURI;
import com.skysoft.framework.HtmlParserTool;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.util.FileIOStreamTools;
import com.skysoft.util.HtmlRegexpUtil;
import org.apache.commons.lang3.StringUtils;

/**
* User: pinaster
* Date: 14-1-8
* Time: 上午11:54
*/
// 增加爬取 实例demo
public class SFDARuleController {
    private static SFDARule srule;

    public static void main(String[] args) {
        // 先写出思路,
        //  先一些重要的栏目进行更新
        //  先学会分割
        //  URL状态有效 无效

        // 怎么样在已经更新的数据里面，
//        不希望从头来一遍的时候 东西比较多，希望能分割一下
//        String currentUrl="http://app1.sfda.gov.cn/datasearch/face3/content.jsp?tableId=25&tableName=TABLE25&tableView=%E5%9B%BD%E4%BA%A7%E8%8D%AF%E5%93%81&Id=2";
//        String content= NetWorkHandlerData.fetchNetWorkData(currentUrl,"utf-8");
//        String text= HtmlParserTool.extractHtmlText(content,"td[colspan=2]>span");
//        System.out.println(text+"hello world");
//        HandlerData();
//        CrawlURI crawler=new CrawlURI();
//        crawler.toString();
        new SFDARuleController().HandlerData();
    }

    public void  initialize()
    {
        srule=new SFDARule();
    }
    public void HandlerData()
    {
        String currentUrl="http://app1.sfda.gov.cn/datasearch/face3/content.jsp?tableId=25&tableName=TABLE25&tableView=%E5%9B%BD%E4%BA%A7%E8%8D%AF%E5%93%81&Id=";

       /*for (int i=2000;i<=20000;i++)
       {

//           currentUrl=currentUrl+i;
           String content= NetWorkHandlerData.fetchNetWorkData(currentUrl+i,"utf-8");
           String text= HtmlParserTool.extractHtmlText(content,"td[colspan=2]>span");
           if (StringUtils.isNotBlank(text))
           {
//               System.out.println(currentUrl+i+":"+text);
               FileIOStreamTools.saveURL(currentUrl + i + "没有有记录", "conf/test.txt");;
           }
       }*/

       //　先写一个程序，然后写一个定时器就行。



       // 初化始的时候
        int lastID,count;

       for (lastID=22,count=lastID+2000;lastID<=count;lastID++)
       {
//           currentUrl=srule.getSiteDomain()+"content.jsp?tableId="+srule.getTableId()+"&tableName=TABLE"+srule.getTableId()+"&tableView=%E5%9B%BD%E4%BA%A7%E8%8D%AF%E5%93%81&Id="+lastID;
           String content= NetWorkHandlerData.fetchNetWorkData(currentUrl+lastID,"utf-8");
           if (isfetchData(content))
           {
              continue;
           }else
           {
//               HtmlRegexpUtil.ExtractTableText(content,currentUrl,"utf-8");
           }
       }



        // 类的描述 一切皆是对象
        //  状态为已经爬取，未爬取，＋页面的内容的前三条信息的对应MD5值  ＋数据标记信息 +更新次数


        //MD5值时不对，这要看这个网页信息是否存在。如果不存在标志为－1

        //如果这个网页的修改经常变化，或者新增数据记录，就添加到增量爬取当中。。

        // 如果更新次数多，就到经常更新范围。

    }
    public static boolean isfetchData(String content)
    {
        String text= HtmlParserTool.extractHtmlText(content,"td[colspan=2]>span");
        if (StringUtils.isNotBlank(text))
        {
            return true;
        }
        return  false;
    }

    public static void extractText(String context, String contexturl) {

    }
}

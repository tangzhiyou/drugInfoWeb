package com.skysoft.test;

import com.skysoft.framework.HtmlParserTool;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.util.FileIOStreamTools;
import com.skysoft.util.URLEncoderTools;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.util.ArrayList;
import java.util.List;

/**
 * User:  tangzhiyou
 * User:  14-1-22
 * User:  上午11:40
 */
public class TestAnccGDS {
    public static void main(String[] args) throws Exception {
//        NetWorkHandlerData.ImitateLoginSite("http://search.anccnet.com/searchResult2.aspx?keyword=%CE%ACC%D2%F8%C7%CC%C6%AC&gdsBtn=%C9%CC%C6%B7%CB%D1%CB%F7","utf-8",3);
//        String content=NetWorkHandlerData.fetchNetWorkData("http://search.anccnet.com/searchResult2.aspx?__VIEWSTATE=%2FwEPDwULLTEzODQxNzY5NjNkZEc4gDy0wp5ERjILg2b7lTTH3F%2Bw&__EVENTVALIDATION=%2FwEWAwKK7u6vCQLd5eLQCQLmjL2EBxmZU7jWYoh9371phOcBPCjfgdVD&keyword=%B8%B4%B7%BD%B0%B1%CE%AC%BD%BA%C4%D2&gdsBtn=%C9%CC%C6%B7%CB%D1%CB%F7","gb2312");
//        System.out.println(content);
//        initFetchData();
//        String context= FileIOStreamTools.ReadFileContent("C:\\Users\\tangzhiyou\\Desktop\\test.html");
//        String bodyContent= HtmlParserTool.extractHtmlLabel(context, "body");
//        System.out.println(HtmlParserTool.extractHtmlText(bodyContent,"div[class=result]"));
//           NetWorkHandlerData.SearchGDS("http://search.anccnet.com/searchResult2.aspx?keyword=%CA%B7%B9%FA%B9%AB%D2%A9%BE%C6&gdsBtn=%C9%CC%C6%B7%CB%D1%CB%F7","");
    initFetchData();
    }
    public static void initFetchData() throws Exception {
        String keyword= URLEncoderTools.GBKURLencode("维C银翘片");
//        String keyword="%B8%B4%B7%BD%B0%B1%CE%AC%BD%BA%C4%D2";
        String currentUrl="http://search.anccnet.com/searchResult2.aspx?__VIEWSTATE=%2FwEPDwULLTEzODQxNzY5NjNkZEc4gDy0wp5ERjILg2b7lTTH3F%2Bw&__EVENTVALIDATION=%2FwEWAwKK7u6vCQLd5eLQCQLmjL2EBxmZU7jWYoh9371phOcBPCjfgdVD&gdsBtn=%C9%CC%C6%B7%CB%D1%CB%F7&keyword="+keyword;
//        startFetchData(currentUrl,"gb2312");

    }




    public static void extractText(String context, String contexturl) {
        System.out.println(context);
    }

}

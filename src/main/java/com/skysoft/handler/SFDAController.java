package com.skysoft.handler;

import com.skysoft.domain.Druggds;
import com.skysoft.framework.HttpClientUtils;
import com.skysoft.framework.Result;
import com.skysoft.framework.StringEntityHandler;
import com.skysoft.service.SFDAService;
import com.skysoft.util.HtmlParserTool;
import com.skysoft.util.HtmlRegexpUtil;
import com.skysoft.util.StringUtils;
import com.skysoft.util.generateBean;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Controller
@RequestMapping("/sfda")
public class SFDAController {
    private Map<String, String> headers=new HashMap<String,String>();
    private Map<String, String> params=new HashMap<String,String>();
    private long totalCount=0;
    private long PAGE_SIZE=0;
    public static final Logger logger = LoggerFactory.getLogger(SFDAController.class);

    @Autowired
    private SFDAService sfdaService;

    @RequestMapping("fetchCFDA")
    public String initialize() {

        return "fetchCFDA";
    }

    @RequestMapping(value = "/parserData", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String show(HttpServletRequest request, HttpServletResponse response) {
        headers.put("Host","app1.sfda.gov.cn");
        headers.put("Connection","keep-alive");
        headers.put("cache-control","no-cache");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.125 Safari/537.36");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept","*/*");
        headers.put("Referer","http://app1.sfda.gov.cn/datasearch/face3/base.jsp?tableId=25&tableName=TABLE25&title=%B9%FA%B2%FA%D2%A9%C6%B7&bcId=124356560303886909015737447882");
        headers.put("Cookie","JSESSIONID=A5628F03D8F8DC56530DFF1F51248606.7; __utmz=59192815.1439557795.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); _gscu_1358151024=39557794bq45j369; _gscs_1358151024=t39561618wx78d669|pv:1; _gscbrs_1358151024=1; __utma=59192815.494353602.1439557795.1439557795.1439561619.2; __utmb=59192815; __utmc=59192815; _gscu_1586185021=39557939cnh10x19; _gscs_1586185021=t39560524ji7n2418|pv:17; _gscbrs_1586185021=1");
        headers.put("Accept-Language","zh-CN,zh;q=0.8");
        headers.put("Accept-Encoding","gzip, deflate");


        params.put("tableId","25");
        params.put("bcId","124356560303886909015737447882");
        params.put("tableName","TABLE25");
        params.put("viewtitleName","COLUMN167");
        params.put("viewsubTitleName","COLUMN166,COLUMN170,COLUMN821");
        params.put("curstart","1");
        params.put("tableView","%E5%9B%BD%E4%BA%A7%E8%8D%AF%E5%93%81");
        Map<String, String> webGather = parserRuleXml("conf/DruggdsRule.xml");
        int batchUpdate;
        String host = "", specialTag, characterset, currentURL, dataTable = "";
        Iterator iter = webGather.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            if (key.equals("SiteDomain")) {
                host = entry.getValue().toString();
            }
            if (key.equals("characterset")) {
                characterset = entry.getValue().toString();
            }
            if (key.equals("batchUpdate")) {
                batchUpdate = Integer.parseInt(entry.getValue().toString());
            }
            if (key.equals("specialTag")) {
                specialTag = entry.getValue().toString();
            }
            if (key.equals("DataTable")) {
                dataTable = entry.getValue().toString();
            }
            if (key.equals("CurrentURL")) {
                currentURL = entry.getValue().toString();
            }
        }

        getTotalCount();
        for(int page=1;page<=PAGE_SIZE;page++)
        {
                params.put("curstart",String.valueOf(page));
                Result result= null;
                String content=null;
                StringEntityHandler entityHandler = new StringEntityHandler();
                try {
                        Thread.sleep(1500);
                        System.out.println("---------------程序休息1.5s------------");
                    result = HttpClientUtils.post("http://app1.sfda.gov.cn/datasearch/face3/search.jsp", headers, params);

                    content=entityHandler.handleEntity(result.getHttpEntity());
                } catch (IOException e) {
                    System.out.println("--------------error--------------");
                    e.printStackTrace();
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            List<String> tables01=HtmlParserTool.extractHtmlLabel(content, "table");
                List<String> urls=HtmlParserTool.extractAttributeHref(tables01.get(2),"a");
                for(String str:urls)
                {
                    String url=str.substring(str.indexOf("\'")+1, str.lastIndexOf("\'"));
                    try {
                            Thread.sleep(1500);
                            System.out.println("---------------程序休息1.5s------------");
                        String html=HttpClientUtils.getHTML(host+url);
                        extractText(html);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        }
        return "successful";
    }
    public void extractText(String context) {
        List<String> values=HtmlParserTool.extractTable(context, "table");
        List<String>  copyvalues=new ArrayList<String>();
        copyvalues.addAll(values.subList(1,15));
        copyvalues.addAll(values.subList(16, 17));
        saveContent(copyvalues);
    }

    // 映射到pojo
    private void saveContent(List<String> list) {
        List<String> fieldNameList=generateBean.getObjectProperty(Druggds.class);
        fieldNameList=fieldNameList.subList(1,fieldNameList.size());
        Druggds druggds=new Druggds();
        for(int i=0;i<fieldNameList.size();i++)
        {
            try {
                BeanUtils.setProperty(druggds,fieldNameList.get(i),list.get(i));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        sfdaService.saveBeans(Arrays.asList(druggds));
    }

    private Map<String, String> parserRuleXml(String filepath) {

        HashMap<String, String> confValue = new HashMap<String, String>();
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(SFDAController.class.getClassLoader().getResourceAsStream(filepath));
            Element employees = (Element) document
                    .selectSingleNode("//webGather");
            for (Iterator<Element> i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                confValue.put(employee.getName(), employee.getText());
            }
        } catch (DocumentException e) {
            logger.error("解析规则文件出错", e);
        }
        return confValue;
    }

    private void getTotalCount(){
        String currentURL = "http://app1.sfda.gov.cn/datasearch/face3/base.jsp?tableId=25&tableName=TABLE25&title=%B9%FA%B2%FA%D2%A9%C6%B7&bcId=124356560303886909015737447882";
        String html= null;
        try {
            html = HttpClientUtils.getHTML(currentURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> contents= HtmlParserTool.extractHtmlLabel(html, "div#content");
        List<String> tables=HtmlParserTool.extractHtmlLabel(contents.get(0), "table");
        String text=tables.get(4);
        String ext=text.substring(text.indexOf("<td width=\"160\">"), text.indexOf("</td>"));
        ext= HtmlRegexpUtil.filterHtml(ext);
        List<String> totalValues= StringUtils.getNumbericFromString(ext);
        PAGE_SIZE=Long.valueOf(totalValues.get(1));
        totalCount=Long.valueOf(totalValues.get(2));
    }
}

package com.skysoft.handler;

import com.skysoft.util.*;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.framework.fetchDatabase;
import com.skysoft.service.CFDAService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: pinaster
 * Date: 13-11-28
 * Time: 下午2:05
 */
@Controller
@RequestMapping("/yaozh")
public class yaozhController {
    private static fetchDatabase worker;
    private static String host = null;
    private static String SearchPages = null;
    private static String ImportFile = null;
    private static String StartRegExp = null;
    private static String EndRegExp = null;
    private static String MappingXML = null;
    private static String currentURL = null;
    private static String characterset = null;
    private static String Datatable = null;
    private static int batchUpdate = 0;
    public static Set<String> links = null;

    static Logger logger = LoggerFactory.getLogger(yaozhController.class);
    @Autowired
    private CFDAService cfdaService;

    @RequestMapping("fetchCFDA")
    public String initialize() {
        worker = new fetchDatabase();
        worker.createAndBindDatabase("Resources/BDBdatas/fetchData");
        links = new HashSet<String>();
        return "fetchCFDA";
    }

    @RequestMapping(value = "/parserData", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String HandlerData(HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, String> webGather = FileIOStreamTools.readerDataHashMap("conf/DefaultRule.xml", null);
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
            if (key.equals("SearchPages")) {
                SearchPages = entry.getValue().toString();
            }
            if (key.equals("batchUpdate")) {
                batchUpdate = Integer.parseInt(entry.getValue().toString());
            }
            if (key.equals("Datatable")) {
                Datatable = entry.getValue().toString();
            }
            if (key.equals("ImportFile")) {
                ImportFile = entry.getValue().toString();
            }
            if (key.equals("StartRegExp")) {
                StartRegExp = entry.getValue().toString();
            }
            if (key.equals("EndRegExp")) {
                EndRegExp = entry.getValue().toString();
            }
            if (key.equals("MappingXML")) {
                MappingXML = entry.getValue().toString();
            }
        }

        if (ImportFile.equals("")) {
            //links=HtmlParserTool.extractLinks(SearchPages,null);
        } else {
            System.out.println("从文件内导入URL数据");
            links = FileIOStreamTools.readerDataSet(ImportFile, host);
        }

        for (String link : links) {
            String Content = NetWorkHandlerData.fetchNetWorkData(link, characterset);

            extractText(Content, link);

        }

        return "successful";
    }


    public static void extractText(String context, String contexturl) {
        //先得到当前区域
        String useful = HtmlParserTool.extractHtmlLabel(context, "div#mall_wrap");
        try {
             String currRegion= HtmlParserTool.extractSpecificContent("当前位置：",
                    "深圳仁爱医院</a>",useful);
            String tableText = null;
            currRegion= HtmlRegexpUtil.filterHtml(tableText, currRegion);
            System.out.println(currRegion.replaceAll("&nbsp;","").replaceAll("&gt;",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> currRegion = new ArrayList<String>();
        Document doc = Jsoup.parse(useful);
        String HtmlText = "";
        Elements values = doc.select("div#mall_wrap a");
        for (Element link : values) {
            String linkText = link.text();
            currRegion.add(linkText);
        }
        StringBuffer RegionText = null;
        RegionText = new StringBuffer();
        for (int i = 0; i < currRegion.size(); i++) {
            if (i == 4) {
                break;
            }
            RegionText.append(String.format("%s->", currRegion.get(i)));
        }
        //System.out.println("当前的区域" + RegionText.toString());
        StringBuffer sbHospitalInfo = null;
        sbHospitalInfo = new StringBuffer();
        // 医院简况
      String tableText = HtmlParserTool.extractHtmlLabel(useful, "div#tagContent0 table");
        String divRegexp = "<div\\s*(\\w+(=('|\").*?\")\\s*)*>([\\s\\S]*)</div>";
        String DivContent = HtmlRegexpUtil.filterHtml(tableText, divRegexp);
        String aRegexp = "<a\\s*(\\w+(=('|\").*?\")\\s*)*>([\\s\\S]*)</a>";
        String AContent = HtmlRegexpUtil.filterHtml(DivContent, aRegexp);
        sbHospitalInfo.append(HtmlParserTool.extractHtmlText(AContent, "table"));
        // 医院介绍
//        String tableText = HtmlParserTool.extractHtmlLabel(useful, "div#tagContent1 table");
        String StringText = HtmlParserTool.extractHtmlText(tableText, "table");
        System.out.println(StringText);
        // 医院科室

        // 联系方式

        System.out.println(sbHospitalInfo.toString());
    }

    // 映射到pojo
    private static void saveContent(List<String> list) {
        Object obj = generateBean.newInstance(MappingXML);
        Map<Integer, String> methodMap = generateBean
                .methodname(MappingXML);
        try {
            Object data = ReflectUtil.toEntityList(obj, list, methodMap);
            worker.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("当前爬取容器的数量**************************:"
                + worker.size());
    }

}
package com.skysoft.handler;

import com.skysoft.util.*;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.framework.fetchDatabase;
import com.skysoft.service.CFDAService;
import org.apache.commons.lang.StringUtils;
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
 * Date: 13-12-17
 * Time: 下午3:00
 */
@Controller
@RequestMapping("/pharmnet")
public class pharmnetController {
    private static fetchDatabase worker;
    private static String ImportFile = null;
    private static String MappingXML = null;
    private static String characterset = null;
    private static String Datatable = null;
    private static int batchUpdate = 0;
    public static Set<String> links = null;
    static Logger logger = LoggerFactory.getLogger(pharmnetController.class);
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

            if (key.equals("characterset")) {
                characterset = entry.getValue().toString();
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
            if (key.equals("MappingXML")) {
                MappingXML = entry.getValue().toString();
            }
        }


        if (StringUtils.isBlank(ImportFile)) {
            //links=HtmlParserTool.extractLinks(SearchPages,null);
        } else {
            System.out.println("从文件内导入URL数据");
            links = FileIOStreamTools.readerDataSet(ImportFile, "");
        }

        for (String link : links) {
            if (StringUtils.isBlank(link)) {
                continue;
            }
            System.out.println("当前爬取的URL地址:............." + link);
            String Content = NetWorkHandlerData.fetchNetWorkData(link, characterset);

            extractText(Content, link);
            if (worker.size() == batchUpdate) {
                cfdaService.save(worker.map, Datatable);
            }
        }
        if (worker.size() >= 1) {
            cfdaService.save(worker.map, Datatable);
        }
        return "successful";
    }

    public static void extractText(String context, String contexturl) {
        String TableText = HtmlParserTool.extractHtmlLabel(context, "table[class=bian221]");
        String H1Text = HtmlParserTool.extractHtmlText(TableText, "h1>a");
        List<String> listText= HtmlRegexpUtil.ExtractTableText(TableText, contexturl, H1Text);
        if (listText.size()==0)
        {
            return;
        }
        saveContent(listText);
    }

    // 映射到pojo
    private static void saveContent(List<String> list) {

        Object obj = generateBean.newInstance(MappingXML);
        Map<Integer, String> methodMap = generateBean
                .methodname(MappingXML);
        if (list.size() == 10) {
            methodMap.remove("unit");
        }
        if (list.size() == 9) {
            methodMap.remove("dosageComment");
        }

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

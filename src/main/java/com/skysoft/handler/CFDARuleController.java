package com.skysoft.handler;

import com.skysoft.framework.BdbPersistentQueue;
import com.skysoft.framework.fetchDatabase;
import com.skysoft.service.CFDAService;
import com.skysoft.util.ReflectUtil;
import com.skysoft.util.generateBean;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Controller
@RequestMapping("/drugInfoWeb")
public class CFDARuleController {
    private static Queue<String> persistentQueue;
    private static fetchDatabase worker;
    static Logger logger = LoggerFactory.getLogger(CFDARuleController.class);

    @Autowired
    private CFDAService cfdaService;

    @RequestMapping("fetchCFDA")
    public String initialize() {
        // 创建url数据库文件
        String dbDir = "Resources/BDBdatas/url";
        File file = new File(dbDir);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        // 创建爬取数据库文件
        persistentQueue = new BdbPersistentQueue(dbDir, "fetchData",
                String.class);

        worker = new fetchDatabase();
        worker.createAndBindDatabase("Resources/BDBdatas/fetchData");

        return "fetchCFDA";
    }

    @RequestMapping(value = "/parserData", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String show(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> data = new HashMap<String, String>();
        // 规则名称
        String GatherName = WebUtils.findParameterValue(request, "GatherName");
        // 站点域名
        String SiteDomain = WebUtils.findParameterValue(request, "SiteDomain");
        data.put("GatherName", GatherName);
        data.put("SiteDomain", SiteDomain);
        // 存储采集规则xml文件
        // createCFDARule(GatherName+".xml", data);
        // 读取配置文件
        Map<String, String> webGather = parserRuleXml("conf/DefaultRule.xml");

        int startpos = 0, endpos = 0, tableId = 0, batchUpdate = 0;
        String host = "", specialTag, characterset, currentURL, Datatable = "";
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
            if (key.equals("tableId")) {
                tableId = Integer.parseInt(entry.getValue().toString());
            }
            if (key.equals("startID")) {
                startpos = Integer.parseInt(entry.getValue().toString());
            }
            if (key.equals("EndID")) {
                endpos = Integer.parseInt(entry.getValue().toString());
            }
            if (key.equals("batchUpdate")) {
                batchUpdate = Integer.parseInt(entry.getValue().toString());
            }
            if (key.equals("specialTag")) {
                specialTag = entry.getValue().toString();
            }
            if (key.equals("Datatable")) {
                Datatable = entry.getValue().toString();
            }
        }
        for (int id = startpos; id <= endpos; id++) {
            currentURL = host + "/content.jsp?" + "tableId=" + tableId
                    + "&tableName=TABLE" + tableId + "&"
                    + "tableView=%E5%9B%BD%E4%BA%A7%E8%8D%AF%E5%93%81&Id=" + id;
            persistentQueue.add(currentURL);
        }
        while (persistentQueue.size() >= 1 || worker.size() >= 1 && persistentQueue.size() == 0) {
            if (worker.size() == batchUpdate || worker.size() >= 1 && persistentQueue.size() == 0) {
                cfdaService.save(worker.map, Datatable);
            }
            if (persistentQueue.size() == 0) {
                break;
            }

            System.out.println("persistentQueue.size()*******:"
                    + persistentQueue.size());
            String urlText = persistentQueue.remove();
            try {
                startFetchData(urlText);
            } catch (Exception e) {
                logger.error("开始爬取数据出错{}", e);
            }

        }
        return "fetchCFDA";
    }

    // 获取要抓取的url列表
    public static void startFetchData(String url) {
        System.out.println("正在爬取的页面" + url);
        if (url.equals("")) {
            return;
        }
        HttpClient httpclient = new HttpClient();
        try {
            // 设置 Http 连接超时为5秒
            httpclient.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000000);
            httpclient.getHttpConnectionManager().getParams()
                    .setSoTimeout(5000000);

        } catch (Exception e) {
            logger.error("这是连接服务器出错了{}", url);
            // 设置 Http 连接超时为5秒
            httpclient.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000000);
            httpclient.getHttpConnectionManager().getParams()
                    .setSoTimeout(5000000);
        }

        GetMethod get = new GetMethod(url);
        // 设置 get 请求超时为 5 秒
        get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(100, true));
        try {
            int statusCode = httpclient.executeMethod(get);
            if (statusCode == HttpStatus.SC_OK) { // 打印服务器返回的状态
                byte[] responseBody = get.getResponseBodyAsString().getBytes(
                        get.getResponseCharSet());
                String response = new String(responseBody, "utf-8");
                extractText(response, url);
                get.releaseConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("这是得到页面出错了{}", url);
        }
    }

    public static void extractText(String context, String contexturl) {

    }

    // 映射到pojo
    private static void saveContent(List<String> list) {
        Object obj = generateBean.newInstance("conf/DefaultRule.xml");
        Map<Integer, String> methodMap = generateBean
                .methodname("conf/DefaultRule.xml");
        try {
            Object data = ReflectUtil.toEntityList(obj, list, methodMap);
            worker.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("当前爬取容器的数量**************************:"
                + worker.size());
    }

    // 处理没有记录的页面

    public void createCFDARule(String RuleName, Map<String, String> rule) {
        System.out.println("创建xml");
        Document root = DocumentHelper.createDocument();
        Element webGather = root.addElement("webGather");
        Iterator iter = rule.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            if (key.equals("GatherName")) {
                Element GatherName = webGather.addElement("GatherName");
                GatherName.setText(entry.getValue().toString());
            }
            if (key.equals("SiteDomain")) {
                Element SiteDomain = webGather.addElement("SiteDomain");
                SiteDomain.setText(entry.getValue().toString());
            }
        }

        try {
            Writer fileWriter = new FileWriter("conf/" + RuleName);
            XMLWriter xmlWriter = new XMLWriter(fileWriter);
            xmlWriter.write(root);
            xmlWriter.close();
        } catch (IOException e) {
            logger.error("解析规则文件出错", e);
        }

    }

    private Map<String, String> parserRuleXml(String filepath) {
        HashMap<String, String> confValue = new HashMap<String, String>();
        File inputXml = new File(filepath);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputXml);
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
}

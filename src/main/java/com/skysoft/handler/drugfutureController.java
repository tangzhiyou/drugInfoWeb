package com.skysoft.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.skysoft.framework.BdbPersistentQueue;
import com.skysoft.framework.fetchDatabase;
import com.skysoft.service.FutureService;
import com.skysoft.util.generateBean;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
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

@Controller
@RequestMapping("/drugfuture")
public class drugfutureController {

    private static Queue<String> persistentQueue;
    private static fetchDatabase worker;
    static Logger logger = LoggerFactory.getLogger(drugfutureController.class);
    public static Map<Integer, String> IndexMap = null;

    @Autowired
    private FutureService fetureService;

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
        IndexMap = new HashMap<Integer, String>();

        return "fetchCFDA";
    }

    @RequestMapping(value = "/parserData", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String HandlerData(HttpServletRequest request,
                              HttpServletResponse response) {

        // 读取配置文件
        Map<String, String> webGather = parserRuleXml("conf/drugfuture.xml");
        int batchUpdate = 0;
        String DataIndex = "";
        String host = "", specialTag, currentURL, characterset, Datatable = "";
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
            if (key.equals("DataIndex")) {
                DataIndex = entry.getValue().toString();
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
        // 处理待爬取URL
        // 取出真正的索引位置
        char[] sIndex = DataIndex.substring(DataIndex.lastIndexOf('-') + 1)
                .toCharArray();
        // char[]
        // eIndex=startIndex.substring(endIndex.lastIndexOf('-')+1).toCharArray();
        // 从索引A开始
        // currentURL=host+"/"+DataIndex.substring(0,DataIndex.lastIndexOf('-')+1)+sIndex.toString()+".html";
        currentURL = host + "/" + DataIndex + ".html";
        System.out.println("正在爬取A索引:" + currentURL);
        // 有几种状态
        // 索引A全部的链接 ,A链接的子链接项 ,缓存相关的分页的URL
        fetchData(currentURL, host, 0);
        Iterator iterIndex = IndexMap.entrySet().iterator();
        while (iterIndex.hasNext()) {
            Map.Entry entry = (Map.Entry) iterIndex.next();
            Object key = entry.getKey();
            System.out.println("待爬取集合:" + entry.getValue().toString());
            fetchData(entry.getValue().toString(), host, 1);
            if (worker.size() >= 1) {
                fetureService.save(worker.map, Datatable);
            }
        }
        return "";
    }

    // 获最索引A的全部链接

    public static void fetchData(String url, String host, int type) {
        // System.out.println("正在爬取的页面" + url);
        if (url.equals("")) {
            return;
        }
        if (type == 2 || type == 1) {
            System.out.println("正在爬取的页面" + url);
        }
        HttpClient httpclient = new HttpClient();
        try {
            // 设置 Http 连接超时为5秒
            httpclient.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000000);
            httpclient.getHttpConnectionManager().getParams()
                    .setSoTimeout(5000000);

        } catch (Exception e) {
            // logger.error("这是连接服务器出错了{}", url);
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
                if (type == 0) {
                    extractIdexALL(host, response);
                } else if (type == 1) {
                    HandlerSubIndexData(response, host);
                } else if (type == 2) {
                    ParserTable(response);
                }
                get.releaseConnection();
            }
        } catch (Exception e) {
            logger.error("这是得到页面出错了{}", url);
        }
    }

    private static void extractIdexALL(String host, String response) {
        // TODO Auto-generated method stub
        String fileName = null;
        Document doc = Jsoup.parse(response);
        Elements values = doc.select("ul>li>a[href]");
        int count = 0;
        for (Element link : values) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            System.out.println(host + "/" + linkHref);
            IndexMap.put(count++, host + "/" + linkHref);
        }
    }

    // 把URL列表保存到文件
    public static void saveURL(String content, String fileName) {
        try {
            fileName = "./Resources/Data/" + fileName;
            File file = new File(fileName);
            FileWriter fwriter = new FileWriter(fileName, true);
            BufferedWriter bfwriter = new BufferedWriter(fwriter);
            bfwriter.newLine();
            bfwriter.write(content, 0, content.length());
            bfwriter.flush();
            bfwriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 先得到这个页面的相关属性

    // 共 1 页 当前第 1 页　

    public static void HandlerSubIndexData(String content, String host) {
        // 放到一个list集合里面

        // 思路写在这里
        // 如果用分页显示的时候，要得到这个URL，用多少个URL集合，放到一个hashMap里面
        String pageURL = "";
        List<String> countlist = new ArrayList<String>();
        Document doc = Jsoup.parse(content);
        int index = 0;
        Elements values = doc.select("div>font");
        for (Element link : values) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            System.out.println(linkText);
            countlist.add(index++, linkText);
        }
        // fdaSearch.aspx?DrugName=ABACAVIR&page=2
        // fdaSearch.aspx?DrugName=ABACAVIR&page=3
        int u = 0;
        List<String> urllist = new ArrayList<String>();
        Elements links = doc.select("div>a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            pageURL = host + "/" + linkHref;
            pageURL = pageURL.substring(0, pageURL.length() - 1);
            break;
            //urllist.add(u++, host+"/" + linkHref);
        }
        int countPage = 0, currentPage = 0, count;
        for (int i = 1; i < countlist.size(); i++) {
            String value = String.format("%s", countlist.get(i));
            if (i == 1) {
                count = Integer.parseInt(value);
            } else if (i == 2) {
                countPage = Integer.parseInt(value);
            } else if (i == 3) {
                currentPage = Integer.parseInt(value);
            }
        }

        Map<Integer, String> pagingMap = new HashMap<Integer, String>();
        for (int i = 0; i < urllist.size() - 3; i++) {

            String value = String.format("%s", countlist.get(i));
            pagingMap.put(i, host + "/" + value);
        }
        try {
            ParserTable(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 取出要待爬取的URL地址
        for (int m = currentPage + 1; m <= countPage; m++) {
            // logger.debug("取出要待爬取的URL地址：{}{}",currentPage,countPage);
            System.out.println("当前m的值:" + m + "currentPage=***:" + currentPage
                    + "countPage=***:" + countPage);
            //计算真正的待抓取url
//			pageURL=pageURL.substring(0,pageURL.length()-1);
            String value = pageURL + m;
            System.out.println(value);
            fetchData(value, host, 2);
        }
    }

    public static void ParserTable(String context) throws Exception {

        List listText = null;
        Parser myParser = null;
        NodeList nodeList = null;
        try {
            myParser = new Parser(context);
            myParser.setEncoding("utf-8");
            NodeFilter filter = new NodeClassFilter(TableTag.class);
            nodeList = myParser.extractAllNodesThatMatch(filter);
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            logger.error("解析页面数据出错", e);
        }
        // 设置页面编码
        int count = 0;
        listText = new ArrayList();
        TableTag table = (TableTag) nodeList.elementAt(0);
        for (int j = 0; j < table.getRowCount(); j++) {
            TableRow tRow = table.getRow(j);
            TableColumn[] columns = tRow.getColumns();
            for (int k = 0; k < columns.length; k++) {
                String text = columns[k].toPlainTextString();
                if (k == 2 && j % 6 == 0 || k == 2 && j % 6 == 1 || k == 4
                        && j % 6 == 1) {
                    listText.add(count++, text);
                    //System.out.println(text);
                } else if (k % 2 == 1 && j % 6 != 1 && j % 6 != 0) {
                    listText.add(count++, text);
                    //System.out.println(text);
                }
            }

        }
        saveContent(listText);
    }

    // 映射到pojo
    private static void saveContent(List<String> list) {
        Object obj = generateBean.newInstance("conf/drugfuture.xml");
        Map<Integer, String> methodMap = generateBean
                .methodname("conf/drugfuture.xml");
        try {
//            List<Object> data = ReflectManyRelations.toEntityList(obj, list, methodMap);
            List<Object> data =null;
            worker.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("当前爬取容器的数量**************************:"
                + worker.size());
    }

    private Map<String, String> parserRuleXml(String filepath) {
        HashMap<String, String> confValue = new HashMap<String, String>();
        File inputXml = new File(filepath);
        SAXReader saxReader = new SAXReader();
        try {
            org.dom4j.Document document = saxReader.read(inputXml);
            org.dom4j.Element employees = (org.dom4j.Element) document
                    .selectSingleNode("//webGather");
            for (Iterator<org.dom4j.Element> i = employees.elementIterator(); i
                    .hasNext(); ) {
                org.dom4j.Element employee = (org.dom4j.Element) i.next();
                confValue.put(employee.getName(), employee.getText());
            }
        } catch (DocumentException e) {
            logger.error("解析规则文件出错", e);
        }
        return confValue;
    }

}

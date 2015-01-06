package com.skysoft.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.skysoft.framework.fetchDatabase;
import com.skysoft.service.CFDAService;
import com.skysoft.service.FutureService;
import com.skysoft.util.ReflectUtil;
import com.skysoft.util.generateBean;

@Controller
@RequestMapping("/18ladys")
public class _18ladysController {
	private static fetchDatabase worker;
	static Logger logger = LoggerFactory.getLogger(_18ladysController.class);
	public static Map<Integer, String> IndexMap = null;

	@Autowired
	private CFDAService cfdaService;

	@RequestMapping("fetchCFDA")
	public String initialize() {
		worker = new fetchDatabase();
		worker.createAndBindDatabase("Resources/BDBdatas/fetchData");
		IndexMap = new HashMap<Integer, String>();

		return "fetchCFDA";
	}
	
	@RequestMapping(value = "/parserData", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String HandlerData(HttpServletRequest request,
			HttpServletResponse response) {

		// 读取配置文件
		Map<String, String> webGather = parserRuleXml("conf/drugfuture.xml");
		int batchUpdate = 0;
		String SearchPages = "",ImportFile="";
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
			if (key.equals("SearchPages")) {
				SearchPages = entry.getValue().toString();
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
			if (key.equals("ImportFile")) {
				ImportFile = entry.getValue().toString();
			}
		}
		
		currentURL = SearchPages;
		if (ImportFile.equals("")) {
			fetchData(currentURL, host, 0,"gbk");
		}else {
			System.out.println("从文件内导入URL数据");
			readerURL(ImportFile);
		}
		
		Iterator iterIndex = IndexMap.entrySet().iterator();
		while (iterIndex.hasNext()) {
			Map.Entry entry = (Map.Entry) iterIndex.next();
			Object key = entry.getKey();
			System.out.println("待爬取集合:" + entry.getValue().toString());
			String text=entry.getValue().toString();
			boolean result=VerificationURL("http://www.18ladys.com/post/+\\w+.html", text);
			if (!result) {
				continue;
			}
			fetchData(text, host, 1,"utf-8");
			if (worker.size()==batchUpdate) {
				cfdaService.save(worker.map,Datatable); 
			}
		}
		if (worker.size()>=1) {
			cfdaService.save(worker.map,Datatable); 
		}
		return "";
	}

	
	public static void fetchData(String url, String host, int type,String characterset) {
		// System.out.println("正在爬取的页面" + url);
		if (url.equals("")) {
			return;
		}
		if (type == 0 || type == 1) {
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
				String response= null;
				if (type == 0) {
					response = new String(responseBody, characterset);
					extractIdexALL(host, response);
				} else if (type == 1) {
					response = new String(responseBody, characterset);
					HandlerSubIndexData(response, url);
				} 
				get.releaseConnection();
			}
		} catch (Exception e) {
			logger.error("这是得到页面出错了{}", url);
		}
	}

	private static void extractIdexALL(String host, String response) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(response);
		Elements values = doc.select("tr>td>a[href]");
		int count = 0;
		for (Element link : values) {
			String linkHref = link.attr("href");
			String linkText = link.text();
			System.out.println(host + linkHref);
			IndexMap.put(count++, host + linkHref);
		}
//		saveURL(IndexMap, "data.txt");
	}

	// 把URL列表保存到文件
	/*public static void saveURL(Map<Integer,String> map, String fileName) {
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
			e.printStackTrace();
		}
	}*/

	// 先得到这个页面的相关属性

	// 共 1 页 当前第 1 页　

	public static void HandlerSubIndexData(String content, String currentURL) throws Exception {
		//先下载图片
		List<String> listText = new ArrayList<String>();
		StringBuffer sbImage=null;
		//处理图片
		Document Imgdoc = Jsoup.parse(content);
		Elements eleNImage = Imgdoc.select("table[align=left] img[src$=.jpg]");
		if (eleNImage.size()==0) {
			eleNImage=Imgdoc.select("div#single div>p:lt(18) img[src$=.jpg]");
		}
		sbImage=new StringBuffer();
		for (Element eleimg : eleNImage) {
			String imgText = eleimg.attr("src");
			String tempText=imgText;
			
			tempText=tempText.replaceAll("http://", "");
			 String[] str=tempText.split("/");
			sbImage.append(str[2]+";");
			System.out.println("正在下载图片中。。。。。");
			downLoadFile(imgText);
		}
		
		//获得药品名称
		Document Namedoc = Jsoup.parse(content);
		Elements eleName = Namedoc.select("h1[class=wz] a[href]");
		if (eleName.size()==0) {
			eleName = Namedoc.select("div[align=left] h3>a[href]");
		}
		for (Element link : eleName) {
			String linkText = link.text();
			System.out.println("获得药品名称"+linkText);
			listText.add(linkText);
		}
		//截取药品内容
		Document doc = Jsoup.parse(content);
		int index = 0;
		Elements values = doc.select("table[align=left] tr:lt(1)");
		if (values.size()==0) {
			values = doc.select("div#single div>p:lt(18)");
			String linkText="";
			for (Element link : values) {
				linkText+= link.text();
			}
			listText.add(linkText);
		}else if (values.size()>=1) {
			for (Element link : values) {
				String linkText = link.text();
				listText.add(linkText);
			}
		}
		listText.add(sbImage.toString());
		listText.add(currentURL);
		System.out.println("正确："+listText.size());
		if (listText.size()==4) {
			System.out.println("合法数据入库");
			saveContent(listText);
		}
	}
	
	// 映射到pojo
	private static void saveContent(List<String> list) {
		Object obj = generateBean.newInstance("conf/drugfuture.xml");
		Map<Integer, String> methodMap = generateBean
				.methodname("conf/drugfuture.xml");
		try {
			Object data=ReflectUtil.toEntityList(obj, list, methodMap);
			worker.writeData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("当前爬取容器的数量**************************:"
				+ worker.size());
	}
	
	public static void  downLoadFile(String downurl) throws MalformedURLException
	 {
//		String text="http://www.18ladys.com/tupian/xianmao2.jpg";
		String Tempdownurl=downurl;
				Tempdownurl=Tempdownurl.replaceAll("http://", "");
		 String[] str=Tempdownurl.split("/");
/*		 for (int i = 0; i < str.length; i++) {
			System.out.println(str[i]);
		} */
		
		int bytesum = 0;
	        int byteread = 0;
	        URL url = new URL(downurl);
	        try {
	            URLConnection conn = url.openConnection();
	            InputStream inStream = conn.getInputStream();
//	            String imgName=downurl.substring(downurl.indexOf("/structure")+11);
	          File file=new File("./Resources/Image/"+str[2]);
	            FileOutputStream fs = new FileOutputStream(file);
	            byte[] buffer = new byte[1024*5];
	            int length;
	            while ((byteread = inStream.read(buffer)) != -1) {
	                bytesum += byteread;
	                fs.write(buffer, 0, byteread);
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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
					.hasNext();) {
				org.dom4j.Element employee = (org.dom4j.Element) i.next();
				confValue.put(employee.getName(), employee.getText());
			}
		} catch (DocumentException e) {
			logger.error("解析规则文件出错", e);
		}
		return confValue;
	}
	public static boolean VerificationURL(String Regex,String url)
	{
		 Pattern p = Pattern.compile(Regex,Pattern.CASE_INSENSITIVE);//匹配<title>开头，</title>结尾的文档
	        Matcher m = p.matcher(url);//开始编译
	        //System.out.println(m.find());
	        boolean result=m.find();
		return result;
	}
	
	// 从文件里面读取要抓取的内容
		public static void readerURL(String fileName)
		{
			File file = new File(fileName);
			FileReader fr;
			try
			{
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String urlText;
				int count = 0;
				while ((urlText = br.readLine()) != null)
				{
					IndexMap.put(count++, urlText);
				}
				br.close();
				fr.close();
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}

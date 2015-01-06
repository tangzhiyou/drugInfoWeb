package com.skysoft.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skysoft.framework.NetWorkHandlerData;
import com.skysoft.util.FileIOStreamTools;

import java.util.List;

/**
 * Created by tangzy on 2015/1/5.
 */
public class TestBaiduMap {
    public static  void main(String[] args)
    {
        List<String> urlList=FileIOStreamTools.readerDataList("C:\\Users\\tangzy\\Desktop\\input.txt",null);
        System.out.println("----------总计多少页爬取--------"+urlList.size());
        int count=1;
        for(String url:urlList)
        {

            if(url==null||"".equals(url))
            {
                continue;
            }
            System.out.println("--------------正在爬取----------------------"+count++);
            String content=NetWorkHandlerData.fetchNetWorkData(url,"UTF-8");
            JSONObject jsonContent=null;
            try {
                jsonContent=JSONObject.parseObject(content);
            }catch (Exception e)
            {
                System.out.println("无法解析的格式");
                continue;
            }
            if (!jsonContent.containsKey("content"))
            {
                continue;
            }
            JSONArray contentArray=jsonContent.getJSONArray("content");
            for(int i=0;i<contentArray.size();i++)
            {
                JSONObject json= (JSONObject) contentArray.get(i);
                System.out.println(json.getString("addr")+"\t:"+json.getString("name"));
                FileIOStreamTools.writeDtatFile(json.getString("name")+"\t"+json.getString("addr"),"C:\\Users\\tangzy\\Desktop\\out.txt");
            }
            try {
                System.out.println("\n休息一会时间再访问");
                Thread.sleep(3000*5);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

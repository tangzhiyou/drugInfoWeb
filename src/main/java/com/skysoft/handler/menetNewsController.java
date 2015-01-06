package com.skysoft.handler;

import com.skysoft.framework.*;
import com.skysoft.service.CFDAService;
import com.skysoft.util.ReflectUtil;
import com.skysoft.util.generateBean;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: pinaster
 * Date: 14-1-4
 * Time: 下午12:19
 */
@Controller
@RequestMapping("/menet")
public class menetNewsController {
    private static Environment myDbEnvironment; // 数据库环境,无需序列化
    private static DatabaseConfig dbConfig;
    private static Database myDatabase; // 数据库,用于保存值,使得支持队列持久化,无需序列化
//    private transient StoredMap<Long, E> queueMap; // 持久化Map,Key为指针位置,Value为值,无需序列化
    private final  static String dbName="fetchmenet"; // 数据库名字
    private final  static String dbDir="Resources/news"; // 数据库所在目录
    private static fetchDatabase worker;
    private  static  menetNewsController menetNews;
    public static List<String> links = null;
    @Autowired
    private CFDAService cfdaService;

    @RequestMapping("fetchCFDA")
    public String initialize() {
        worker = new fetchDatabase();
        worker.createAndBindDatabase("Resources/BDBdatas/fetchData");
        menetNews=new menetNewsController();
        links=new ArrayList<String>();
        return "fetchCFDA";
    }

    public static void main(String[] args) {
        String currentURL="http://www.menet.com.cn/List.aspx?classid=26";
        String context= NetWorkHandlerData.fetchNetWorkData(currentURL, "utf-8");
        context=HtmlParserTool.extractHtmlLabel(context,"body");
//        extractText(context,currentURL);
        menetNews.openDatabase();
        extractText(context,"");
//        System.out.println(menetController.readFromDatabase("http://www.menet.com.cn/Articles/information/201312/201312300841404140_107676.shtml"));
    }


    @RequestMapping(value = "/parserData", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String HandlerData(HttpServletRequest request,
                              HttpServletResponse response) {
        String currentURL="http://www.menet.com.cn/List.aspx?classcode=1600";
        String context= NetWorkHandlerData.fetchNetWorkData(currentURL, "utf-8");
        context=HtmlParserTool.extractHtmlLabel(context, "div[class=newsLeftContent]");
        menetNews.openDatabase();
        cacheColumns(context,"dl>dt>a");

        for (String link : links) {
            String Content = NetWorkHandlerData.fetchNetWorkData(link, "gb2312");
            extractText(Content, link);
            if (worker.size() == 1) {
                cfdaService.save(worker.map, "t_drugnewsinfo");
            }
        }
        return "successful";
    }

    public static void extractText(String context, String contexturl) {

        String text= HtmlParserTool.extractHtmlLabel(context, "body");
        int count=0;
        List<Object> listText=new ArrayList<Object>();
        String data=HtmlParserTool.extractHtmlText(text,"h1");
        listText.add(count++,data);
        listText.add(count++,"无");
        listText.add(count++,"2014-01-06");
        listText.add(count++,"米内网");
        data=HtmlParserTool.extractHtmlText(text,"div[class=newsKeyword]");
        listText.add(count++,data);
        listText.add(count++,new Date()) ;
        listText.add(count++,contexturl);
        listText.add(count++,"政策解读");
        data=HtmlParserTool.extractHtmlText(text,"div#sidebar");
        listText.add(count++,data);
        listText.add(count++, 0);
        saveContent(listText);

    }

    public static  void   cacheColumns(String content, String filterLable) {
        System.out.println(content);
        // 提取URL地址 标题
        List<String> urls=new ArrayList<String>();
        Document doc = Jsoup.parse(content);
        StringBuffer sbHtmlText = null;
        Elements values = doc.select("dl>dt>a");
        sbHtmlText = new StringBuffer();
        for (Element link : values) {
            String HtmlText = link.text();
            urls.add(HtmlText);
            sbHtmlText.append(HtmlText);
        }
        List<String> atrtitles=new ArrayList<String>();
        Elements titles = doc.select("dl>dt>a");
        for (Element text : titles) {
            String HtmlText = text.attr("href");
            atrtitles.add("http://www.menet.com.cn"+HtmlText);
            sbHtmlText.append(HtmlText);
        }
        for (int i=0;i<atrtitles.size();i++)
        {
            String tempText=readFromDatabase(String.format("%s", atrtitles.get(i)));
            if (StringUtils.isBlank(tempText))
            {
                writeToDatabase(String.format("%s",atrtitles.get(i)),String.format("%s",urls.get(i)),true);
                links.add(String.format("%s",atrtitles.get(i)));
            }else
            {
                System.out.println("该地址新闻已经被爬取过了"+tempText);
            }

        }


        //暂时没有处理缓存比较多的时候，要注意及时清理

        //查找在数据库里面，如果没有就添加到爬取队列里面 ，

        // 清空之前的数据库里面的数据
        // 重新写入数据

    }

    // 映射到pojo
    private static void saveContent(List<Object> listText) {
        Object data=null;
        try {
            data = ReflectUtil.ReflectToEntity(ReflectUtil.packageName("conf/DefaultRule.xml"),ReflectUtil.methodList("conf/DefaultRule.xml"),listText);
            worker.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("当前爬取容器的数量**************************:"
                + worker.size());
    }
    /*
     * 向数据库中写入记录
     * 传入key和value
     */
    public static boolean writeToDatabase(String key,String value,boolean isOverwrite) {
        try {
            //设置key/value,注意DatabaseEntry内使用的是bytes数组
            DatabaseEntry theKey=new DatabaseEntry(key.replaceAll("\\s*","").getBytes("UTF-8"));
            DatabaseEntry theData=new DatabaseEntry(value.getBytes("UTF-8"));
            OperationStatus res = null;
            Transaction txn = null;
            try
            {
                TransactionConfig txConfig = new TransactionConfig();
                txConfig.setSerializableIsolation(true);
                txn = myDbEnvironment.beginTransaction(null, txConfig);
                if(isOverwrite)
                {
                    res = myDatabase.put(txn, theKey, theData);
                }
                else
                {
                    res = myDatabase.putNoOverwrite(txn, theKey, theData);
                }

                txn.commit();

                if(res == OperationStatus.SUCCESS)
                {
//                    CheckMethods.PrintDebugMessage("向数据库" + dbName +"中写入:"+key+","+value);
                    return true;
                }
                else if(res == OperationStatus.KEYEXIST)
                {
//                    CheckMethods.PrintDebugMessage("向数据库" + dbName +"中写入:"+key+","+value+"失败,该值已经存在");
                    return false;
                }
                else
                {
//                    CheckMethods.PrintDebugMessage("向数据库" + dbName +"中写入:"+key+","+value+"失败");
                    return false;
                }

            }
            catch(LockConflictException lockConflict)
            {
                txn.abort();
                lockConflict.printStackTrace();

//                CheckMethods.PrintInfoMessage("向数据库" + dbName +"中写入:"+key+","+value+"出现lock异常");
//                CheckMethods.PrintInfoMessage(lockConflict.getMessage());
//                CheckMethods.PrintInfoMessage(lockConflict.getCause().toString());
//                CheckMethods.PrintInfoMessage(lockConflict.getStackTrace().toString());
                return false;
            }
        }
        catch (Exception e)
        {
            // 错误处理
//            CheckMethods.PrintInfoMessage("向数据库" + dbName +"中写入:"+key+","+value+"出现错误");
             e.printStackTrace();
            return false;
        }
    }
    /*
     * 打开当前数据库
     */
    public void openDatabase() {
        try{
//            CheckMethods.PrintDebugMessage("打开数据库: "+dbName);
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            envConfig.setTransactional(true);
            envConfig.setReadOnly(false);
            envConfig.setTxnTimeout(10000, TimeUnit.MILLISECONDS);
            envConfig.setLockTimeout(10000, TimeUnit.MILLISECONDS);
            /*
             *   其他配置 可以进行更改
                EnvironmentMutableConfig envMutableConfig = new EnvironmentMutableConfig();
                envMutableConfig.setCachePercent(50);//设置je的cache占用jvm 内存的百分比。
                envMutableConfig.setCacheSize(123456);//设定缓存的大小为123456Bytes
                envMutableConfig.setTxnNoSync(true);//设定事务提交时是否写更改的数据到磁盘，true不写磁盘。
                //envMutableConfig.setTxnWriteNoSync(false);//设定事务在提交时，是否写缓冲的log到磁盘。如果写磁盘会影响性能，不写会影响事务的安全。随机应变。
             *
             */
            File file = new File(dbDir);
            if(!file.exists())
                file.mkdirs();
            myDbEnvironment = new Environment(file,envConfig);

            dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(true);
            dbConfig.setReadOnly(false);
            //dbConfig.setSortedDuplicates(false);
            /*
                setBtreeComparator 设置用于B tree比较的比较器，通常是用来排序
                setDuplicateComparator 设置用来比较一个key有两个不同值的时候的大小比较器。
                setSortedDuplicates 设置一个key是否允许存储多个值，true代表允许，默认false.
                setExclusiveCreate 以独占的方式打开，也就是说同一个时间只能有一实例打开这个database。
                setReadOnly 以只读方式打开database,默认是false.
                setTransactional 如果设置为true,则支持事务处理，默认是false，不支持事务。
            */
            if(myDatabase == null)
                myDatabase = myDbEnvironment.openDatabase(null, dbName, dbConfig);

//            CheckMethods.PrintDebugMessage(dbName+"数据库中的数据个数: "+myDatabase.count());
            /*
             *  Database.getDatabaseName()
                取得数据库的名称
                如：String dbName = myDatabase.getDatabaseName();

                Database.getEnvironment()
                取得包含这个database的环境信息
                如：Environment theEnv = myDatabase.getEnvironment();

                Database.preload()
                预先加载指定bytes的数据到RAM中。
                如：myDatabase.preload(1048576l); // 1024*1024

                Environment.getDatabaseNames()
                返回当前环境下的数据库列表
                Environment.removeDatabase()
                删除当前环境中指定的数据库。
                如：
                String dbName = myDatabase.getDatabaseName();
                myDatabase.close();
                myDbEnv.removeDatabase(null, dbName);

                Environment.renameDatabase()
                给当前环境下的数据库改名
                如：
                String oldName = myDatabase.getDatabaseName();
                String newName = new String(oldName + ".new", "UTF-8");
                myDatabase.close();
                myDbEnv.renameDatabase(null, oldName, newName);

                Environment.truncateDatabase()
                清空database内的所有数据，返回清空了多少条记录。
                如：
                Int numDiscarded= myEnv.truncate(null,
                myDatabase.getDatabaseName(),true);
                CheckMethods.PrintDebugMessage("一共删除了 " + numDiscarded +" 条记录 从数据库 " + myDatabase.getDatabaseName());
             */
        }
        catch(DatabaseException e){
            e.printStackTrace();
//            CheckMethods.PrintInfoMessage(e.getMessage());

        }
    }

    /*
     * 关闭当前数据库
     */
    public  void closeDatabase() {
        try {
            if(myDatabase != null)
            {
//                myDatabase.sync();

            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    /*
     * 从数据库中读出数据
     * 传入key 返回value
     */
    public static  String readFromDatabase(String key) {
        //Database.getSearchBoth()
        try {
            // 把字符串转化成byte[]
            DatabaseEntry theKey = new DatabaseEntry(StringHelper.trimAll(key).getBytes("UTF-8"));
            DatabaseEntry theData = new DatabaseEntry();
            Transaction txn = null;
            try
            {
                TransactionConfig txConfig = new TransactionConfig();
                txConfig.setSerializableIsolation(true);
                txn = myDbEnvironment.beginTransaction(null, txConfig);
                OperationStatus res = myDatabase.get(txn, theKey, theData, LockMode.DEFAULT);
                txn.commit();
                if(res == OperationStatus.SUCCESS)
                {
                    byte[] retData = theData.getData();
                    String foundData = new String(retData, "UTF-8");
//                    CheckMethods.PrintDebugMessage("从数据库" + dbName +"中读取:"+key+","+foundData);
                    return foundData;
                }
                else
                {
//                    CheckMethods.PrintDebugMessage("No record found for key '" + key + "'.");
                    return "";
                }
            }
            catch(LockConflictException lockConflict)
            {
                txn.abort();
                lockConflict.printStackTrace();
//                CheckMethods.PrintInfoMessage("从数据库" + dbName +"中读取:"+key+"出现lock异常");
//                CheckMethods.PrintInfoMessage(lockConflict.getMessage());
//                CheckMethods.PrintInfoMessage(lockConflict.getCause().toString());
//                CheckMethods.PrintInfoMessage(lockConflict.getStackTrace().toString());
                return "";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return "";
        }
    }

    /*
     * 删除数据库中的一条记录
     */
    public  boolean deleteFromDatabase(String key) {
        boolean success = false;
        long sleepMillis = 0;
        for(int i=0;i<3;i++)
        {
            if (sleepMillis != 0)
            {
                try
                {
                    Thread.sleep(sleepMillis);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                sleepMillis = 0;
            }
            Transaction txn = null;
            try
            {
                TransactionConfig txConfig = new TransactionConfig();
                txConfig.setSerializableIsolation(true);
                txn = myDbEnvironment.beginTransaction(null, txConfig);
                DatabaseEntry theKey;
                theKey = new DatabaseEntry(StringHelper.trimAll(key).getBytes("UTF-8"));
                OperationStatus res = myDatabase.delete(txn, theKey);
                txn.commit();
                if(res == OperationStatus.SUCCESS)
                {
//                    CheckMethods.PrintDebugMessage("从数据库" + dbName +"中删除:"+key);
                    success = true;
                    return success;
                }
                else if(res == OperationStatus.KEYEMPTY)
                {
//                    CheckMethods.PrintDebugMessage("没有从数据库" + dbName +"中找到:"+key+"。无法删除");
                }
                else
                {
//                    CheckMethods.PrintDebugMessage("删除操作失败，由于"+res.toString());
                }
                return false;
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                return false;
            }
            catch(LockConflictException lockConflict)
            {
                   lockConflict.printStackTrace();
//                CheckMethods.PrintInfoMessage("删除操作失败，出现lockConflict异常");
//                CheckMethods.PrintInfoMessage(lockConflict.getMessage());
//                CheckMethods.PrintInfoMessage(lockConflict.getCause().toString());
//                CheckMethods.PrintInfoMessage(lockConflict.getStackTrace().toString());
                sleepMillis = 1000;

                continue;
            }
            finally
            {
                if (!success)
                {
                    if (txn != null)
                    {
                        txn.abort();
                    }
                }
            }
        }
        return false;
    }
    /*
     * 遍历数据库中的所有记录，返回list
     */
    public  ArrayList<String> getEveryItem() {
//        CheckMethods.PrintDebugMessage("===========遍历数据库"+dbName+"中的所有数据==========");
        Cursor myCursor = null;
        ArrayList<String> resultList = new ArrayList<String>();
        Transaction txn = null;
        try{
            txn = this.myDbEnvironment.beginTransaction(null, null);
            CursorConfig cc = new CursorConfig();
            cc.setReadCommitted(true);
            if(myCursor==null)
                myCursor = myDatabase.openCursor(txn, cc);
            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();
            // 使用cursor.getPrev方法来遍历游标获取数据
            if(myCursor.getFirst(foundKey, foundData, LockMode.DEFAULT)
                    == OperationStatus.SUCCESS)
            {
                String theKey = new String(foundKey.getData(), "UTF-8");
                String theData = new String(foundData.getData(), "UTF-8");
                resultList.add(theKey);
//                CheckMethods.PrintDebugMessage("Key | Data : " + theKey + " | " + theData + "");
                while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT)
                        == OperationStatus.SUCCESS)
                {
                    theKey = new String(foundKey.getData(), "UTF-8");
                    theData = new String(foundData.getData(), "UTF-8");
                    resultList.add(theKey);
//                    CheckMethods.PrintDebugMessage("Key | Data : " + theKey + " | " + theData + "");
                }
            }
            myCursor.close();
            txn.commit();
            return resultList;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
//            CheckMethods.PrintInfoMessage("getEveryItem处理出现异常");
//            CheckMethods.PrintInfoMessage(e.getMessage().toString());
//            CheckMethods.PrintInfoMessage(e.getCause().toString());
            txn.abort();
            if (myCursor != null)
            {
                myCursor.close();
            }
            return null;
        }
    }
}

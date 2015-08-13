package com.skysoft.framework;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Created by tangzy on 2015/8/14.
 */
public class fetchDatabaseTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateAndBindDatabase() throws Exception {
        Queue<String> persistentQueue=null;
        fetchDatabase worker=null;
        // 创建url数据库文件
        String urlDir = "Resources/BDBdatas/url";
        File urlFile = new File(urlDir);
        if (!urlFile.exists() || !urlFile.isDirectory()) {
            urlFile.mkdirs();
        }
        String fetchDataDir = "Resources/BDBdatas/fetchData";
        File fetchDataFile = new File(fetchDataDir);
        if (!fetchDataFile.exists() || !fetchDataFile.isDirectory()) {
            fetchDataFile.mkdirs();
        }
        // 创建爬取数据库文件
        persistentQueue = new BdbPersistentQueue(urlDir, "fetchData",
                String.class);

        worker = new fetchDatabase();
        worker.createAndBindDatabase("Resources/BDBdatas/fetchData");
        worker.writeData("aa");
        System.out.println(String.valueOf(worker.map.get(0)));
    }
}
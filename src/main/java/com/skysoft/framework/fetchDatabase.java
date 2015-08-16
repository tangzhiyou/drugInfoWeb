package com.skysoft.framework;

import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

public class FetchDatabase {


//    private Environment env;
//    private ClassCatalog catalog;
//    private Database db;
    public static TreeMap<String, Object> map=new TreeMap<String, Object>();


//    public void createAndBindDatabase(String dbDir)
//            throws
//            DatabaseException, IllegalArgumentException {
//        File envFile = null;
//        EnvironmentConfig envConfig = null;
//        DatabaseConfig dbConfig = null;
//        Environment env = null;
//        try {
//            // 数据库位置
//            envFile = new File(dbDir);
//
//            // 数据库环境配置
//            envConfig = new EnvironmentConfig();
//            envConfig.setAllowCreate(true);
//            envConfig.setTransactional(false);
//
//            // 数据库配置
//            dbConfig = new DatabaseConfig();
//            dbConfig.setAllowCreate(true);
//            dbConfig.setTransactional(false);
//            dbConfig.setDeferredWrite(true);
//
//            // 创建环境
//
//            env = new Environment(envFile, envConfig);
//
//            // catalog is needed for serial bindings (java serialization)
//            Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
//            catalog = new StoredClassCatalog(catalogDb);
//
//            // use Integer tuple binding for key entries
//            TupleBinding<Integer> keyBinding = TupleBinding
//                    .getPrimitiveBinding(Integer.class);
//
//            // use String serial binding for data entries
//            SerialBinding<Object> dataBinding = new SerialBinding<Object>(
//                    catalog, Object.class);
//
//            this.db = env.openDatabase(null, "fetchdata", dbConfig);
//
//            // create a map view of the database
//            this.map = new StoredSortedMap<Integer, Object>(db, keyBinding,
//                    dataBinding, true);
//        } catch (DatabaseNotFoundException e) {
//            throw e;
//        } catch (DatabaseExistsException e) {
//            throw e;
//        } catch (DatabaseException e) {
//            throw e;
//        } catch (IllegalArgumentException e) {
//            throw e;
//        }
//
//    }

    /**
     * Closes the database.
     */
//    public void close() throws Exception {
//
//        if (catalog != null) {
//            catalog.close();
//            catalog = null;
//        }
//        if (db != null) {
//            db.close();
//            db = null;
//        }
//        if (env != null) {
//            env.close();
//            env = null;
//        }
//    }

    public static void writeData(Object data) {
        int size = size();
        map.put(String.valueOf(size), data);
    }


    public static int size() {
        return map.size();
    }
}

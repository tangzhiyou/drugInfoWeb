package com.skysoft.framework;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseExistsException;
import com.sleepycat.je.DatabaseNotFoundException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
public class fetchDatabase {

	private static final String[] INT_NAMES = { "Hello", "Database", "World", };

	private static boolean create = true;

	private Environment env;
	private ClassCatalog catalog;
	private Database db;
	public static SortedMap<Integer, Object> map;

	/** Creates the environment and runs a transaction */
	public static void main(String[] argv) throws Exception
	{
		fetchDatabase worker = new fetchDatabase();
		worker.createAndBindDatabase("./tmep");
		worker.ReadData();
	}

	public void createAndBindDatabase(String dbDir)
			throws DatabaseNotFoundException, DatabaseExistsException,
			DatabaseException, IllegalArgumentException
	{
		File envFile = null;
		EnvironmentConfig envConfig = null;
		DatabaseConfig dbConfig = null;
		Environment env = null;
		try
		{
			// 数据库位置
			envFile = new File(dbDir);

			// 数据库环境配置
			envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			envConfig.setTransactional(false);

			// 数据库配置
			dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			dbConfig.setTransactional(false);
			dbConfig.setDeferredWrite(true);

			// 创建环境

			env = new Environment(envFile, envConfig);

			// catalog is needed for serial bindings (java serialization)
			Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
			catalog = new StoredClassCatalog(catalogDb);

			// use Integer tuple binding for key entries
			TupleBinding<Integer> keyBinding = TupleBinding
					.getPrimitiveBinding(Integer.class);

			// use String serial binding for data entries
			SerialBinding<Object> dataBinding = new SerialBinding<Object>(
					catalog, Object.class);

			this.db = env.openDatabase(null, "fetchdata", dbConfig);

			// create a map view of the database
			this.map = new StoredSortedMap<Integer, Object>(db, keyBinding,
					dataBinding, true);
		} catch (DatabaseNotFoundException e)
		{
			throw e;
		} catch (DatabaseExistsException e)
		{
			throw e;
		} catch (DatabaseException e)
		{
			throw e;
		} catch (IllegalArgumentException e)
		{
			throw e;
		}

	}

	/** Closes the database. */
	public void close() throws Exception
	{

		if (catalog != null)
		{
			catalog.close();
			catalog = null;
		}
		if (db != null)
		{
			db.close();
			db = null;
		}
		if (env != null)
		{
			env.close();
			env = null;
		}
	}

	public void writeData(Object data)
	{
		int i = size();
		map.put(new Integer(i), data);
	}

	public Object ReadData()
	{
		Object value = null;
		Iterator<Map.Entry<Integer, Object>> iter = map.entrySet().iterator();
		System.out.println("Reading data");
		while (iter.hasNext())
		{
			Map.Entry<Integer, Object> entry = iter.next();
			value = entry.getValue();
		}
		return value;

	}

	public int size()
	{
		return map.size();
	}
}

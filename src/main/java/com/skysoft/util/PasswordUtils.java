package com.skysoft.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class PasswordUtils {
	public static Invocable invoke = null;
	static {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine jsEngine = manager.getEngineByName("javascript");
		// String jsFileName = "resources.qq/password.js"; // 指定md5加密文件
		String jsFileName = "ecma.js"; // 指定md5加密文件
		Reader reader;
		try {
			InputStream in = PasswordUtils.class.getClassLoader()
					.getResourceAsStream(jsFileName);
			reader = new InputStreamReader(in);
			jsEngine.eval(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		invoke = (Invocable) jsEngine;

	}

	public static String getPassEncoding(String pubKey, String serverTime,
										 String nonce,String password) {
		String pass = null;
		try {
			pass = (String) invoke.invokeFunction("getPassEncoding", new Object[] {
					pubKey, serverTime,nonce,password});
		} catch (Exception e) {
			System.out.println("为登陆密码加密时，出现异常!");
			e.printStackTrace();
		}
		return pass;
	}
	//
	public static String getHexString(String hexCode) {
		String pass = null;
		try {
			pass = (String) invoke.invokeFunction("hexchar2bin",
					new Object[] { hexCode });
		} catch (Exception e) {
			System.out.println("将字符串转化为16进制时，出现异常!");
			e.printStackTrace();
		}
		return pass;
	}

	public static void main(String[] args) {

		String pass = null;
		try {
			Object callback = invoke.invokeFunction("callbackC");
			Object curForm = null;
			String url="content.jsp?tableId=91&tableName=TABLE91&tableView=食品生产许可获证企业&Id=667";
			pass = (String) invoke.invokeFunction("commitForECMA", new Object[] {
					callback, url,curForm});
		} catch (Exception e) {
			System.out.println("为登陆密码加密时，出现异常!");
			e.printStackTrace();
		}
		System.out.println(pass);
	}

}

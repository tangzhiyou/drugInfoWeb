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
		// String jsFileName = "resources.qq/password.js"; // ָ��md5�����ļ�
		String jsFileName = "password4Sina.js"; // ָ��md5�����ļ�
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
			System.out.println("Ϊ��½�������ʱ�������쳣!");
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
			System.out.println("���ַ���ת��Ϊ16����ʱ�������쳣!");
			e.printStackTrace();
		}
		return pass;
	}

}

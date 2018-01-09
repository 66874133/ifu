package com.funnel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

	private static Map<String, String> map = new HashMap<String, String>();
	static {
		String path = PropertiesUtil.class.getClassLoader().getResource("")
				.getPath();

		File file = new File(path + "resources/");

		File[] fs = file.listFiles(new FilenameFilter() {

			public boolean accept(File arg0, String arg1) {

				if (arg1.endsWith(".properties")) {
					return true;
				}
				return false;

			}
		});
		for (File f : fs) {
			try {
				loadProperties(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void loadProperties(File f) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(f));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Enumeration<Object> e = prop.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement().toString();
			if (null != prop.getProperty(key))
				map.put(key, prop.getProperty(key));
		}
	}

	public static String getProperty(String key) {
		return map.get(key);
	}

	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getProperty("serverName"));
	}
}

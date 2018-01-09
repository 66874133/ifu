package com.funnel.datasource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class IbatisConfUtil {

	private static Map<String, IbatisSql> sqlIdToIbatisSql = new HashMap<String, IbatisSql>();

	private static SAXBuilder saxBuilder = new SAXBuilder(false);
	static {

		String path = IbatisConfUtil.class.getClassLoader().getResource("")
				.getPath();

		File file = new File(path + "resources/sqlmap");

		File[] fs = file.listFiles();
		for (File f : fs) {
			try {
				loadIbatis(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadIbatis(File file) throws JDOMException, IOException {
		Document doc = saxBuilder.build(file);

		Element root = doc.getRootElement();

		String namespace = root.getAttributeValue("namespace");

		List<Element> list = root.getChildren();
		for (Element e : list) {
			IbatisSql ibatisSql = new IbatisSql();
			String id = e.getAttributeValue("id");
			String resultClass = e.getAttributeValue("resultClass");
			String parameterClass = e.getAttributeValue("parameterClass");
			String sql = e.getTextTrim();

			ibatisSql.setId(namespace + "." + id);
			ibatisSql.setParameterClass(parameterClass);
			ibatisSql.setResultClass(resultClass);
			ibatisSql.setSql(sql);

			sqlIdToIbatisSql.put(namespace + "." + id, ibatisSql);
		}
	}

	public static void loadIbatis(String filePath) throws JDOMException,
			IOException {

		loadIbatis(new File(filePath));

	}

	public static IbatisSql getIbatisSql(String sqlId) {
		return sqlIdToIbatisSql.get(sqlId);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getIbatisSql("cache.querySysParams"));

	}

}

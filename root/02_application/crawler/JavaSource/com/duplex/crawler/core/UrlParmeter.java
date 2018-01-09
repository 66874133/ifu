package com.duplex.crawler.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.funnel.svc.util.StringUtils;

/**
 * 一条连接抓取后获得 的各项内容
 * 
 * @author Jerry
 * 
 */
public class UrlParmeter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5799595924547418177L;
	private HashMap<String, String[]> map;

	public UrlParmeter() {
		this.map = new HashMap<String, String[]>();
	}

	public void replaceParameter(String name, String[] values) {
		map.put(name, values);
	}

	public void addParameter(String name, String[] values) {

		List<String> list = new ArrayList<String>();
		if (map.containsKey(name)) {
			for (String s : map.get(name)) {
				list.add(s);
			}
		}

		for (String v : values) {
			list.add(v);
		}

		map.put(name, list.toArray(new String[] {}));
	}

	public void remove(String param) {
		this.map.remove(param);
	}

	public void addParameter(String name, String value) {
		List<String> list = new ArrayList<String>();
		if (map.containsKey(name)) {
			for (String s : map.get(name)) {
				list.add(s);
			}
		}

		list.add(value);

		map.put(name, list.toArray(new String[] {}));
	}

	public String[] getParameters() {
		return map.keySet().toArray(new String[] {});
	}

	public boolean containsParameter(String name) {
		return map.containsKey(name);
	}

	public String[] getValues(String name) {
		if (map.containsKey(name)) {
			return map.get(name);
		} else {
			return new String[] {};
		}
	}

	public String toString() {
		List<String> list = new ArrayList<String>();
		for (String key : map.keySet()) {
			list.add(key + "="
					+ StringUtils.getStringFromStrings(map.get(key), ","));
		}

		return StringUtils.getStringFromStrings(list, "|");
	}
}

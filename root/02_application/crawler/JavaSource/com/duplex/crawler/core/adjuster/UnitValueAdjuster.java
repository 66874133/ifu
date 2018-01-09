package com.duplex.crawler.core.adjuster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.duplex.crawler.core.CrawlerObject;

public class UnitValueAdjuster implements IValueAdjuster {

	private static Map<String, Long> map = new HashMap<String, Long>();

	static {
		map.put("万", 10000L);
	}

	public String adjust(String content, CrawlerObject crawlerObject) {

		Iterator<String> iterator = map.keySet().iterator();

		while (iterator.hasNext()) {
			String unit = iterator.next();
			if (content.contains(unit)) {
				content = content.substring(0, content.indexOf(unit));
				double s = Double.parseDouble(content);
				long value = (long) (s * map.get(unit));
				return String.valueOf(value);
			}

		}

		return content;
	}

	public static void main(String[] args) {
		System.out.println(new UnitValueAdjuster().adjust("1.41212121万笔", null));
	}
}

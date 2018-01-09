package com.duplex.crawler.core.adjuster;

import java.util.HashMap;

import com.duplex.crawler.core.CrawlerObject;
import com.funnel.svc.util.StringUtils;

public class ValueSplitAdjuster implements IValueAdjuster {
	private String spliter;
	private String merger;
	private String temple;

	public String adjust(String value,CrawlerObject crawlerObject) {
		String[] ss = StringUtils.split(value, spliter);

		for (int i = 0; i < ss.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("value", ss[i].trim());
			ss[i] = StringUtils.replace(temple, map);
		}

		return StringUtils.getStringFromStrings(ss, merger);
	}

}

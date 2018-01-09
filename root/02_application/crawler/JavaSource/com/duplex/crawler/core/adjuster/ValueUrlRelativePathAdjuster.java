package com.duplex.crawler.core.adjuster;

import com.duplex.crawler.core.CrawlerObject;
import com.funnel.svc.util.UrlUtil;

public class ValueUrlRelativePathAdjuster implements IValueAdjuster {

	public String adjust(String url, CrawlerObject crawlerObject) {

		if (UrlUtil.isRelativeURL(url)) {
			try {
				return UrlUtil.getAbsoluteURL(url, crawlerObject.getUrl());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return url;
	}

}

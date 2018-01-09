package com.duplex.crawler.core.adjuster;

import com.duplex.crawler.core.CrawlerObject;
import com.funnel.svc.util.UrlUtil;

public class ValueUrlProtocolAdjuster implements IValueAdjuster {

	public String adjust(String url,CrawlerObject crawlerObject) {
		
		String protocol = UrlUtil.getUrlProtocol(crawlerObject.getUrl());
		
		return protocol+":"+url;
	}

	

	

}

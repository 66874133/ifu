package com.duplex.crawler.core.builder;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;

public class UrlLinkPatternBuilder implements IUrlBuilder {

	private String pattern;

	public String[] biuldUrl(CrawlerObject base) {

		String url = base.getUrl();

		String[] values = RegularExpressionUtil.getPatternValue(url, pattern);

		return values;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "UrlLinkPatternBuilder [pattern=" + pattern + "]";
	}

}

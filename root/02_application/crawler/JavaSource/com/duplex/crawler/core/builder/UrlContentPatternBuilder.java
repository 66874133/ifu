package com.duplex.crawler.core.builder;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;

public class UrlContentPatternBuilder implements IUrlBuilder {

	private String pattern;

	public String[] biuldUrl(CrawlerObject base) {

		String text = base.getContent();
		String[] result = RegularExpressionUtil.getPatternValue(text, pattern);

		return result;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "UrlContentPatternBuilder [pattern=" + pattern + "]";
	}

}

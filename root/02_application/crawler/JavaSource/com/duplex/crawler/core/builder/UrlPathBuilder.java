package com.duplex.crawler.core.builder;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;

public class UrlPathBuilder implements IUrlBuilder {

	private String path;
	private String pattern;
	private boolean order;

	public String[] biuldUrl(CrawlerObject base) {
		HTMLPath parser = new HTMLPath(base.getContent());

		if (order) {
			return new String[] { String.valueOf(HTMLUtil.getNodeOrder(parser,
					path)) };
		} else {
			String[] result = HTMLUtil.getNodeValues(parser, path, pattern);

			return result;
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + path + " " + pattern;
	}

}

package com.duplex.crawler.core.builder;

import com.duplex.crawler.core.CrawlerObject;

public class UrlFieldBuilder implements IUrlBuilder {
	private String field;


	public String[] biuldUrl(CrawlerObject base) {
		String[] result = new String[] {};
		if (base.getParameter().containsParameter(field)) {
			result = base.getParameter().getValues(field);
		}

		// TulipLogs.getNamedLogger(base, "extractor").info(
		// result.length + " urls is built at " + this.toString()
		// + " base on url '" + base.getUrl() + "'");

		return result;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + field;
	}

}

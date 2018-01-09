package com.duplex.crawler.core.adjuster;

import com.duplex.crawler.core.CrawlerObject;

public class TemplateCombineAdjuster implements IValueAdjuster {

	private String template;

	public String adjust(String value, CrawlerObject crawlerObject) {

		String str = template.replace("$1", value);

		return str;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}

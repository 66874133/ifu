package com.duplex.crawler.core.writer;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;



public class StaticWriterField extends AbstractWriterField {

	private String value;

	@Override
	public String[] getAllValues(CrawlerObject task, HTMLPath parser) {

		return new String[] { value };
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}

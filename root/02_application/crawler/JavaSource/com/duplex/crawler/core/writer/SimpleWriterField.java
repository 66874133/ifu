package com.duplex.crawler.core.writer;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;

public class SimpleWriterField extends AbstractWriterField {
	private String path;
	private String pattern;

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

	@Override
	public String[] getAllValues(CrawlerObject task, HTMLPath parser) {
		return HTMLUtil.getNodeValues(parser, path, pattern);
	}

}

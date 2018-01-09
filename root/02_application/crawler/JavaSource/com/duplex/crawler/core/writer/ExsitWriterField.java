package com.duplex.crawler.core.writer;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;
import com.funnel.svc.util.StringUtils;

public class ExsitWriterField extends AbstractWriterField {
	private String path;
	private String pattern;
	private String existValue;
	private String notExistValue;

	@Override
	public String[] getAllValues(CrawlerObject executeObject, HTMLPath parser) {
		CrawlerObject ut = (CrawlerObject) executeObject;
		if (!StringUtils.isNullOrEmpty(path)) {
			String[] ss = HTMLUtil.getNodeValues(parser, path);
			if (ss.length > 0) {
				return new String[] { existValue };
			} else {
				return new String[] { notExistValue };
			}
		} else {
			String[] values = RegularExpressionUtil.getPatternValue(
					ut.getContent(), pattern);
			if (values.length > 0) {
				return new String[] { existValue };
			} else {
				return new String[] { notExistValue };
			}
		}

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExistValue() {
		return existValue;
	}

	public void setExistValue(String exsitValue) {
		this.existValue = exsitValue;
	}

	public String getNotExistValue() {
		return notExistValue;
	}

	public void setNotExistValue(String notExistValue) {
		this.notExistValue = notExistValue;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}

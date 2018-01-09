package com.duplex.crawler.core.writer;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;
import com.funnel.svc.util.StringUtils;

public class UrlWriterField extends AbstractWriterField {
	private String pattern;

	@Override
	public String[] getAllValues(CrawlerObject executeObject, HTMLPath parser) {
		CrawlerObject ut = (CrawlerObject) executeObject;
		if (StringUtils.isNullOrEmpty(pattern)) {
			return new String[] { ut.getUrl() };
		} else {
			return RegularExpressionUtil.getPatternValue(ut.getUrl(), pattern);
		}
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}

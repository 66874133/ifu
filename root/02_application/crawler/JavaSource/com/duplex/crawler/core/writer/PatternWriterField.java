package com.duplex.crawler.core.writer;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;

public class PatternWriterField extends AbstractWriterField {
	private String pattern;

	@Override
	public String[] getAllValues(CrawlerObject executeObject, HTMLPath parser) {

		CrawlerObject ut = (CrawlerObject) executeObject;
		String content = ut.getContent();

		String[] values = RegularExpressionUtil.getPatternValue(content,
				pattern);

		return values;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public static void main(String[] args) {
		String[] values = RegularExpressionUtil.getPatternValue("raw_title:abc,raw_title:abc2,raw_title:abc2,",
				"raw_title:(.*?),");

		
	}

}

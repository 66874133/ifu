package com.duplex.crawler.core.writer;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;

public class UrlPatternWriterField extends AbstractWriterField {
	private String pattern;

	@Override
	public String[] getAllValues(CrawlerObject executeObject, HTMLPath parser) {

		CrawlerObject ut = (CrawlerObject) executeObject;

		String[] values = RegularExpressionUtil.getPatternValue(ut.getUrl(),
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
		
		System.out.println(RegularExpressionUtil.getPatternValue("https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.7.4kbTps?id=527564998613&skuId=3148491",
				"[&\\?]id=(\\d*)&")[0]);
	}
}

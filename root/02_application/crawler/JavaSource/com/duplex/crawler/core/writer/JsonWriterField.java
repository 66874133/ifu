package com.duplex.crawler.core.writer;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;
import com.funnel.svc.util.StringUtils;



public class JsonWriterField extends AbstractWriterField {
	private String path;
	private String parameter;

	@Override
	public String[] getAllValues(CrawlerObject executeObject, HTMLPath parser) {
		CrawlerObject ut = (CrawlerObject) executeObject;
		
		String json = ut.getContent();
		if (!StringUtils.isNullOrEmpty(path)) {
			json = HTMLUtil.getNodeHTML(parser, path);
		}

		String[] values = RegularExpressionUtil.getPatternValue(json, "\""
				+ parameter + "\":['\"](.*?)['\"]");

		return values;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}

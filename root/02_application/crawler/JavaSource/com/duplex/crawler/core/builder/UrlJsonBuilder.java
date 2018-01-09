package com.duplex.crawler.core.builder;

import java.util.ArrayList;
import java.util.List;

import com.discover.crawler.util.JsonUtil;
import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;
import com.funnel.svc.util.StringUtils;

public class UrlJsonBuilder implements IUrlBuilder {

	private String path;
	private String pattern;

	private String parameter;
	private String matche;


	public String[] biuldUrl(CrawlerObject base) {
		String text = base.getContent();

		if (!StringUtils.isNullOrEmpty(path)) {
			HTMLPath parser = new HTMLPath(base.getContent());
			List<String> list = new ArrayList<String>();
			String[] result = HTMLUtil.getNodeValues(parser, path, pattern);

			for (int i = 0; i < result.length; i++) {
				try {
					list.addAll(getJsonValues(result[i]));
				} catch (Exception e) {
					continue;
				}
			}
			return list.toArray(new String[0]);
		}

		else if (StringUtils.isNullOrEmpty(path)
				&& !StringUtils.isNullOrEmpty(pattern)) {
			List<String> list = new ArrayList<String>();
			String[] result = RegularExpressionUtil.getPatternValue(text,
					pattern);
			for (int i = 0; i < result.length; i++) {
				try {
					list.addAll(getJsonValues(result[i]));
				} catch (Exception e) {
					continue;
				}
			}

		}

		return getJsonValues(text).toArray(new String[0]);

	}

	public List<String> getJsonValues(String text) {
		List<String> values = JsonUtil.getValues(parameter, text);

		if (!StringUtils.isNullOrEmpty(matche)) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < values.size(); i++) {

				if (RegularExpressionUtil.matches(values.get(i), matche)) {
					list.add(values.get(i));
				}
			}

			return list;
		}

		return values;
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

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getMatche() {
		return matche;
	}

	public void setMatche(String matche) {
		this.matche = matche;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + path + " " + pattern
				+ " " + parameter + " " + matche;
	}
}

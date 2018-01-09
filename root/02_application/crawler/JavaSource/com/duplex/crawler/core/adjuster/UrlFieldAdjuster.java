package com.duplex.crawler.core.adjuster;

import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.core.CrawlerObject;
import com.funnel.svc.util.StringUtils;

public class UrlFieldAdjuster implements IValueAdjuster {
	private String slashs;
	private String parameters;


	public String adjust(String url,CrawlerObject task) {
		if (url.contains("?")) {
			String[] ss = StringUtils.split(url, "?");
			if (ss.length == 2) {
				String result = "";
				if (!StringUtils.isNullOrEmpty(ss[0])
						&& !StringUtils.isNullOrEmpty(slashs)) {
					int index = url.indexOf("//");
					String temp = ss[0].substring(index + 2);
					String[] sls = StringUtils.split(temp, "/");
					List<String> list = new ArrayList<String>();
					for (int i = 0; i < sls.length; i++) {
						if (contains(slashs, String.valueOf(i))) {
							list.add(sls[i]);
						}
					}

					result = url.substring(0, index + 2)
							+ StringUtils.getStringFromStrings(list, "/") + "?";
				} else {
					result = ss[0].trim() + "?";
				}

				if (!StringUtils.isNullOrEmpty(ss[1])
						&& !StringUtils.isNullOrEmpty(parameters)) {
					String[] pas = StringUtils.split(ss[1].trim(), "&");
					if (pas.length > 0) {
						List<String> list = new ArrayList<String>();
						for (String pa : pas) {
							String key = pa.substring(0, pa.indexOf("="));
							if (contains(parameters, key)) {
								list.add(pa);
							}
						}

						result = result
								+ StringUtils.getStringFromStrings(list, "&");
					}
				} else {
					result = result + ss[1].trim();
				}

				if (result.endsWith("?")) {
					result = result.substring(0, result.length() - 1);
				}

				return result;
			}
		}

		return url;
	}

	private boolean contains(String big, String small) {
		String[] bs = StringUtils.split(big, ",");
		for (String b : bs) {
			if (b.trim().equals(small.trim())) {
				return true;
			}
		}

		return false;
	}

	public String getSlashs() {
		return slashs;
	}

	public void setSlashs(String slashs) {
		this.slashs = slashs;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}

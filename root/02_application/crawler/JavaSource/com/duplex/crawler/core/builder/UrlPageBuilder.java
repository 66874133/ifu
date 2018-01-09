package com.duplex.crawler.core.builder;

import com.duplex.crawler.core.CrawlerObject;

public class UrlPageBuilder implements IUrlBuilder {
	private String total;
	private String size;


	public String[] biuldUrl(CrawlerObject base) {
		try {
			int total = Integer.parseInt(base.getParameter().getValues(
					this.total)[0]);
			int size = Integer.parseInt(base.getParameter()
					.getValues(this.size)[0]);

			if (size > 0) {
				return new String[] { String.valueOf(total / size + 1) };
			}
		} catch (Exception e) {
		}
		return new String[] { "-1" };
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + total + " " + size;
	}
}

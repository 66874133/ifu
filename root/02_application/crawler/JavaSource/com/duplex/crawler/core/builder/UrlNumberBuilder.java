package com.duplex.crawler.core.builder;

import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.core.CrawlerObject;

public class UrlNumberBuilder implements IUrlBuilder {
	private int start;
	private IUrlBuilder end;

	public String[] biuldUrl(CrawlerObject base) {
		List<String> list = new ArrayList<String>();

		String[] ss = end.biuldUrl(base);
		if (ss.length == 1) {
			try {
				int endNumber = Integer.parseInt(ss[0]);
				for (int i = start; i <= endNumber; i++) {
					list.add(String.valueOf(i));
				}
			} catch (Exception e) {

			}
		}

		String[] result = list.toArray(new String[] {});

		// TulipLogs.getNamedLogger(base, "extractor").info(
		// result.length + " urls is built at " + this.toString()
		// + " base on url '" + base.getUrl() + "'");

		return result;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + start + " "
				+ end.toString();
	}

}

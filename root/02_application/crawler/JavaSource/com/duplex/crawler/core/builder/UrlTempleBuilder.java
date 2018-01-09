package com.duplex.crawler.core.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duplex.crawler.core.CrawlerObject;
import com.funnel.svc.util.CombinationUtil;

public class UrlTempleBuilder implements IUrlBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8943442699935540262L;
	private List<IUrlBuilder> builders;
	private String template;

	@SuppressWarnings("unchecked")
	public String[] biuldUrl(CrawlerObject base) {
		List<String> result = new ArrayList<String>();

		List<List> llist = new ArrayList<List>();

		for (IUrlBuilder builder : builders) {
			List<String> list = new ArrayList<String>();
			list.addAll(Arrays.asList(builder.biuldUrl(base)));

			llist.add(list);
		}

		llist = CombinationUtil.getCombination(llist);

		for (List list : llist) {
			String temp = template;
			for (int i = 0; i < list.size(); i++) {
				String v = (String) list.get(i);
				temp = temp.replace("$" + (i + 1), v);
			}

			result.add(temp);
		}

		return result.toArray(new String[] {});
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public List<IUrlBuilder> getBuilders() {
		return builders;
	}

	public void setBuilders(List<IUrlBuilder> builders) {
		this.builders = builders;
	}

	public String toString() {
		return template;
	}

}

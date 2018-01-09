package com.duplex.crawler.core.builder;

import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.core.CrawlerObject;

public class UrlMultipleNumberBuilder implements IUrlBuilder {
	/**
	 * 
	 */
	private static final long serialVersionUID = -20889051368461026L;

	/**
	 * 数字枚举 可以连续也可以不连续 60*1-20,33,121
	 */
	private String target;

	public String[] biuldUrl(CrawlerObject base) {
		List<String> list = getTargets(target);

		return list.toArray(new String[0]);
	}

	public List<String> getTargets(String target) {

		List<String> list = new ArrayList<String>();
		String[] targets = target.split(",");

		for (int j = 0; j < targets.length; j++) {
			if (targets[j].contains("-")) {
				list.addAll(getContinuousReplacementsList(targets[j]));
			} else {
				list.add(targets[j]);
			}

		}

		return list;
	}

	private List<String> getContinuousReplacementsList(String target) {
		List<String> list = new ArrayList<String>();

		int distance = 1;
		if (target.contains("*")) {
			String[] strs = target.split("\\*");
			distance = Integer.parseInt(strs[0]);
			target = strs[1];
		}

		String[] targets = target.split("-");

		int indexLeft = Integer.parseInt(targets[0]);
		int indexRight = Integer.parseInt(targets[1]);

		int endIndex = Math.max(indexLeft, indexRight);
		int beginIndex = Math.min(indexLeft, indexRight);

		for (int j = beginIndex; j <= endIndex; j++) {
			list.add(String.valueOf(j * distance));
		}

		return list;

	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + target;
	}

}

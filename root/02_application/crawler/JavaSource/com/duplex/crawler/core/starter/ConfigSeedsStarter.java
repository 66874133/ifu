package com.duplex.crawler.core.starter;

import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.core.CrawlerObject;

public class ConfigSeedsStarter implements IJobStarter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6056285410669052032L;

	private String prefix;
	private List<String> seeds = new ArrayList<String>();

	public List<IExecuteObject> getExecuteObjects() {

		List<IExecuteObject> list = new ArrayList<IExecuteObject>();
		for (String str : seeds) {
			if (null != prefix) {
				str = prefix + str;
			}
			CrawlerObject crawlerObject = new CrawlerObject();
			crawlerObject.setUrl(str);
			crawlerObject.setUrlType(CrawlerObject.URLTYPE_SEED);
			list.add(crawlerObject);
		}
		return list;
	}

	public void addSeed(String str) {

		if (!seeds.contains(str)) {
			seeds.add(str);
		}

	}

	public List<String> getSeeds() {
		return seeds;
	}

	public void setSeeds(List<String> seeds) {
		this.seeds = seeds;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}

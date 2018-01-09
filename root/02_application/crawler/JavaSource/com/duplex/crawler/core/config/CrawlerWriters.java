package com.duplex.crawler.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CrawlerWriters implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2045119986557750533L;
	private List<CrawlerWriter> writers;

	public CrawlerWriters() {
		this.writers = new ArrayList<CrawlerWriter>();
	}

	public void addCrawlerWriter(CrawlerWriter writer) {
		this.writers.add(writer);
	}

	public List<CrawlerWriter> getWriters() {
		return writers;
	}

	public void setWriters(List<CrawlerWriter> writers) {
		this.writers = writers;
	}

}

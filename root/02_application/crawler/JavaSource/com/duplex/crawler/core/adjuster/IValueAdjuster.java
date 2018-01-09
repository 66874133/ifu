package com.duplex.crawler.core.adjuster;

import java.io.Serializable;

import com.duplex.crawler.core.CrawlerObject;

public interface IValueAdjuster extends Serializable{
	public String adjust(String value,CrawlerObject crawlerObject);
}

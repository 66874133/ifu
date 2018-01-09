package com.duplex.crawler.core.builder;

import java.io.Serializable;

import com.duplex.crawler.core.CrawlerObject;

public interface IUrlBuilder extends Serializable {
	public String[] biuldUrl(CrawlerObject base);
}

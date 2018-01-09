package com.duplex.crawler.core.output;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerWriter;

public interface IOutput {
	public void output(CrawlerObject task, CrawlerWriter writer);
}

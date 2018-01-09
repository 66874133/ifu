package com.duplex.crawler.core.checker;

import java.io.Serializable;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerWriter;

public interface ICrawlerObjectDataChecker extends Serializable{
	public boolean isRightData(CrawlerObject task, CrawlerWriter writer);
}

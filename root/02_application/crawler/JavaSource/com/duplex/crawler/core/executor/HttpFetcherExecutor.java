package com.duplex.crawler.core.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.discover.crawler.service.CrawlerField;
import com.duplex.crawler.ExecuteResult;
import com.duplex.crawler.IExcuteable;
import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerConf;

public class HttpFetcherExecutor implements IExcuteable {

	private final static Log logger = LogFactory
			.getLog(HttpFetcherExecutor.class);

	private static final long serialVersionUID = 8893502921764613032L;

	private String key;

	public ExecuteResult excute(JobContext jobContext,
			IExecuteObject executeObject) {

		CrawlerObject crawlerObject = (CrawlerObject) executeObject;
		CrawlerConf conf = (CrawlerConf) jobContext.getParam("crawler-conf");
		logger.info("excute url = " + crawlerObject.getUrl());

		String referer = "";
		if (crawlerObject.getParameter().containsParameter(
				CrawlerField.HTTP_HEAD_FIELD_REFERER)) {
			referer = crawlerObject.getParameter().getValues(
					CrawlerField.HTTP_HEAD_FIELD_REFERER)[0];
		}

		String content;
		try {
			content = HttpFetcherClientPool.getHttpFetcherClient(
					conf.getCategory()).doCrawler(crawlerObject.getUrl(),
					conf.getEncode(), referer);

			logger.info("excute url = " + crawlerObject.getUrl() + " finished!");
			crawlerObject.setContent(content);
		} catch (Exception e) {
			logger.error("error when excute url = " + crawlerObject.getUrl(), e);
		}
		return new ExecuteResult(ExecuteResult.CODE_OK);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}

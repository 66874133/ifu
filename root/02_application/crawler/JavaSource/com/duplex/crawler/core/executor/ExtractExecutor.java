package com.duplex.crawler.core.executor;

import java.util.ArrayList;
import java.util.List;

import com.discover.crawler.service.CrawlerField;
import com.duplex.crawler.ExecuteResult;
import com.duplex.crawler.IExcuteable;
import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerConf;
import com.duplex.crawler.core.config.ExtractField;
import com.funnel.svc.util.StringUtils;

public class ExtractExecutor implements IExcuteable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7463880143384687548L;

	private String key;

	public ExecuteResult excute(JobContext jobContext,
			IExecuteObject executeObject) {
		// extract
		CrawlerObject crawlerObject = (CrawlerObject) executeObject;

		// 是否需要分裂出新对象
		boolean needExtract = false;
		ExecuteResult result = new ExecuteResult(ExecuteResult.CODE_OK);
		if (!StringUtils.isNullOrEmpty(crawlerObject.getContent())) {
			CrawlerConf conf = (CrawlerConf) jobContext
					.getParam("crawler-conf");

			List<ExtractField> extractFields = conf.getExtractor()
					.getExtractFields();

			String to = null;
			String[] urls = null;
			for (ExtractField extractField : extractFields) {
				String from = extractField.getFrom();
				if (from.equalsIgnoreCase(crawlerObject.getUrlType())) {
					String[] strings = extractField.getUrlBuilder().biuldUrl(
							crawlerObject);
					String[] stringsNew = new String[strings.length];
					System.arraycopy(strings, 0, stringsNew, 0, strings.length);
					if (null != extractField.getAdjuster()) {
						for (int i = 0; i < stringsNew.length; i++) {
							stringsNew[i] = extractField.getAdjuster().adjust(
									stringsNew[i], crawlerObject);
						}
					}

					crawlerObject.getParameter().replaceParameter(
							extractField.getName(), stringsNew);
					if (null == extractField.getName()
							|| extractField.getName().equals("")) {
						to = extractField.getTo();
						urls = stringsNew;
					}

					if (!needExtract) {
						needExtract = true;
					}

				}

			}

			// extract

			if (needExtract) {
				List<IExecuteObject> newList = new ArrayList<IExecuteObject>();

				String[] keys = crawlerObject.getParameter().getParameters();
				// String[] urls =
				// crawlerObject.getParameter().getValues("url");

				for (int i = 0; i < urls.length; i++) {
					CrawlerObject newCrawlerObject = new CrawlerObject();
					newCrawlerObject.setUrlType(to);

					// 设置为新链接的url地址
					newCrawlerObject.setUrl(urls[i]);
					// 设置Referer 地址
					newCrawlerObject.getParameter().addParameter(
							CrawlerField.HTTP_HEAD_FIELD_REFERER,
							crawlerObject.getUrl());

					for (int j = 0; j < keys.length; j++) {

						if (null != crawlerObject.getParameter().getValues(
								keys[j])
								&& crawlerObject.getParameter().getValues(
										keys[j]).length > 0) {

							if (null != keys[j]) {
								if (crawlerObject.getParameter().getValues(
										keys[j]).length < urls.length) {
									newCrawlerObject
											.getParameter()
											.addParameter(
													keys[j],
													crawlerObject
															.getParameter()
															.getValues(keys[j])[0]);
								} else {
									newCrawlerObject
											.getParameter()
											.addParameter(
													keys[j],
													crawlerObject
															.getParameter()
															.getValues(keys[j])[i]);
								}
							}

						} else {
							newCrawlerObject.getParameter().addParameter(
									keys[j], "");
						}

					}

					// 设置list发现的新对象的序号
					if (crawlerObject.getUrlType().equals(
							CrawlerObject.URLTYPE_LIST)) {
						newCrawlerObject.getParameter().addParameter(
								CrawlerField.FIELD_INDEX_IN_PAGE, "" + (i + 1));
					}

					newList.add(newCrawlerObject);
				}

				result.setNewList(newList);
				return result;
			}
		}
		return result;

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

package com.duplex.crawler.core.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.duplex.crawler.ExecuteResult;
import com.duplex.crawler.IExcuteable;
import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerConf;
import com.duplex.crawler.core.config.CrawlerWriter;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.writer.IWriterField;
import com.funnel.svc.util.StringUtils;

public class WriterExecutor implements IExcuteable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 423089448828404475L;

	private String key;

	public ExecuteResult excute(JobContext jobContext,
			IExecuteObject executeObject) {

		CrawlerObject ut = (CrawlerObject) executeObject;
		CrawlerConf conf = (CrawlerConf) jobContext.getParam("crawler-conf");
		if (!StringUtils.isNullOrEmpty(ut.getContent())) {
			HTMLPath parser = new HTMLPath(ut.getContent());
			HashMap<String, String[]> map = new HashMap<String, String[]>();

			for (CrawlerWriter cw : conf.getWriters().getWriters()) {
				if (cw.getUrlType().equals(ut.getUrlType())) {
					for (IWriterField field : cw.getFields()) {
						try {
							String[] values = field.getValues(ut, parser);
							if (field.needHandleDiscard()) {
								if (null == values || values.length < 1) {
									ut.setDataRight(false);
									ExecuteResult executeResult = new ExecuteResult(
											ExecuteResult.CODE_FAILED);
									executeResult.setMessage("满足过滤条件主动丢弃丢弃");
									return executeResult;
								}
							}
							if (field.needHandleUnicode()) {
								values = parseValue(values);
							}

							if (map.containsKey(field.getName())) {
								if (isEmtpy(map.get(field.getName()))
										&& !isEmtpy(values)) {
									map.put(field.getName(), values);
								}
							} else {
								map.put(field.getName(), values);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			for (String field : map.keySet()) {
				ut.getParameter().replaceParameter(field, map.get(field));
			}
		}
		return new ExecuteResult(ExecuteResult.CODE_OK);
	}

	private boolean isEmtpy(String[] values) {
		return values == null || values.length == 0
				|| (values.length == 1 && StringUtils.isNullOrEmpty(values[0]));
	}

	private String[] parseValue(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String value : values) {
			list.add(StringUtils.unicodeToString(value));
		}

		return list.toArray(new String[] {});
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

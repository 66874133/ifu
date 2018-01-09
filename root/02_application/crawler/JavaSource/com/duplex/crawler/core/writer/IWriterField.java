package com.duplex.crawler.core.writer;

import java.io.Serializable;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.html.HTMLPath;

public interface IWriterField extends Serializable {
	public String getName();

	public String[] getValues(CrawlerObject task, HTMLPath path);

	public boolean isJobField();

	public boolean needHandleUnicode();
	
	public boolean needHandleDiscard();
}

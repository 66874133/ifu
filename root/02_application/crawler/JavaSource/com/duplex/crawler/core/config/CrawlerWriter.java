package com.duplex.crawler.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.checker.ICrawlerObjectDataChecker;
import com.duplex.crawler.core.output.IOutput;
import com.duplex.crawler.core.writer.IWriterField;

public class CrawlerWriter implements Serializable {
	private String urlType;
	private boolean written;
	private String format;

	private List<IWriterField> fields;

	private IOutput output;

	private ICrawlerObjectDataChecker taskChecker;

	public CrawlerWriter() {
		this.fields = new ArrayList<IWriterField>();
	}

	public void addWriterField(IWriterField field) {
		this.fields.add(field);
	}

	public List<IWriterField> getFields() {
		return fields;
	}

	public void setFields(List<IWriterField> fields) {
		this.fields = fields;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public boolean isWritten() {
		return written;
	}

	public void setWritten(boolean written) {
		this.written = written;
	}

	public ICrawlerObjectDataChecker getTaskChecker() {
		return taskChecker;
	}

	public void setTaskChecker(ICrawlerObjectDataChecker taskChecker) {
		this.taskChecker = taskChecker;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public IOutput getOutput() {
		return output;
	}

	public void setOutput(IOutput output) {
		this.output = output;
	}

	public void write(CrawlerObject task) {
		if (written && task.getUrlType().equals(urlType)) {
			if (getOutput() != null) {
				if (taskChecker == null || taskChecker.isRightData(task, this)) {
					task.setDataRight(true);
					getOutput().output(task, this);
				} else {
					System.out.println("data-error");
				}
			}
		}
	}
}

package com.duplex.crawler.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CrawlerExtractor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 999387946464655743L;
	private List<ExtractField> extractFields;

	public CrawlerExtractor() {
		this.extractFields = new ArrayList<ExtractField>();
	}

	public void addExtractField(ExtractField field) {
		this.extractFields.add(field);
	}

	public List<ExtractField> getExtractFields() {
		return extractFields;
	}

	public void setExtractFields(List<ExtractField> extractFields) {
		this.extractFields = extractFields;
	}

}

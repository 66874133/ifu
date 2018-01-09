package com.duplex.crawler.core.config;

import java.io.Serializable;

import com.duplex.crawler.core.adjuster.IValueAdjuster;
import com.duplex.crawler.core.builder.IUrlBuilder;


public class ExtractField implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5589261776947263717L;
	private String name;
	private String from;
	private String to;
	private IUrlBuilder urlBuilder;
	private IValueAdjuster adjuster;
	private boolean isParameter;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public IUrlBuilder getUrlBuilder() {
		return urlBuilder;
	}

	public void setUrlBuilder(IUrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	public IValueAdjuster getAdjuster() {
		return adjuster;
	}

	public void setAdjuster(IValueAdjuster adjuster) {
		this.adjuster = adjuster;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isParameter() {
		return isParameter;
	}

	public void setParameter(boolean isParameter) {
		this.isParameter = isParameter;
	}

}

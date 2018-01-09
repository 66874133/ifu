package com.duplex.crawler.core;

import com.duplex.crawler.DependExecuteObject;

public class CrawlerObject extends DependExecuteObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3096081714921876925L;
	private String url;
	public static final String URLTYPE_SEED = "seed";
	public static final String URLTYPE_ITEM = "item";
	public static final String URLTYPE_LIST = "list";
	public static final String URLTYPE_NEXT = "next";
	

	private String[] newUrls;
	private String urlType;

	private transient String content;
	private int deep;

	private boolean dataRight = true;
	private UrlParmeter parameter = new UrlParmeter();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UrlParmeter getParameter() {
		return parameter;
	}

	public void setParameter(UrlParmeter parameter) {
		this.parameter = parameter;
	}

	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public boolean isDataRight() {
		return dataRight;
	}

	public void setDataRight(boolean dataRight) {
		this.dataRight = dataRight;
	}

	public String[] getNewUrls() {
		return newUrls;
	}

	public void setNewUrls(String[] newUrls) {
		this.newUrls = newUrls;
	}

	@Override
	public String toString() {
		return "CrawlerObject [url=" + url + ", urlType=" + urlType + "]";
	}

	@SuppressWarnings("unchecked")
	public CrawlerObject clone() {
		CrawlerObject task = new CrawlerObject();
		task.deep = this.deep++;

		if (parameter != null) {
			UrlParmeter newParam = new UrlParmeter();
			for (String name : parameter.getParameters()) {
				newParam.addParameter(name, parameter.getValues(name));
			}

			task.parameter = newParam;
		}

		return task;
	}
}

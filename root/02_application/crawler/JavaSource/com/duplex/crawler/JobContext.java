package com.duplex.crawler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.duplex.crawler.ExcuteDepend;



public class JobContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344668696158925881L;

	private ExcuteDepend depend;

	private String jobKey;

	private Map<String, Object> params = new HashMap<String, Object>();

	public JobContext(String key) {
		this.jobKey = key;
	}
	
	public Object getParam(String key)
	{
		return params.get(key);
	}

	public void putParam(String str, Object object) {
		params.put(str, object);
	}

	public String getJobKey() {
		return jobKey;
	}

	public ExcuteDepend getDepend() {
		return depend;
	}

	public void setDepend(ExcuteDepend depend) {
		this.depend = depend;
	}

}

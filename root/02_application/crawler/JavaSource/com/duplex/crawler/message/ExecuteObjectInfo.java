package com.duplex.crawler.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.IExecuteObject;

public class ExecuteObjectInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6546676006415233023L;

	private String jobKey;

	private int count;

	private List<IExecuteObject> executeObjects = new ArrayList<IExecuteObject>();

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<IExecuteObject> getExecuteObjects() {
		return executeObjects;
	}

	public void setExecuteObjects(List<IExecuteObject> executeObjects) {
		this.executeObjects = executeObjects;
	}

	public void addExecuteObject(IExecuteObject object) {
		executeObjects.add(object);
	}

	public String getJobKey() {
		return jobKey;
	}

	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}
}

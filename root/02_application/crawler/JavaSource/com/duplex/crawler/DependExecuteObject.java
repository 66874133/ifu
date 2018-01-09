package com.duplex.crawler;

import java.util.ArrayList;
import java.util.List;

public class DependExecuteObject implements IExecuteObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1276722984731670620L;

	private String name;

	protected String jobName;
	protected String startTime;
	protected String clientName;

	public boolean isFaild = false;

	private List<String> progress = new ArrayList<String>();

	public ExecuteStep step = null;

	/**
	 * 获取任务处理进度
	 * 
	 * @return
	 */
	public synchronized List<String> getTaskProgress() {

		return progress;
	}

	public synchronized void addTaskProgress(String key) {
		progress.add(key);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

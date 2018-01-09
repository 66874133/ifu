package com.duplex.crawler.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.JobContext;

public class JobContextInfo implements Serializable {

	private boolean running;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5045226626507933112L;
	private List<JobContext> jobContexts = new ArrayList<JobContext>();

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public List<JobContext> getJobContexts() {
		return jobContexts;
	}
	

	public void setJobContexts(List<JobContext> jobContexts) {
		this.jobContexts = jobContexts;
	}

	public void addJobContext(JobContext jobContext) {
		jobContexts.add(jobContext);
	}

}

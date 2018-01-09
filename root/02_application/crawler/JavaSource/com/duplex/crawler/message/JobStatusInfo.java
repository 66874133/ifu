package com.duplex.crawler.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.duplex.crawler.JobStatus;

public class JobStatusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2630402991573002167L;
	private List<JobStatus> info = new ArrayList<JobStatus>();

	public List<JobStatus> getInfo() {
		return info;
	}

	public void setInfo(List<JobStatus> info) {
		this.info = info;
	}

	public void addJobStatus(JobStatus status) {
		info.add(status);
	}

}

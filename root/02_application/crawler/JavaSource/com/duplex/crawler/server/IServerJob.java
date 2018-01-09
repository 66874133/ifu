package com.duplex.crawler.server;

import java.util.List;

import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobStatus;

public interface IServerJob {

	public void start();

	public JobStatus status();
	
	public String getJobKey();

	public  List<IExecuteObject> fetch(int size);
}

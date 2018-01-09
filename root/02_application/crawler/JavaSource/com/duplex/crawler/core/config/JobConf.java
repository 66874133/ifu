package com.duplex.crawler.core.config;

import com.duplex.crawler.core.starter.IJobStarter;

public class JobConf {
	private IJobStarter starter;

	public IJobStarter getStarter() {
		return starter;
	}

	public void setStarter(IJobStarter starter) {
		this.starter = starter;
	}
}

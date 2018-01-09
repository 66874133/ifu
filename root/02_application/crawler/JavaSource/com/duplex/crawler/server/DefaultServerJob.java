package com.duplex.crawler.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobStatus;
import com.duplex.crawler.core.starter.IJobStarter;

public class DefaultServerJob implements IServerJob {

	private Logger logger = Logger.getLogger(this.getClass());
	private String jobKey;
	private JobStatus jobStatus = new JobStatus();
	private List<IExecuteObject> waiting = new ArrayList<IExecuteObject>();

	private List<IExecuteObject> processing = new ArrayList<IExecuteObject>();

	private List<IExecuteObject> success = new ArrayList<IExecuteObject>();
	private List<IExecuteObject> failed = new ArrayList<IExecuteObject>();
	private IJobStarter starter;

	public DefaultServerJob(String jobKey) {
		new DefaultServerJob(jobKey, null);
	}

	public DefaultServerJob(String jobKey, IJobStarter starter) {
		this.jobKey = jobKey;
		this.starter = starter;

		ExcuteThreadPoolDaemonThread daemonThread = new ExcuteThreadPoolDaemonThread();
		daemonThread.start();
	}

	public List<IExecuteObject> fetch(int size) {

		List<IExecuteObject> fetchObject = new ArrayList<IExecuteObject>();

		int i = waiting.size();

		size = Math.min(i, size);

		for (int j = 0; j < size; j++) {

			IExecuteObject executeObject = waiting.remove(0);
			fetchObject.add(executeObject);
			processing.add(executeObject);
		}

		return fetchObject;
	}

	public JobStatus status() {

		jobStatus.put("waiting", "" + waiting.size());
		jobStatus.put("processing", "" + processing.size());
		jobStatus.put("success", "" + success.size());

		logger.info("status-:waiting=" + waiting.size() + "processing="
				+ processing.size() + "success=" + success.size());
		return jobStatus;
	}

	public void start() {

		if (null != starter) {
			List<IExecuteObject> list = starter.getExecuteObjects();

			waiting.addAll(list);

		}
		logger.info("DefaultServerJob name=" + jobKey + " is start! list="
				+ waiting);
	}

	public String getJobKey() {
		return jobKey;
	}

	private class ExcuteThreadPoolDaemonThread extends Thread {

		@Override
		public void run() {
			while (true) {
				logger.info("等待执行的对象" + waiting.size() + "个," + "正在执行的对象"
						+ processing.size() + "个," + " 执行成功的对象"
						+ success.size() + "个," + " 执行失败的对象" + failed.size()
						+ "个");

				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					logger.error("", e);
				}
			}
		}

	}

}

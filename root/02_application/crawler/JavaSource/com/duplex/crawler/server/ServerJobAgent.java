package com.duplex.crawler.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.duplex.crawler.JobContext;
import com.duplex.crawler.message.MessageObject;
import com.duplex.frame.handler.ObjectProtocolHandler;
import com.duplex.frame.handler.response.ObjectResponseHandler;
import com.duplex.frame.mina.server.MinaTCPServer;

/**
 * ServerJobAgent 任务管理池
 * 
 * @author Jerry
 * 
 */
public class ServerJobAgent {

	private Logger logger = Logger.getLogger(this.getClass());
	private Map<String, JobContext> keyToJobContext = new HashMap<String, JobContext>();
	/**
	 * 一个JobConf 对应一个serverjob
	 */
	private Map<String, IServerJob> keyToJob = new HashMap<String, IServerJob>();

	/**
	 * 等待执行的任务
	 */
	private List<String> waitingJobs = new ArrayList<String>();

	/**
	 * 服务端已启动的任务
	 */
	private List<String> startedJobs = new ArrayList<String>();

	/**
	 * 客户端已启动的任务
	 */
	private List<String> runningJobs = new ArrayList<String>();
	/**
	 * 执行失败的任务
	 */
	private List<String> failedJobs = new ArrayList<String>();

	/**
	 * 已完成的任务
	 */
	private List<String> finishedJobs = new ArrayList<String>();

	private MinaTCPServer minaTCPServer;

	public ServerJobAgent(int port) {
		minaTCPServer = new MinaTCPServer(port);
	}

	public void register(ObjectResponseHandler<MessageObject> objectResponseHandler) {

		minaTCPServer.setHandler(new ObjectProtocolHandler<MessageObject>(objectResponseHandler));
	}

	public void start() {
		minaTCPServer.start();
		ExcuteThreadPoolDaemonThread daemonThread = new ExcuteThreadPoolDaemonThread();
		daemonThread.start();
	}

	public void startJob(JobContext jobContext, IServerJob serverJob) {
		serverJob.start();
		startedJobs.add(jobContext.getJobKey());
		keyToJob.put(jobContext.getJobKey(), serverJob);
		keyToJobContext.put(jobContext.getJobKey(), jobContext);
	}

	public boolean isStarted(String key) {
		return startedJobs.contains(key);
	}

	public boolean isRunning(String key) {
		return runningJobs.contains(key);
	}

	public JobContext getJobContext(String key) {
		return keyToJobContext.get(key);
	}

	public Map<String, JobContext> getKeyToKeyToJobContext() {
		return keyToJobContext;
	}

	public IServerJob getServerJob(String key) {
		return keyToJob.get(key);
	}

	public List<String> getWaitingJobs() {
		return waitingJobs;
	}

	public List<String> getStartedJobs() {
		return startedJobs;
	}

	public List<String> getRunningJobs() {
		return runningJobs;
	}

	private class ServerJobAddThread extends Thread {
		@Override
		public void run() {
			while (true) {
				// 监控waitingJobs 看里面是否有可以启动的任务
			}
		}
	}

	private class ExcuteThreadPoolDaemonThread extends Thread {

		@Override
		public void run() {
			while (true) {
				logger.info("等待执行的任务" + waitingJobs.size() + "个," + "可执行的任务"
						+ startedJobs.size() + "个," + "客户端正在执行的任务"
						+ runningJobs.size() + "个," + " 执行成功的任务"
						+ finishedJobs.size() + "个," + " 执行失败的任务"
						+ failedJobs.size() + "个");

				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

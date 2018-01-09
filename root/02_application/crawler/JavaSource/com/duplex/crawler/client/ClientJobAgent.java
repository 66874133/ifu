package com.duplex.crawler.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.duplex.crawler.JobContext;
import com.duplex.crawler.message.ExecuteObjectInfo;
import com.duplex.crawler.message.MessageObject;
import com.duplex.crawler.server.protocol.IProtocolAdapter;
import com.duplex.frame.handler.ObjectProtocolHandler;
import com.duplex.frame.handler.response.ObjectResponseHandler;
import com.duplex.frame.mina.client.MinaTCPClient;

/**
 * ClientJobPool总的管理池 一个JVM里面只有一个 每个JOB分别对应一个clientjob和一个serverjob
 * 
 * @author Jerry
 * 
 */
public class ClientJobAgent {
	private Logger logger = Logger.getLogger(this.getClass());
	private List<IClientJob> clientJobs = new ArrayList<IClientJob>();

	private Map<String, JobContext> keyToJobContext = new HashMap<String, JobContext>();

	private MinaTCPClient minaTCPClient;

	private Map<String, IClientJob> keyToJob = new HashMap<String, IClientJob>();
	private String strAddress;

	public ClientJobAgent(String strAddress) {
		this.strAddress = strAddress;
		minaTCPClient = new MinaTCPClient(strAddress);
	}
	
	public void register(ObjectResponseHandler<MessageObject> objectResponseHandler)
	{
		minaTCPClient.setHandler(new ObjectProtocolHandler<MessageObject>(objectResponseHandler));
	}

	public void start() {
		minaTCPClient.connect(strAddress);

		FetchJobThread fetchJobThread = new FetchJobThread();
		fetchJobThread.start();

		FetchExecuteObjectThread fetchExecuteObjectThread = new FetchExecuteObjectThread();
		fetchExecuteObjectThread.start();
	}

	public void startJob(JobContext jobContext, IClientJob clientJob) {
		clientJob.start();
		clientJobs.add(clientJob);
		keyToJobContext.put(jobContext.getJobKey(), jobContext);
		keyToJob.put(jobContext.getJobKey(), clientJob);
	}

	public IClientJob getClientJob(String key) {
		return keyToJob.get(key);
	}

	private void fetchJobs() {
		MessageObject messageObject = new MessageObject();
		messageObject.setCode(IProtocolAdapter.FETCH_JOB_ALL);
		minaTCPClient.send(messageObject);
	}

	private void fetchExecuteObjects(String jobKey) {
		MessageObject messageObject = new MessageObject();

		ExecuteObjectInfo executeObjectInfo = new ExecuteObjectInfo();
		executeObjectInfo.setJobKey(jobKey);
		messageObject.setCode(IProtocolAdapter.FETCH_OBJECT);
		messageObject.setObject(executeObjectInfo);
		minaTCPClient.send(messageObject);
	}

	/**
	 * 定时获取可执行任务列表
	 * 
	 * @author Jerry
	 * 
	 */
	private class FetchJobThread extends Thread {
		@Override
		public void run() {

			while (true) {

				logger.info("fetch jobs");
				fetchJobs();
				logger.info("fetch jobs ok!");
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private class FetchExecuteObjectThread extends Thread {
		@Override
		public void run() {

			while (true) {

				System.out.println("fetch objects");
				for (JobContext jobContext : keyToJobContext.values()) {

					if (getClientJob(jobContext.getJobKey()).canFetch()) {
						fetchExecuteObjects(jobContext.getJobKey());
					}
				}

				System.out.println("fetch objects ok!");
				try {
					sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

}

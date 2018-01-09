package com.duplex.crawler.client.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.crawler.JobContext;
import com.duplex.crawler.client.ClientJobAgent;
import com.duplex.crawler.client.DependClientJob;
import com.duplex.crawler.client.IClientJob;
import com.duplex.crawler.message.JobContextInfo;
import com.duplex.crawler.message.MessageObject;
import com.duplex.crawler.server.protocol.IProtocolAdapter;

public class FETCH_JOB_ALL_ACK_Handler implements IProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private ClientJobAgent clientJobAgent;

	public FETCH_JOB_ALL_ACK_Handler(ClientJobAgent clientJobAgent) {
		this.clientJobAgent = clientJobAgent;
	}

	public void work(IoSession session, MessageObject messageObject) {

		JobContextInfo jobContextInfo = (JobContextInfo) messageObject
				.getObject();

		int size = jobContextInfo.getJobContexts().size();
		logger.info("收到服务器发来的任务:共" + jobContextInfo.getJobContexts().size()
				+ "个");
		if (size > 0) {

			for (JobContext jobContext : jobContextInfo.getJobContexts()) {
				IClientJob clientJob = new DependClientJob(jobContext);
				clientJobAgent.startJob(jobContext, clientJob);
				jobContextInfo.setRunning(true);
			}
			
		}

	}

	public String getMessageCode() {
		return UPDATE_JOB_RUNNING_ACK;
	}

}

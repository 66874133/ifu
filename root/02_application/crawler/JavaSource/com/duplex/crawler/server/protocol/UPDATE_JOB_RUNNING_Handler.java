package com.duplex.crawler.server.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.crawler.JobContext;
import com.duplex.crawler.message.JobContextInfo;
import com.duplex.crawler.message.MessageObject;
import com.duplex.crawler.server.ServerJobAgent;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class UPDATE_JOB_RUNNING_Handler implements IProtocolAdapter {
	private Logger logger = Logger.getLogger(this.getClass());

	private ServerJobAgent pool;

	public UPDATE_JOB_RUNNING_Handler(ServerJobAgent pool) {
		this.pool = pool;
	}

	public void work(IoSession session, MessageObject messageObject) {
		JobContextInfo jobContextInfo = (JobContextInfo) messageObject
				.getObject();

		for (JobContext jobContext : jobContextInfo.getJobContexts()) {
			pool.getRunningJobs().add(jobContext.getJobKey());
			logger.info("客户端已开始执行任务:" + jobContext.getJobKey());
		}

	}

	public String getMessageCode() {
		return UPDATE_JOB_RUNNING;
	}
}

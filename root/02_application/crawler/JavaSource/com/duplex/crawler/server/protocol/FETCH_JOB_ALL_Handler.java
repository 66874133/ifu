package com.duplex.crawler.server.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

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
public class FETCH_JOB_ALL_Handler implements IProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private ServerJobAgent pool;

	public FETCH_JOB_ALL_Handler(ServerJobAgent pool) {
		this.pool = pool;
	}

	public void work(IoSession session, MessageObject messageObject) {

		logger.info("有客户端请求派发任务:id=" + session.getId());
		JobContextInfo jobContextInfo = new JobContextInfo();

		int n = 0;
		for (String jobKey : pool.getKeyToKeyToJobContext().keySet()) {

			// 服务端已经启动等待客户端执行
			if (pool.isStarted(jobKey) && !pool.isRunning(jobKey)) {
				jobContextInfo.addJobContext(pool.getJobContext(jobKey));
				n++;
			}
		}

		logger.info("派发完成,共计" + n + "个任务");
		messageObject.setCode(FETCH_JOB_ALL_ACK);
		messageObject.setObject(jobContextInfo);
		session.write(messageObject);

	}

	public String getMessageCode() {
		return FETCH_JOB_ALL;
	}
}

package com.duplex.crawler.server.protocol;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.message.ExecuteObjectInfo;
import com.duplex.crawler.message.MessageObject;
import com.duplex.crawler.server.IServerJob;
import com.duplex.crawler.server.ServerJobAgent;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class FETCH_OBJECT_Handler implements IProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private ServerJobAgent pool;

	public FETCH_OBJECT_Handler(ServerJobAgent pool) {
		this.pool = pool;
	}

	public void work(IoSession session, MessageObject messageObject) {
		logger.info("有客户端请求派发执行对象...");
		ExecuteObjectInfo executeObjectInfo = (ExecuteObjectInfo) messageObject
				.getObject();

		IServerJob serverJob = pool.getServerJob(executeObjectInfo.getJobKey());

		int count = executeObjectInfo.getCount();
		List<IExecuteObject> iFetchObject = null;
		if (count > 0) {
			iFetchObject = serverJob.fetch(count);
		} else {
			iFetchObject = serverJob.fetch(50);
		}

		executeObjectInfo.setExecuteObjects(iFetchObject);

		messageObject.setCode(FETCH_OBJECT_ACK);
		messageObject.setObject(executeObjectInfo);
		session.write(messageObject);
		logger.info("派发执行对象完成,共计" + count + "个");
	}

	public String getMessageCode() {
		return FETCH_OBJECT;
	}
}

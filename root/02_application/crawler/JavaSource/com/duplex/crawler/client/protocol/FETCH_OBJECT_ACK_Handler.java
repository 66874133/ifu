package com.duplex.crawler.client.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.client.ClientJobAgent;
import com.duplex.crawler.message.ExecuteObjectInfo;
import com.duplex.crawler.message.MessageObject;
import com.duplex.crawler.server.protocol.IProtocolAdapter;

public class FETCH_OBJECT_ACK_Handler implements  IProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private ClientJobAgent clientJobAgent;

	public FETCH_OBJECT_ACK_Handler(ClientJobAgent clientJobAgent) {
		this.clientJobAgent = clientJobAgent;
	}

	public void work(IoSession session, MessageObject messageObject) {

		ExecuteObjectInfo info = (ExecuteObjectInfo) messageObject.getObject();

		logger.info("收到服务器发来的可执行对象:共" + info.getExecuteObjects().size() + "个");
		for (IExecuteObject obj : info.getExecuteObjects()) {
			clientJobAgent.getClientJob(info.getJobKey()).add(obj);
		}

		// messageObject.setCode(CrawlerProtocol.UPDATE_JOB_RUNNING);
		// messageObject.setObject(confInfo);
		// session.write(messageObject); 回应接受成功

	}

	public String getMessageCode() {
		return FETCH_OBJECT_ACK;
	}

}

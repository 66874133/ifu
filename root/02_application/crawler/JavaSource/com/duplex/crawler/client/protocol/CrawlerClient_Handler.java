package com.duplex.crawler.client.protocol;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.crawler.message.MessageObject;
import com.duplex.crawler.server.protocol.IProtocolAdapter;
import com.duplex.frame.handler.response.ObjectResponseHandler;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class CrawlerClient_Handler extends ObjectResponseHandler<MessageObject> {

	private Logger logger = Logger.getLogger(this.getClass());

	private Map<String, IProtocolAdapter> map = new HashMap<String, IProtocolAdapter>();

	public void addHandler(IProtocolAdapter protocolAdapter) {
		map.put(protocolAdapter.getMessageCode(), protocolAdapter);
	}

	@Override
	protected MessageObject toProtocolObject(MessageObject dintput)
			throws Exception {
		return dintput;
	}

	@Override
	protected void handle(IoSession session, MessageObject messageObject) {

		logger.info("有客户端请求派发任务:id=" + session.getId());

		map.get(messageObject.getCode()).work(session, messageObject);

	}
}

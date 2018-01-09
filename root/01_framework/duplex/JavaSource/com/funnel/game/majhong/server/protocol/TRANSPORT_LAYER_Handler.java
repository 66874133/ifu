package com.funnel.game.majhong.server.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.frame.handler.response.ByteArrayMessageResponseHandler;
import com.duplex.frame.mina.MinaDataInputStream;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class TRANSPORT_LAYER_Handler extends
		ByteArrayMessageResponseHandler<TransportProtocol> {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	protected TransportProtocol toProtocolObject(MinaDataInputStream dintput)
			throws Exception {

		int ack = dintput.readInt();
		logger.info("ack=" + ack);
		int broadcast = dintput.readInt();
		logger.info("broadcast=" + broadcast);
		TransportProtocol t = new TransportProtocol();

		t.setAck(ack);
		t.setBroadcast(broadcast);
		return t;
	}

	@Override
	protected void handle(IoSession session, TransportProtocol t) {
		if (t.getAck() > 0) {
			logger.info("TransportProtocol=" + t);
		}

	}
}

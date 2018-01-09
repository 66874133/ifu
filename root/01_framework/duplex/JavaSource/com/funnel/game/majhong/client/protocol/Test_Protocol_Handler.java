package com.funnel.game.majhong.client.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.duplex.frame.handler.response.ObjectResponseHandler;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class Test_Protocol_Handler extends
		ObjectResponseHandler<IoBuffer> {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	protected IoBuffer toProtocolObject(
			IoBuffer dintput) throws Exception {
		return dintput;
	}

	@Override
	protected void handle(IoSession session, IoBuffer t) {
		logger.info("handle t=" + t);


		byte[] bs = new byte[t.limit()];
		t.get(bs);
		logger.info("handle bs=" + bs);
	}
}

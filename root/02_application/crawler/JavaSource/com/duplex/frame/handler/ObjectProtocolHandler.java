package com.duplex.frame.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.duplex.frame.handler.response.ObjectResponseHandler;

public class ObjectProtocolHandler<T> extends IoHandlerAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private ObjectResponseHandler<T> objectResponseHandler;

	public ObjectProtocolHandler(ObjectResponseHandler<T> objectResponseHandler) {
		this.objectResponseHandler = objectResponseHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.
	 * apache .mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println(session.getRemoteAddress().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org
	 * .apache .mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		logger.info("收到服务器发来的消息:" + message);

		if (null != objectResponseHandler) {
			objectResponseHandler.work(session, (T) message);
		}

	}

}

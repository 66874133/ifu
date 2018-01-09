package com.duplex.frame.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.duplex.frame.ByteArrayTransfer.Mode;
import com.duplex.frame.handler.response.ByteArrayMessageResponseHandler;
import com.duplex.frame.mina.MinaDataInputStream;

public class ByteArraytProtocolHandler extends IoHandlerAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private ByteArrayMessageResponseHandler byteArrayMessageResponseHandler;

	private Mode mode;

	public ByteArraytProtocolHandler(
			ByteArrayMessageResponseHandler byteArrayMessageResponseHandler,
			Mode name) {
		this.byteArrayMessageResponseHandler = byteArrayMessageResponseHandler;
		this.mode = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.
	 * apache .mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("客户端连接成功 getRemoteAddress="
				+ session.getRemoteAddress().toString());
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

		logger.info("接收消息.." + session.getRemoteAddress().toString());
		try {
			MinaDataInputStream dataInputStream = (MinaDataInputStream) message;
			if (null != byteArrayMessageResponseHandler) {
				byteArrayMessageResponseHandler.work(session, dataInputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("收到服务器发来的消息:" + message);
		}

	}

	

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("sessionOpened id=" + session.getId());
		super.sessionOpened(session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("sessionClosed id=" + session.getId());
		super.sessionClosed(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if (cause instanceof IOException) {
			IOException exception = (IOException) cause;
			logger.info("客户端主动终止连接 id=" + session.getId() + " "
					+ exception.getMessage());
		} else {
			logger.info("exceptionCaught id=" + session.getId());
			super.exceptionCaught(session, cause);
		}

	}
}

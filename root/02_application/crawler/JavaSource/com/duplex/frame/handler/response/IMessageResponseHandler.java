package com.duplex.frame.handler.response;

import org.apache.mina.core.session.IoSession;

public interface IMessageResponseHandler<T> {

	public void work(IoSession session, T t);
}

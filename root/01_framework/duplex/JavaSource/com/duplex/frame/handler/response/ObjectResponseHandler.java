package com.duplex.frame.handler.response;

import org.apache.mina.core.session.IoSession;

public abstract class ObjectResponseHandler<T> implements
		IMessageResponseHandler<T> {

	@SuppressWarnings("rawtypes")
	private IMessageResponseHandler nextLayerhandler;

	protected abstract T toProtocolObject(T dintput)
			throws Exception;

	protected abstract void handle(IoSession session, T t);

	@SuppressWarnings("unchecked")
	public void work(IoSession session, T object) {

		
		try {

			T t = toProtocolObject(object);
			handle(session, t);
			if (null != nextLayerhandler) {
				System.out.println("nextLayerhandler="
						+ nextLayerhandler.getClass().getName());
				nextLayerhandler.work(session, object);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setNextLayerhandler(IMessageResponseHandler nextLayerhandler) {
		this.nextLayerhandler = nextLayerhandler;
	}

}

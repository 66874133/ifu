package com.duplex.frame.handler.response;

import org.apache.mina.core.session.IoSession;

import com.duplex.frame.mina.MinaDataInputStream;

public abstract class ByteArrayMessageResponseHandler<T> implements
		IMessageResponseHandler<MinaDataInputStream> {

	@SuppressWarnings("rawtypes")
	private IMessageResponseHandler nextLayerhandler;

	protected abstract T toProtocolObject(MinaDataInputStream dintput)
			throws Exception;

	protected abstract void handle(IoSession session, T t);

	@SuppressWarnings("unchecked")
	public void work(IoSession session, MinaDataInputStream dintput) {

		
		try {

			T t = toProtocolObject(dintput);
			handle(session, t);
			if (null != nextLayerhandler) {
				System.out.println("nextLayerhandler="
						+ nextLayerhandler.getClass().getName());
				nextLayerhandler.work(session, dintput);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setNextLayerhandler(IMessageResponseHandler nextLayerhandler) {
		this.nextLayerhandler = nextLayerhandler;
	}

}

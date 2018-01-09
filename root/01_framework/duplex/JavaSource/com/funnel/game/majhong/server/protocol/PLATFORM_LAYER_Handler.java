package com.funnel.game.majhong.server.protocol;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.duplex.frame.handler.response.ByteArrayMessageResponseHandler;
import com.duplex.frame.mina.MinaDataInputStream;
import com.funnel.game.majhong.server.protocol.platform.EXIT_GAME_Protocol;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class PLATFORM_LAYER_Handler extends
		ByteArrayMessageResponseHandler<PlatformProtocol> {

	private Logger logger = Logger.getLogger(this.getClass());

	private Map<Integer, IPlatformProtocolAdapter> map = new HashMap<Integer, IPlatformProtocolAdapter>();

	public void addHandler(IPlatformProtocolAdapter protocolAdapter) {
		map.put(protocolAdapter.getMessageCode(), protocolAdapter);
	}

	@Override
	protected PlatformProtocol toProtocolObject(MinaDataInputStream dintput)
			throws Exception {

		int messageCode = dintput.readInt();
		logger.info("messageCode="+messageCode);
		int operation = dintput.readInt();
		logger.info("operation="+operation);
		int keySize = dintput.readInt();
		logger.info("keySize="+keySize);
		byte[] keyBytes = new byte[keySize];
		dintput.read(keyBytes, 0, keySize);
		String key = new String(keyBytes);
		logger.info("key="+key);
		int retry = dintput.readInt();
		logger.info("retry="+retry);
		long ts = dintput.readLong();
		logger.info("ts="+ts);
		
		PlatformProtocol t = new PlatformProtocol();

		t.setOperation(operation);
		t.setMessageCode(messageCode);
		t.setKey(key);
		t.setT(ts);
		t.setRetry(retry);
		return t;
	}

	@Override
	protected void handle(IoSession session, PlatformProtocol t) {

		logger.info("Platform=" + t);

//		EXIT_GAME_Protocol start_GAME_Protocol = new EXIT_GAME_Protocol();
//		start_GAME_Protocol.work(session, t);
		IPlatformProtocolAdapter adapter = map.get(t.getMessageCode());
		logger.info("Platform adapter=" + adapter.getClass().getName());
		adapter.work(session, t);

	}

}

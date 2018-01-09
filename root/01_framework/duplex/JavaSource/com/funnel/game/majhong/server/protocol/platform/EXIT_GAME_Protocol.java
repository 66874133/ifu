package com.funnel.game.majhong.server.protocol.platform;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;
import com.funnel.game.majhong.server.protocol.IPlatformProtocolAdapter;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.game.TestMap;
import com.funnel.game.majhong.server.protocol.game.login.SessionManager;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class EXIT_GAME_Protocol implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	public void work(IoSession session, PlatformProtocol t) {
		logger.info("有客户端请求START_GAME...");

		String sessionId = t.getKey();
		String message = "";
		String roomKey = SessionManager.getMajhongSession(sessionId)
				.getRoomKey();

		TestMap.roomKeyToSessions.get(roomKey).remove(
				SessionManager.getMajhongSession(sessionId));

		message = sessionId+"退出房间"+roomKey;
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();
		String data = "{\"message\":\"" + message + "!\",\"sessionId\":\""
				+ sessionId + "\"}";
		clientTransportProtocol.setData(data);
		clientTransportProtocol.setRet(1);
		clientTransportProtocol.setStatus(200);
		clientTransportProtocol.setVersion(System.currentTimeMillis());

		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1008;
	}

}

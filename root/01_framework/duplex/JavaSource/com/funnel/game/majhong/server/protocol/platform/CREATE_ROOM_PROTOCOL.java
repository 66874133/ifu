package com.funnel.game.majhong.server.protocol.platform;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;
import com.funnel.game.majhong.server.protocol.IPlatformProtocolAdapter;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.game.TestMap;
import com.funnel.game.majhong.server.protocol.game.login.MajhongSession;
import com.funnel.game.majhong.server.protocol.game.login.SessionManager;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class CREATE_ROOM_PROTOCOL implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	public void work(IoSession session, PlatformProtocol t) {
		logger.info("有客户端请求CREATE_ROOM_PROTOCOL...");

		String sessionId = t.getKey();
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();

		String roomKey = "1234";
		String message = "创建成功房卡密码"+roomKey;
		String data = "{" + "\"sessionId\":\"" + sessionId
				+ "\",\"message\":\"" + message + "\"" + "}";

		TestMap.roomKeyToSessions.put(roomKey, new ArrayList<MajhongSession>());
		SessionManager.getMajhongSession(sessionId).setRoomKey(roomKey);
		TestMap.roomKeyToSessions.get(roomKey).add(SessionManager.getMajhongSession(sessionId));

		clientTransportProtocol.setData(data);
		clientTransportProtocol.setRet(0);
		clientTransportProtocol.setStatus(0);
		clientTransportProtocol.setVersion(System.currentTimeMillis());

		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1005;
	}

}

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
public class JOIN_ROOM_PROTOCOL implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	public void work(IoSession session, PlatformProtocol t) {
		logger.info("有客户端请求CREATE_ROOM_PROTOCOL...");

		String roomKeyAndSessionId = t.getKey();
		String message = "";
		String[] strings = roomKeyAndSessionId.split("--");
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();
		if (null != TestMap.roomKeyToSessions.get(strings[0])) {
			if (TestMap.roomKeyToSessions.get(strings[0]).size() <= 4) {
				SessionManager.getMajhongSession(strings[1]).setRoomKey(strings[0]);
				TestMap.roomKeyToSessions.get(strings[0]).add(
						SessionManager.getMajhongSession(strings[1]));
				clientTransportProtocol.setStatus(0);
				message = "加入房间成功,该房间信息为xxxx";
			} else {
				message = "房间人数超过4人";
				clientTransportProtocol.setStatus(1);
			}
		}

		String data = "{\"message\":\"" + message + "!\",\"validNum\":\""
				+ System.currentTimeMillis() + "\"}";
		clientTransportProtocol.setData(data);
		clientTransportProtocol.setRet(0);
		clientTransportProtocol.setVersion(System.currentTimeMillis());

		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1006;
	}

}

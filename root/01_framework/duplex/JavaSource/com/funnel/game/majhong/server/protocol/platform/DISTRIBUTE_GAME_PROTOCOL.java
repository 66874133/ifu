package com.funnel.game.majhong.server.protocol.platform;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;
import com.funnel.game.majhong.server.protocol.IPlatformProtocolAdapter;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.game.TestMap;
import com.funnel.game.majhong.server.protocol.game.login.CertCenter;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class DISTRIBUTE_GAME_PROTOCOL implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	public void work(IoSession session, PlatformProtocol t) {
		logger.info("有客户端请求DISTRIBUTE_GAME...");

		String sessionId = t.getKey();

		String server = "120.25.228.198";
		String port = "11234";
		long cert = CertCenter.updateCert();

		if (!TestMap.sessionIdToName.containsKey(sessionId)) {
			logger.info("非法的会话 sessionId=" + sessionId);
			return;
		}

		TestMap.certToSessionId.put("" + cert, sessionId);
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();
		String data = "{\"server\":\"" + server + "\",\"port\":\"" + port
				+ "\"" + ",\"cert\":\"" + cert + "\"" + "}";
		clientTransportProtocol.setData(data);
		clientTransportProtocol.setRet(0);
		clientTransportProtocol.setStatus(1);
		clientTransportProtocol.setVersion(System.currentTimeMillis());

		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1002;
	}

}

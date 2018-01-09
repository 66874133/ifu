package com.funnel.game.majhong.server.protocol.platform;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;
import com.funnel.game.majhong.server.protocol.IPlatformProtocolAdapter;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.game.login.SessionManager;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class READY_GAME_Protocol implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	public void work(IoSession session, PlatformProtocol bs) {
		logger.info("有客户端请求派发执行对象...");

		String sessionId = bs.getKey();
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();

		String name = "测试用户名";
		String data = "{" + "\"sessionId\":\"" + sessionId + "\",\"name\":\""
				+ name + "\"" + "}";

		SessionManager.getMajhongSession(sessionId).setGameReady(true);
		clientTransportProtocol.setData(data);
		clientTransportProtocol.setRet(0);
		clientTransportProtocol.setStatus(0);
		clientTransportProtocol.setVersion(System.currentTimeMillis());

		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1007;
	}
}

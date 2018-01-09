package com.funnel.game.majhong.server.protocol.platform;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;
import com.funnel.game.majhong.server.protocol.IPlatformProtocolAdapter;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.game.TestMap;
import com.funnel.game.majhong.server.protocol.game.login.Identifier;
import com.funnel.game.majhong.server.protocol.game.login.MajhongSession;
import com.funnel.game.majhong.server.protocol.game.login.SessionManager;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class CONNECT_GAME_PROTOCOL implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	private Identifier identifier = new Identifier();


	public void work(IoSession session, PlatformProtocol t) {
		logger.info("有客户端请求LOGIN_GAME...");

		String nameAndPwd = t.getKey();

		String validNum = "";

		MajhongSession majhongSession = SessionManager.newSession();

		TestMap.sessionIdToName.put(majhongSession.getSessionId(), nameAndPwd);
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();

		clientTransportProtocol.setRet(0);

		if ("abc123".equals(nameAndPwd)) {
			clientTransportProtocol.setStatus(1);
		} else {
			validNum = identifier.make(nameAndPwd);
			clientTransportProtocol.setStatus(-1);
		}

		String data = "{" + "\"sessionId\":\"" + majhongSession.getSessionId()
				+ "\",\"validNum\":\"" + validNum + "\"" + "}";
		clientTransportProtocol.setData(data);

		clientTransportProtocol.setVersion(System.currentTimeMillis());

		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1001;
	}

}

package com.funnel.game.majhong.server.protocol.platform;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;
import com.funnel.game.majhong.server.protocol.IPlatformProtocolAdapter;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.game.TestMap;

/**
 * 
 * 获取可分发给客户端的任务
 * 
 * @author Jerry
 * 
 */
public class LOGIN_GAME_Protocol implements IPlatformProtocolAdapter {

	private Logger logger = Logger.getLogger(this.getClass());

	public void work(IoSession session, PlatformProtocol t) {
		logger.info("有客户端请求LOGIN_GAME...");

		String cert = t.getKey();
		ClientTransportProtocol clientTransportProtocol = new ClientTransportProtocol();
	
		String message = "";
		String sessionId = TestMap.certToSessionId.get(cert);
		if(null != sessionId)
		{
			clientTransportProtocol.setStatus(0);
		}
		else
		{
			message = "非法的通信证书";
			clientTransportProtocol.setStatus(1);
		}
		
		clientTransportProtocol.setRet(0);
		clientTransportProtocol.setVersion(System.currentTimeMillis());
		String data = "{" + "\"sessionId\":\"" + sessionId
				+ "\",\"message\":\"" + message + "\"" + "}";
		clientTransportProtocol.setData(data);
		
		
		logger.info("有客户端请求START_GAME clientTransportProtocol="
				+ clientTransportProtocol);
		session.write(clientTransportProtocol);

	}

	public int getMessageCode() {
		return 1003;
	}

}

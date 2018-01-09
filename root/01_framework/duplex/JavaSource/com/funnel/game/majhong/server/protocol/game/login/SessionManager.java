package com.funnel.game.majhong.server.protocol.game.login;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

	private static Map<String, MajhongSession> sessionIdToSession = new HashMap<String, MajhongSession>();

	public static MajhongSession newSession() {
		MajhongSession majhongSession = new MajhongSession();
		majhongSession.setSessionId(newSessionId());

		sessionIdToSession.put(majhongSession.getSessionId(), majhongSession);
		return majhongSession;
	}

	public static MajhongSession getMajhongSession(String sessionId) {
		return sessionIdToSession.get(sessionId);
	}

	private static String newSessionId() {
		return "" + System.currentTimeMillis();
	}

}

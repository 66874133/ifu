package com.funnel.game.majhong.server.protocol.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.funnel.game.majhong.server.protocol.game.login.MajhongSession;

public class TestMap {

	
	public static Map<String, String> sessionIdToName = new HashMap<String, String>();
	
	public static Map<String, String> certToSessionId = new HashMap<String, String>();

	
	public static Map<String, List<MajhongSession>> roomKeyToSessions = new HashMap<String, List<MajhongSession>>();

}

package com.funnel.game.majhong.server.protocol.game.login;

import java.util.HashMap;
import java.util.Map;

public class Identifier {

	private Map<String, String> keyToCode = new HashMap<String, String>();

	public String make(String key) {
		keyToCode.put(key, "abc");
		return keyToCode.get(key);
	}

	public boolean check(String key, String value) {
		return keyToCode.get(key).equalsIgnoreCase(value);
	}
}

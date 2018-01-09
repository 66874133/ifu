package com.funnel.game.majhong.server.protocol.game.login;

import java.util.ArrayList;
import java.util.List;

public class CertCenter {

	private static List<Long> certs = new ArrayList<Long>();

	public static long updateCert() {
		long t = (long) (System.currentTimeMillis() / (100 * Math.random()));

		certs.add(t);
		
		return t;
	}

	public static boolean check(long s) {
		return certs.contains(s);
	}

	public static void main(String[] args) {

		System.out.println(updateCert());
	}
}

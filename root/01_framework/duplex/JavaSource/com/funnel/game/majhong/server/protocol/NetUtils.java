package com.funnel.game.majhong.server.protocol;

public class NetUtils {

	
	// 16位
	public static short netShort(short value) {
		short tmp;
		tmp = (short) (((value << 8) & 0xFF00) | ((value >> 8) & 0xFF));
		return tmp;
	}

	// 32位
	public static int netInt(int value) {
		int tmp;
		tmp = (value << 24) | ((value << 8) & 0xFF0000)
				| ((value >> 8) & 0xFF00) | ((value >> 24) & 0xFF);

		return tmp;
	}
	
	// 32位
		public static int localInt(int value) {
			int tmp;
			tmp =  ((value >> 24) & 0xFF)| ((value >> 8) & 0xFF00) |((value << 8) & 0xFF0000)|(value << 24);
			return tmp;
		}

	// 64位
	public static long netLong(long value) {
		long tmp;
		tmp = (((long) netInt((int) (value & 0x0FFFFFFFFL)) << 32) & 0xFFFFFFFF00000000L)
				| ((long) netInt((int) ((value >> 32) & 0x0FFFFFFFFL)));
		return tmp;
	}
	
	public static long localLong(long value) {
		long tmp;
		tmp = ((long) netInt((int) ((value >> 32) & 0x0FFFFFFFFL)))|(((long) netInt((int) (value & 0x0FFFFFFFFL)) << 32) & 0xFFFFFFFF00000000L);
	
		return tmp;
	}
	public static void main(String[] args) {
		int i = netInt(3);
		System.out.println(i);
		System.out.println(localInt(i));
		
		long s = System.currentTimeMillis();
		System.out.println(s);
		 s = netLong(s);
		System.out.println(s);
		System.out.println(localLong(s));
	}
}

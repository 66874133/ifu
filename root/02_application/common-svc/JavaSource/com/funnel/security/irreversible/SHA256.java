package com.funnel.security.irreversible;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class SHA256 {

	private MessageDigest messageDigest;

	public SHA256() throws Exception {
		messageDigest = MessageDigest.getInstance("SHA-256");
	}

	/**
	 * 利用java原生的摘要实现SHA256加密
	 * 
	 * @param str
	 *            加密后的报文
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String decrypt(String str) throws UnsupportedEncodingException {

		String encodeStr = "";

		messageDigest.update(str.getBytes("UTF-8"));
		encodeStr = byte2Hex(messageDigest.digest());

		return encodeStr;
	}

	/**
	 * 将byte转为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String str = "klkffdfdfd";
			SHA256 sha256 = new SHA256();
			
			String str2 = sha256.decrypt(str);
			
			System.out.println("原文:"+str+",密文:"+str2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

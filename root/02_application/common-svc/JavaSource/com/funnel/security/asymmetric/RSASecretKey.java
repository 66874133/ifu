package com.funnel.security.asymmetric;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class RSASecretKey {

	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	private int keyLength = 1024;

	private Map<String, Object> keyMap;

	public RSASecretKey() throws Exception {
		this(1024);
	}

	public RSASecretKey(int keyLength) throws Exception {
		this.keyLength = keyLength;
		keyMap = initKey();

	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(keyLength);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public String getPrivateKey() throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return RSA.encryptBASE64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public String getPublicKey() throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return RSA.encryptBASE64(key.getEncoded());
	}

}

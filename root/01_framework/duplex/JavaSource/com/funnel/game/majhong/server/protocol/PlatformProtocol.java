package com.funnel.game.majhong.server.protocol;

import java.io.Serializable;

public class PlatformProtocol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1017324208002537083L;

	private int messageCode;

	private int operation;

	private int retry;

	private String key;

	private long t;

	/**
	 * 版本号
	 */
	private long version;

	public int getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getT() {
		return t;
	}

	public void setT(long t) {
		this.t = t;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "PlatformProtocol [messageCode=" + messageCode + ", operation="
				+ operation + ", retry=" + retry + ", key=" + key + ", t=" + t
				+ ", version=" + version + "]";
	}

}

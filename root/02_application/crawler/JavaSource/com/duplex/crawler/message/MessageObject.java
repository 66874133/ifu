package com.duplex.crawler.message;

import java.io.Serializable;

public class MessageObject implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -931516291204080828L;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	private Object object;
}

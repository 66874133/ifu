package com.duplex.crawler;

import java.util.List;

import com.duplex.crawler.IExecuteObject;

public class ExecuteResult {

	public static int CODE_OK = 0;
	public static int CODE_FAILED = -1;
	private int code;
	private String message;

	private List<IExecuteObject> newList;
	public ExecuteResult(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<IExecuteObject> getNewList() {
		return newList;
	}

	public void setNewList(List<IExecuteObject> newList) {
		this.newList = newList;
	}
}

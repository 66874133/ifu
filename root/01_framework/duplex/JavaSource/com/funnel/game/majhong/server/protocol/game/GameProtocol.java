package com.funnel.game.majhong.server.protocol.game;

public class GameProtocol {

	private int operation;
	
	private String userId;

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

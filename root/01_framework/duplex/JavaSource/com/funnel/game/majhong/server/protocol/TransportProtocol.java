package com.funnel.game.majhong.server.protocol;

public class TransportProtocol {

	private int ack;

	private int broadcast;

	public int getAck() {
		return ack;
	}

	public void setAck(int ack) {
		this.ack = ack;
	}

	public int getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(int broadcast) {
		this.broadcast = broadcast;
	}

	@Override
	public String toString() {
		return "TransportProtocol [ack=" + ack + ", broadcast=" + broadcast
				+ "]";
	}

}

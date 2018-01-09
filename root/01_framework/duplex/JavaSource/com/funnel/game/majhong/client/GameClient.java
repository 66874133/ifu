package com.funnel.game.majhong.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;

import com.duplex.frame.handler.ObjectProtocolHandler;
import com.duplex.frame.mina.client.AbstractMinaTCPClient;
import com.duplex.frame.mina.client.ByteArrayMinaTCPClient;
import com.funnel.game.majhong.client.protocol.Test_Protocol_Handler;
import com.funnel.game.majhong.server.protocol.PlatformProtocol;
import com.funnel.game.majhong.server.protocol.TransportProtocol;

public class GameClient {

	private static Logger logger = Logger.getLogger(GameClient.class);
	private AbstractMinaTCPClient minaTCPClient;

	private String strAddress;

	public GameClient(String strAddress, IoHandlerAdapter ioHandlerAdapter) {
		this.strAddress = strAddress;
		minaTCPClient = new ByteArrayMinaTCPClient(strAddress);
		minaTCPClient.setHandler(ioHandlerAdapter);
	}

	public void start() {
		minaTCPClient.connect(strAddress);

		TransportProtocol protocol = new TransportProtocol();
		protocol.setAck(99);
		protocol.setBroadcast(100);

		PlatformProtocol stu = new PlatformProtocol();
		stu.setMessageCode(1);
		stu.setOperation(112);
		stu.setKey("abc");
		stu.setRetry(3);
		stu.setT(System.currentTimeMillis());

		ByteArrayOutputStream boutput = new ByteArrayOutputStream();
		DataOutputStream doutput = new DataOutputStream(boutput);

		try {
			doutput.writeInt(protocol.getAck());
			doutput.writeInt(protocol.getBroadcast());
			doutput.writeInt(stu.getMessageCode());
			doutput.writeInt(stu.getOperation());

			int size = stu.getKey().getBytes().length;
			doutput.writeInt(size);
			doutput.write(stu.getKey().getBytes());
			doutput.writeInt(stu.getRetry());
			doutput.writeLong(stu.getT());

			byte[] bs = boutput.toByteArray();

			ByteArrayOutputStream boutput2 = new ByteArrayOutputStream();
			DataOutputStream doutput2 = new DataOutputStream(boutput2);
			doutput2.writeInt(bs.length);
			doutput2.write(bs);

			minaTCPClient.send(IoBuffer.wrap(boutput2.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ObjectProtocolHandler handler = new ObjectProtocolHandler(
				new Test_Protocol_Handler());

		GameClient client = new GameClient("127.0.0.1:11234", handler);

		client.start();
	}

}

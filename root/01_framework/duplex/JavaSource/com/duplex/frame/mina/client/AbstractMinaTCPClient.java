package com.duplex.frame.mina.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public abstract class AbstractMinaTCPClient {

	protected IoSession session;

	protected String name;

	private NioSocketConnector connector = new NioSocketConnector();

	public AbstractMinaTCPClient(String name) {
		this.name = name;
		initConnector(connector);
	}

	protected abstract void initConnector(NioSocketConnector connector);

	public void setHandler(IoHandlerAdapter ioHandlerAdapter) {
		connector.setHandler(ioHandlerAdapter);
	}

	public boolean connect(SocketAddress address) {

		ConnectFuture future = connector.connect(address);

		future.awaitUninterruptibly();
		if (!future.isConnected()) {
			return false;
		}
		session = future.getSession();
		return true;
	}

	public boolean connect(String strAddress) {

		SocketAddress address = parseSocketAddress(strAddress);
		return connect(address);
	}

	public void send(Object obj) {
		session.write(obj);
	}

	public void login() {
		session.write("LOGIN " + name);
	}

	public void broadcast(String message) {
		session.write("BROADCAST " + message);
	}

	public void quit() {
		if (session != null) {
			if (session.isConnected()) {
				session.write("QUIT");
				// Wait until the chat ends.
				session.getCloseFuture().awaitUninterruptibly();
			}
			session.close(true);
		}
	}

	/**
	 * 解析地址
	 * 
	 * @param s
	 * @return
	 */
	private SocketAddress parseSocketAddress(String s) {
		s = s.trim();
		int colonIndex = s.indexOf(":");
		if (colonIndex > 0) {
			String host = s.substring(0, colonIndex);
			int port = parsePort(s.substring(colonIndex + 1));
			return new InetSocketAddress(host, port);
		} else {
			int port = parsePort(s.substring(colonIndex + 1));
			return new InetSocketAddress(port);
		}
	}

	private static int parsePort(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Illegal port number: " + s);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

}

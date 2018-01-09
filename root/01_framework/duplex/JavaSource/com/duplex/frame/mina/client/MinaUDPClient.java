/**
 * 
 */
package com.duplex.frame.mina.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

/**
 * @author jiangbo
 * 
 */
public class MinaUDPClient {

	private IoSession session;

	private NioDatagramConnector connector = new NioDatagramConnector();

	public boolean connect(SocketAddress address) {

		IoFilter CODEC_FILTER = new ProtocolCodecFilter(
				new TextLineCodecFactory());
		IoFilter OBJECT_FILTER = new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory());

		// connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
		// connector.getFilterChain().addLast("codec", CODEC_FILTER);
		connector.getFilterChain().addLast("object", OBJECT_FILTER);

		ConnectFuture future = connector.connect(address);

		future.awaitUninterruptibly();
		if (!future.isConnected()) {
			return false;
		}
		session = future.getSession();
		return true;
	}

	public void setsetHandler(IoHandlerAdapter ioHandlerAdapter) {
		connector.setHandler(ioHandlerAdapter);
	}

	public void login(String name) {
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
	private static SocketAddress parseSocketAddress(String s) {
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
}

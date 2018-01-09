/**
 * 
 */
package com.duplex.frame.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

/**
 * @author jiangbo
 * 
 */
public class MinaUDPServer {

	private NioDatagramAcceptor acceptor = new NioDatagramAcceptor();

	private int port;

	public MinaUDPServer(int port) {

		this.port = port;
		IoFilter CODEC_FILTER = new ProtocolCodecFilter(
				new TextLineCodecFactory());
		IoFilter OBJECT_FILTER = new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory());
		// acceptor.getFilterChain().addLast("codec", CODEC_FILTER);
		acceptor.getFilterChain().addLast("object", OBJECT_FILTER);

		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));

	}

	public void start() {
		try {
			acceptor.bind();
			System.out.println("MinaServer is start port :" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

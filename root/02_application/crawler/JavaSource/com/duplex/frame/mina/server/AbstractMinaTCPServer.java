package com.duplex.frame.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public abstract class AbstractMinaTCPServer {

	private Logger logger = Logger.getLogger(this.getClass());

	protected NioSocketAcceptor acceptor;

	public AbstractMinaTCPServer(int port) {
		acceptor = new NioSocketAcceptor();

		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));

		logger.info("MinaTcpServer port :" + port);

	}

	public void setHandler(IoHandlerAdapter ioHandlerAdapter) {
		acceptor.setHandler(ioHandlerAdapter);
	}

	protected abstract void initAcceptor(NioSocketAcceptor acceptor);

	public NioSocketAcceptor getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(NioSocketAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	public void start() {
		try {
			initAcceptor(acceptor);
			acceptor.bind();
			logger.info("MinaTcpServer is start !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package com.funnel.game.majhong.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.duplex.frame.ByteArrayTransfer.Mode;
import com.duplex.frame.mina.server.ByteArrayMinaTCPServer;

/**
 * ServerJobAgent 任务管理池
 * 
 * @author Jerry
 * 
 */
public class GameServerAgent {

	private Logger logger = Logger.getLogger(this.getClass());

	private ByteArrayMinaTCPServer minaTCPServer;

	public GameServerAgent(int port, IoHandlerAdapter ioHandlerAdapter,
			Mode transfer) {
		minaTCPServer = new ByteArrayMinaTCPServer(port);
		minaTCPServer.setTransfer(transfer);
		minaTCPServer.setHandler(ioHandlerAdapter);
		minaTCPServer
				.getAcceptor()
				.getFilterChain()
				.addLast(
						"codec",
						new ProtocolCodecFilter(new PureByteArrayCodecFactory(
								transfer)));
	}

	public void start() {
		logger.info("start...");
		minaTCPServer.start();
		ExcuteThreadPoolDaemonThread daemonThread = new ExcuteThreadPoolDaemonThread();
		daemonThread.start();

		logger.info("started!");
	}

	private class ExcuteThreadPoolDaemonThread extends Thread {

		@Override
		public void run() {
			while (true) {

				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

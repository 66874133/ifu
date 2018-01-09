/**
 * 
 */
package com.duplex.frame.mina.server;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.duplex.frame.ByteArrayTransfer.Mode;

/**
 * @author jiangbo
 * 
 */
public class ByteArrayMinaTCPServer extends AbstractMinaTCPServer {

	private Mode transfer;

	public ByteArrayMinaTCPServer(int port) {
		super(port);
	}

	@Override
	protected void initAcceptor(NioSocketAcceptor acceptor) {

		IoFilter CODEC_FILTER = new ProtocolCodecFilter(
				new TextLineCodecFactory());
		IoFilter OBJECT_FILTER = new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory());
		acceptor.getSessionConfig().setReadBufferSize(1024);

		acceptor.getSessionConfig().setReceiveBufferSize(1024);
		acceptor.getSessionConfig().setReadBufferSize(1024);
		acceptor.getSessionConfig().setMaxReadBufferSize(8 * 1024);

		acceptor.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, 10); // 写
																				// 通道均在10
																				// 秒内无任何操作就进入空闲状态
		acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 10); // 读
																				// 通道均在10
																				// 秒内无任何操作就进入空闲状态
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10); // 读写
																			// 通道均在10
																			// 秒内无任何操作就进入空闲状态
		// acceptor.getFilterChain().addLast("codec", CODEC_FILTER);
		// acceptor.getFilterChain().addLast("object", OBJECT_FILTER);
		// acceptor.setHandler(new TimeServerProtocolHandler());

	}

	public Mode getTransfer() {
		return transfer;
	}

	public void setTransfer(Mode transfer) {
		this.transfer = transfer;
	}

}

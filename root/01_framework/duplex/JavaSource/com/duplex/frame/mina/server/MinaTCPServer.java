/**
 * 
 */
package com.duplex.frame.mina.server;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


/**
 * @author jiangbo
 * 
 */
public class MinaTCPServer extends AbstractMinaTCPServer {

	public MinaTCPServer(int port) {
		super(port);
	}

	@Override
	protected void initAcceptor(NioSocketAcceptor acceptor) {
		IoFilter CODEC_FILTER = new ProtocolCodecFilter(
				new TextLineCodecFactory());
		IoFilter OBJECT_FILTER = new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory());
		// acceptor.getFilterChain().addLast("codec", CODEC_FILTER);
		acceptor.getFilterChain().addLast("object", OBJECT_FILTER);
		// acceptor.setHandler(new TimeServerProtocolHandler());

	}

}

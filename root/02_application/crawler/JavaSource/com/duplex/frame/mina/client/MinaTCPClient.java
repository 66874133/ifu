/**
 * 
 */
package com.duplex.frame.mina.client;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * @author jiangbo
 * 
 */
public class MinaTCPClient extends AbstractMinaTCPClient{

	
	public MinaTCPClient(String name) {
		super(name);
	}

	@Override
	protected void initConnector(NioSocketConnector connector) {
		IoFilter CODEC_FILTER = new ProtocolCodecFilter(
				new TextLineCodecFactory());
		IoFilter OBJECT_FILTER = new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory());

		// connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
		// connector.getFilterChain().addLast("codec", CODEC_FILTER);
		connector.getFilterChain().addLast("object", OBJECT_FILTER);
		
	}



}

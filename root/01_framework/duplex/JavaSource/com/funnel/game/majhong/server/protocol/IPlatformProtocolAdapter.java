package com.funnel.game.majhong.server.protocol;

import com.duplex.frame.handler.response.IMessageResponseHandler;

public interface IPlatformProtocolAdapter extends IMessageResponseHandler<PlatformProtocol>{

	public int getMessageCode();
}

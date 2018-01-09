package com.funnel.svc.handlers.chain;

import com.funnel.svc.handlers.Handler;


/**
 * 默认处理器链对象创建者实现
 * 
 * @author wanghua
 * @since 2012-07-18
 * 
 */
public class DefaultHandlerChainCreater implements HandlerChainCreator {
	private Handler[] handlers;

	/**
	 * 创建处理器链对象
	 * 
	 * @author wanghua
	 * @since 2012-07-18
	 * 
	 */
	@Override
	public HandlerChain createHandlerChain() {
		DefaultHandlerChain handlerChain = new DefaultHandlerChain(handlers);
		return handlerChain;
	}

	public Handler[] getHandlers() {
		return handlers;
	}

	public void setHandlers(Handler[] handlers) {
		this.handlers = handlers;
	}
}

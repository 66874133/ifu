package com.funnel.svc.handlers.chain;


/**
 * 处理器链对象创建者
 * 
 * @author wanghua
 * @since 2012-07-18
 * 
 */
public interface HandlerChainCreator {
	/**
	 * 创建处理器链对象
	 * 
	 * @author wanghua
	 * @since 2012-07-18
	 * 
	 */
	public HandlerChain createHandlerChain();
}

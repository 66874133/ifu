package com.funnel.svc.handlers.chain;

import com.funnel.svc.SvcContext;

/**
 * 处理器链
 * 
 * @author wanghua
 * @since 2012-07-18
 * 
 */
public interface HandlerChain {
	/**
	 * 执行处理器
	 * 
	 * @author wanghua
	 * @since 2012-07-18
	 * 
	 */
	public void doHandler(SvcContext context);
}

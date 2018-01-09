package com.funnel.svc.handlers;

import com.funnel.svc.SvcContext;
import com.funnel.svc.handlers.chain.HandlerChain;

/**
 * 处理器
 * 
 * @author wanghua
 * @since 2012-07-18
 * 
 */
public interface Handler {
	/**
	 * 执行处理
	 * 
	 * @author wanghua
	 * @param context
	 *            处理器上下文对象,chain处理器链对象
	 * 
	 */
	public void handle(SvcContext context, HandlerChain chain);
}

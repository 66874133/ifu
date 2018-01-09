package com.funnel.svc.handlers.chain;

import com.funnel.svc.SvcContext;
import com.funnel.svc.handlers.Handler;

/**
 * 默认处理器链实现
 * 
 * @author wanghua
 * @since 2012-07-18
 * 
 */
public class DefaultHandlerChain implements HandlerChain {
	// 当前链中包含的处理器
	private Handler[] handlers;
	// 当请执行到的处理器index
	private int pos = 0;

	public DefaultHandlerChain(Handler[] handlers) {
		this.handlers = handlers;
	}

	/**
	 * 执行处理器，根据pos决定执行哪个处理器
	 * 
	 * @author wanghua
	 * @since 2012-07-18
	 * 
	 */
	@Override
	public void doHandler(SvcContext context) {
		if (null == handlers || handlers.length == 0) {
			return;
		}
		if (pos < handlers.length) {
			int currPos = pos;
			pos = pos + 1;
			handlers[currPos].handle(context,this);
		}
	}

}

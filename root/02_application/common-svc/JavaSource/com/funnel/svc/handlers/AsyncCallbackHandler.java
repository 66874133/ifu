package com.funnel.svc.handlers;

import org.apache.log4j.Logger;

import com.funnel.svc.SvcContext;
import com.funnel.svc.handlers.chain.HandlerChain;
import com.funnel.svc.util.CallbackUtil;

public class AsyncCallbackHandler implements Handler {
	protected final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void handle(SvcContext context, HandlerChain chain) {
		long begin = System.currentTimeMillis();
		callback(context);
		if ((System.currentTimeMillis() - begin) > 30000) {
			logger.info("服务svcCode:" + context.getSvcCode() + " seq:"
					+ context.getSeqno() + ",执行成功回调方法时间："
					+ (System.currentTimeMillis() - begin) + "ms");
		}
		chain.doHandler(context);
	}

	public void callback(SvcContext context) {
		CallbackUtil.callback(true, context);
	}

}

package com.funnel.svc.handlers;

import org.apache.log4j.Logger;

import com.funnel.svc.SvcContext;
import com.funnel.svc.handlers.chain.HandlerChain;
import com.funnel.svc.util.CallbackUtil;

public class AsyncErrorCallbackHandler implements Handler {
	protected final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void handle(SvcContext context, HandlerChain chain) {
		long begin = System.currentTimeMillis();
		try {
			callback(context);
		} catch (Exception e1) {
			logger.error("执行失败回调失败, svcCode:" + context.getSvcCode() + " seq:"
					+ context.getSeqno(), e1);
		}
		if ((System.currentTimeMillis() - begin) > 30000) {
			logger.info("服务svcCode:" + context.getSvcCode() + " seq:"
					+ context.getSeqno() + ",执行失败回调方法时间："
					+ (System.currentTimeMillis() - begin) + "ms");
		}
		chain.doHandler(context);
	}

	public void callback(SvcContext context) {
		CallbackUtil.callback(false, context);
	}

}

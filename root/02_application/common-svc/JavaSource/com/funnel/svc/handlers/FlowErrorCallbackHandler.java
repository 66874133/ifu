package com.funnel.svc.handlers;

import com.funnel.svc.SvcContext;
import com.funnel.svc.flow.Flow;
import com.funnel.svc.handlers.chain.HandlerChain;

/**
 * 流程失败处理器
 * 
 * @author wanghua4
 * 
 */
public class FlowErrorCallbackHandler implements Handler {

	@Override
	public void handle(SvcContext context, HandlerChain chain) {
		Flow flow = (Flow) context.getBelongFlowSvc();
		if (null != flow) {

		}
		chain.doHandler(context);
	}
}
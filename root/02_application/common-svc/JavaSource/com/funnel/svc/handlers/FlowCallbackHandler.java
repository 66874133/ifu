
package com.funnel.svc.handlers;

import com.funnel.svc.SvcContext;
import com.funnel.svc.flow.Flow;
import com.funnel.svc.handlers.chain.HandlerChain;

/**
 * 当每个流程节点执行完后，回调该处理器，实现流程的流转
 * 
 * @author wanghua4
 */
public class FlowCallbackHandler implements Handler
{

    @Override
    public void handle(SvcContext context, HandlerChain chain)
    {
        Flow flow = (Flow)context.getBelongFlowSvc();
        if ((!context.isFlowPasue()) && null != flow)
        {
            ((Flow)context.getBelongFlowSvc()).excuteNode(context);
        }
        chain.doHandler(context);
    }
}

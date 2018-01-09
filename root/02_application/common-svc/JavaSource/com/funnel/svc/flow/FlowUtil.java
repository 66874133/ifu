package com.funnel.svc.flow;

import org.apache.log4j.Logger;

import com.funnel.svc.Service;
import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;
import com.funnel.svc.util.ObjectCloneUtil;
import com.funnel.svc.util.ServiceUtil;

public class FlowUtil {
	protected static final Logger logger = Logger.getLogger(FlowUtil.class.getClass());

	public static void startFlow(Flow flowSvc, SvcContext context) {
		if (flowSvc.getNodes().size() == 0) {
			throw new SvcException("该流程未配置任何节点");
		}
		try {
			SvcContext flowContext = (SvcContext) ObjectCloneUtil
					.deepClone(context);
			initContext(flowSvc, context, flowContext);
			excuteNode(flowSvc, flowContext);
			context.setResponseData(flowContext.getResponseData());
		} catch (Exception e) {
			throw new SvcException("执行流程:" + context.getSvcCode() + " 失败", e);
		}
	}

	private static void initContext(Flow flowSvc, SvcContext context,
			SvcContext newContext) {
		newContext.setBelongFlowSvc((Service) flowSvc);
		newContext.setNextNodeIndex(0);
		newContext.setParent(context);
	}

	public static void excuteNode(Flow flowSvc, SvcContext context) {
		Service parentFlowSvc = context.getParent().getBelongFlowSvc();
		if (context.isFinish()) {
			logger.debug((parentFlowSvc == null ? "" : "父流程:"
					+ parentFlowSvc.getSvcCode())
					+ " 当前流程:"
					+ context.getBelongFlowSvc().getSvcCode()
					+ " 节点个数:"
					+ flowSvc.getNodes().size()
					+ " 第:"
					+ context.getNextNodeIndex()
					+ " 个节点:"
					+ context.getSvcCode() + " 执行完成后，上下文状态被业务节点设置为了结束，流程终止");
			return;
		}
		if (context.getNextNodeIndex() == flowSvc.getNodes().size()) {
			logger.debug((parentFlowSvc == null ? "" : "父流程:"
					+ parentFlowSvc.getSvcCode())
					+ " 当前流程:"
					+ context.getBelongFlowSvc().getSvcCode()
					+ " 节点个数:" + flowSvc.getNodes().size() + " 全部执行完成");
			context.setFinish(true);
			return;
		}
		String nextSvc = flowSvc.getNodes().get(context.getNextNodeIndex());
		try {
			context.moveToNext(nextSvc);
			logger.debug((parentFlowSvc == null ? "" : "父流程:"
					+ parentFlowSvc.getSvcCode())
					+ " 当前流程:"
					+ context.getBelongFlowSvc().getSvcCode()
					+ " 执行第:" + context.getNextNodeIndex() + " 个节点:" + nextSvc);
			context.getRequestData().putAll(context.getResponseData());
			callNodeSvc(context);
		} catch (Exception e) {
			throw new SvcException((parentFlowSvc == null ? "" : "父流程:"
					+ parentFlowSvc.getSvcCode())
					+ " 当前流程:"
					+ context.getBelongFlowSvc().getSvcCode()
					+ "调用下一步节点:" + nextSvc + "失败", e);
		}

	}

	private static void callNodeSvc(SvcContext context) {
		Service service = ServiceUtil.getSvcByCode(context.getSvcCode());
		if (null == service) {
			throw new SvcException("根据服务码：" + context.getSvcCode()
					+ " 未查找到对应的服务对象");
		}
		service.request(context);

	}
}

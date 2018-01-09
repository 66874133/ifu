package com.funnel.svc.flow;

import java.util.ArrayList;
import java.util.List;

import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

/**
 * 同步流程服务
 * 
 * @author wanghua4
 * 
 */
public class SyncFlowService extends SyncService implements Flow {
	private List<String> nodes = new ArrayList<String>();

	// private Service beforExcuteNodeSvc;

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

	// public Service getBeforExcuteNodeSvc() {
	// return beforExcuteNodeSvc;
	// }
	//
	// public void setBeforExcuteNodeSvc(Service beforExcuteNodeSvc) {
	// this.beforExcuteNodeSvc = beforExcuteNodeSvc;
	// }

	@Override
	public void process(SvcContext context) {
		FlowUtil.startFlow(this, context);

	}

	public void excuteNode(SvcContext context) {
		// if (null != beforExcuteNodeSvc) {
		// beforExcuteNodeSvc.request(context);
		// }
		FlowUtil.excuteNode(this, context);
	}

}

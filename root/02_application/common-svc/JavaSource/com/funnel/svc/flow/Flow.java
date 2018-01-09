package com.funnel.svc.flow;

import java.util.List;

import com.funnel.svc.SvcContext;

/**
 * 流程接口
 * 
 * @author wanghua4
 * 
 */
public interface Flow {
	public List<String> getNodes();

	/**
	 * 开始执行节点
	 * 
	 * @param context
	 */
	public void excuteNode(SvcContext context);
}

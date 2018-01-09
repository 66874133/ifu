package com.funnel.svc;

import net.minidev.json.JSONObject;

import com.funnel.svc.util.JsonUtil;

/**
 * 服务上下文
 * 
 * @author wanghua4
 * 
 */
public class SvcContext implements Context {
	// 执行该请求的服务器名称
	private String serverName;
	// 序列号
	private String seqno;
	// 目标服务码
	private String svcCode;
	// 请求数据对象
	private JSONObject requestData;
	// 响应数据对象,主要用于同步消息
	private JSONObject responseData = new JSONObject();
	// 失败异常
	private SvcException failException;

	// 以下为流程服务提供支持的属性
	// 所属流程
	private Service belongFlowSvc;
	// 父上下文，用于嵌套流程时
	private SvcContext parent;
	// 流程是否结束
	private boolean finish;

    private boolean isFlowPasue = false;
	// 当前需要执行的节点
	private int nextNodeIndex = 0;
	
	private byte[] input;

	public String getSvcCode() {
		return svcCode;
	}

	public void setSvcCode(String svcCode) {
		this.svcCode = svcCode;
	}

	public JSONObject getRequestData() {
		return requestData;
	}

	public void setRequestData(JSONObject requestData) {
		this.requestData = requestData;

	}

	public JSONObject getResponseData() {
		return responseData;
	}

	public void setResponseData(JSONObject responseData) {
		this.responseData = responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = JsonUtil.toJSONObject(responseData);
	}

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public SvcException getFailException() {
		return failException;
	}

	public void setFailException(SvcException failException) {
		this.failException = failException;
	}

	public SvcContext getParent() {
		return parent;
	}

	public void setParent(SvcContext parent) {
		this.parent = parent;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public Service getBelongFlowSvc() {
		return belongFlowSvc;
	}

	public void setBelongFlowSvc(Service belongFlowSvc) {
		this.belongFlowSvc = belongFlowSvc;
	}

	public int getNextNodeIndex() {
		return nextNodeIndex;
	}

	public void setNextNodeIndex(int nextNodeIndex) {
		this.nextNodeIndex = nextNodeIndex;
	}

	public void moveToNext(String nextSvcCode) {
		this.setSvcCode(nextSvcCode);
		this.nextNodeIndex++;
	}

    public boolean isFlowPasue()
    {
        return isFlowPasue;
    }

    public void setFlowPasue(boolean isFlowPasue)
    {
        this.isFlowPasue = isFlowPasue;
    }

	public byte[] getInput() {
		return input;
	}

	public void setInput(byte[] input) {
		this.input = input;
	}

}

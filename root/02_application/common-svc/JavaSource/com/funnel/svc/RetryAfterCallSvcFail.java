package com.funnel.svc;

import net.minidev.json.JSONObject;

/**
 * 当调用服务失败后，重试相关处理
 * 
 * @author wanghua4
 * 
 */
public interface RetryAfterCallSvcFail {
	/**
	 * 是否要重试 返回true执行重试
	 * 
	 * @param context
	 * @return
	 */
	public boolean isRetry(JSONObject retyParam);

	/**
	 * 执行重试，返回结果
	 * 
	 * @param retyParam
	 * @return
	 */
	public JSONObject doRety(JSONObject retyParam);
}

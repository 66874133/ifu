package com.funnel.svc;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

public class RetryAfterFlowControl implements RetryAfterCallSvcFail {
	protected final Logger logger = Logger.getLogger(this.getClass());

	private long sleepTime = 2000;

	public RetryAfterFlowControl() {

	}

	public RetryAfterFlowControl(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public boolean isRetry(JSONObject retyParam) {
		if (Constant.ERROR_CODE_THREAD_POOL_REFUSE_ERROR.equals(retyParam
				.get(Constant.REQ_RESPONSE_FAIL_CODE))
				|| Constant.ERROR_CODE_FLOW_CONTRAL_ERROR.equals(retyParam
						.get(Constant.REQ_RESPONSE_FAIL_CODE))) {
			return true;
		}
		return false;
	}

	@Override
	public JSONObject doRety(JSONObject retyParam) {
		try {
			logger.debug("服务被流控，sleep 2秒后重试,svcCode:"
					+ retyParam.get(Constant.MESSAGE_SVC_CODE));
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			logger.error("服务被流控，sleep 异常", e);
		}
		return ServiceCallUtil.callService((String) retyParam
				.get(Constant.MESSAGE_SVC_CODE), (JSONObject) retyParam
				.get(Constant.MESSAGE_REQ_DATA),
				(RetryAfterCallSvcFail) retyParam
						.get(Constant.MESSAGE_RETRY_OBJ));

	}

}

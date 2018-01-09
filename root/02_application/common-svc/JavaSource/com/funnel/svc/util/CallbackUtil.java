package com.funnel.svc.util;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import com.funnel.svc.Constant;
import com.funnel.svc.RetryAfterFlowControl;
import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcContext;

public class CallbackUtil {
	protected static final Logger logger = Logger.getLogger(CallbackUtil.class);

	public static void callback(boolean isSuccess, SvcContext context) {

		String callbackSvc = (String) ((JSONObject) context.getRequestData())
				.get("callback");
		if (!StringUtils.hasText(callbackSvc)) {
			return;
		}
		JSONObject callBackData = getCallBackData(isSuccess, context);

		// logger.debug("svcCode:" + context.getSvcCode() + " seq:"
		// + context.getSeqno() + " 执行回调,数据:"
		// + JsonUtil.toJSONString(callBackData));
		ServiceCallUtil.callService(callbackSvc, callBackData,
				new RetryAfterFlowControl());
	}

	public static JSONObject getCallBackData(boolean isSuccess,
			SvcContext context) {
		JSONObject callBackData = new JSONObject();
		if (null != context.getResponseData()) {
			callBackData = context.getResponseData();
		}
		callBackData.put(Constant.MESSAGE_SEQUENCENO_FIELD, context.getSeqno());
		if (isSuccess) {
			callBackData.put(Constant.REQ_RESPONSE_STATE_FIELD,
					Constant.REQ_RESPONSE_STATE_SUCCESS);
		} else {
			callBackData.put(Constant.REQ_RESPONSE_STATE_FIELD,
					Constant.REQ_RESPONSE_STATE_FAIL);
			callBackData.put(Constant.REQ_RESPONSE_FAIL_CODE, context
					.getFailException().getCode());
			callBackData.put(Constant.REQ_RESPONSE_STATE_FAIL_INFO, context
					.getFailException().getMessage());
			callBackData.put(Constant.MESSAGE_LAST_MSG_FIELD,
					context.getRequestData());
		}

		return callBackData;
	}
}

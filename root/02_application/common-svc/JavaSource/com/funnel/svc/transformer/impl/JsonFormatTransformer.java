package com.funnel.svc.transformer.impl;

import com.funnel.svc.Constant;
import com.funnel.svc.SvcException;
import com.funnel.svc.transformer.AbstractSvcMsgTransfomer;
import com.funnel.svc.util.JsonUtil;
import com.funnel.svc.util.StringUtils;

import net.minidev.json.JSONObject;

/**
 * Created by wanghua4 on 2014/8/18. json格式消息转换器 请求时，将json数据根据模板生成目标报文
 * 响应时，根据目标报文和模板转换成json格式
 * 
 * 响应是接收的是json格式的报文
 */
public class JsonFormatTransformer extends AbstractSvcMsgTransfomer {

	@Override
	public String getCode() {
		return "json";
	}

	@Override
	public JSONObject transformReqMsg(String reqMsg) {
		if (!StringUtils.hasText(reqMsg)) {
			return new JSONObject();
		}
		JSONObject reqObj = JsonUtil.toJSONObject(reqMsg);
		return reqObj;
	}

	@Override
	public String transformRespMsg(JSONObject respMsg) {
		if (null == respMsg) {
			respMsg = new JSONObject();
		}
		respMsg.put(Constant.REQ_RESPONSE_STATE_FIELD,
				Constant.REQ_RESPONSE_STATE_SUCCESS);
		return JsonUtil.toJSONString(respMsg);
	}

	@Override
	public String transformRespErrMsg(SvcException svcException) {
		JSONObject failJson = new JSONObject();
		failJson.put(Constant.REQ_RESPONSE_STATE_FIELD,
				Constant.REQ_RESPONSE_STATE_FAIL);
		if (null != svcException.getCode()) {
			failJson.put(Constant.REQ_RESPONSE_FAIL_CODE,
					svcException.getCode());
		}
		failJson.put(Constant.REQ_RESPONSE_STATE_FAIL_INFO,
				svcException.getMessage());
		return JsonUtil.toJSONString(failJson);
	}
}

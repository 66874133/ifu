package com.discover.crawler.service.transformer;

import net.minidev.json.JSONObject;

import com.funnel.svc.SvcException;
import com.funnel.svc.transformer.impl.JsonFormatTransformer;

public class HtmlDiscoverDataTransformer extends JsonFormatTransformer {
	public static final String TRANSFORMER_CODE = "HtmlDiscoverDataTransformer";

	public String getCode() {
		return TRANSFORMER_CODE;
	}

	public String transformRespMsg(JSONObject respMsg) {
		return respMsg.toJSONString();
	}

	public String transformRespErrMsg(SvcException svcException) {
		// TODO Auto-generated method stub
		return null;
	}

}

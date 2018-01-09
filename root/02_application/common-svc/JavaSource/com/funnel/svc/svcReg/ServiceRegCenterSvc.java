package com.funnel.svc.svcReg;

import net.minidev.json.JSONObject;

import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

public class ServiceRegCenterSvc extends SyncService {

	@Override
	public String getSvcCode() {
		return "serviceRegCenterSvc";
	}

	@Override
	public String getSvcDesc() {
		return "注册中心对外接口服务";
	}

	@Override
	public void process(SvcContext context) {
		String action = context.getRequestData().getAsString("action");
		if ("reload".equals(action)) {
			ServiceRegCenter.loadServiceRegInfo();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("reload", "success");
			context.setResponseData(jsonObject);
		} else {
			String targetSvcCode = context.getRequestData().getAsString(
					"svcCode");
			ServiceInfo serviceInfo = ServiceRegCenter
					.getServiceInfo(targetSvcCode);
			context.setResponseData(serviceInfo);
		}

	}

}

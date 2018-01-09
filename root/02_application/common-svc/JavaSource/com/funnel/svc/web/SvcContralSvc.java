package com.funnel.svc.web;

import net.minidev.json.JSONObject;

import com.funnel.svc.Service;
import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.ServiceUtil;

public class SvcContralSvc extends SyncService {

	@Override
	public String getSvcCode() {
		return "svcContralSvc";
	}

	@Override
	public String getSvcDesc() {
		return "获取服务信息";
	}

	@Override
	public void process(SvcContext context) {
		String action = (String) context.getRequestData().get("action");

		String svcCode = (String) context.getRequestData().get("svcCode");
		Service service = ServiceUtil.getSvcByCode(svcCode);
		if (null == service) {
			throw new SvcException("服务:" + svcCode + " 不存在，请检查配置服务信息");
		}
		if ("svcInfo".equals(action)) {
			JSONObject resp = new JSONObject();
			int doing = service.getDoingNum();
			int done = service.getDoneNum();
			int error = service.getErrorNum();
			long allExcuteTime = service.getAllExcuteTime();
			resp.put("doingNum", doing);
			resp.put("doneNum", done);
			resp.put("errorNum", error);
			resp.put("maxExcuteTime", service.getMaxExcuteTime());
			resp.put("allExcuteTime", allExcuteTime);
			if (done + error > 0) {
				resp.put("avgExcuteTime", allExcuteTime / (done + error));
			}

			resp.put("maxRequestNum", service.getMaxRequestNum());
			resp.put("pauseCause", service.getPauseCause());
			resp.put("svcDesc", service.getSvcDesc());
			context.setResponseData(resp);
			return;
		}
		if ("setMaxRequest".equals(action)) {
			int maxRequestNum = (Integer) context.getRequestData().get(
					"maxRequestNum");
			service.setMaxRequestNum(maxRequestNum);
			return;
		}
		if ("pause".equals(action)) {
			String pauseCause = (String) context.getRequestData().get(
					"pauseCause");
			service.pause(pauseCause);
			return;
		}
		if ("resum".equals(action)) {
			service.resume();
			return;
		}
	}
}

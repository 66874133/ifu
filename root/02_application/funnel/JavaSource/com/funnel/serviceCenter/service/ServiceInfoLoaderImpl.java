package com.funnel.serviceCenter.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.funnel.svc.svcReg.ServiceInfo;
import com.funnel.svc.svcReg.ServiceInfoLoader;

public class ServiceInfoLoaderImpl implements ServiceInfoLoader {
	private final Log logger = LogFactory.getLog(ServiceInfoLoaderImpl.class);

	@SuppressWarnings("unchecked")
	public List<ServiceInfo> getAllServiceInfo() {
		logger.info("开始装载服务注册信息");
		//List<ServiceInfo> serviceInfos = DataBaseUtil.select("svcInfo.querySvcInfo", null);
		logger.info("装载服务注册信息完成");
		//return serviceInfos;
		return new ArrayList<ServiceInfo>();
	}

}

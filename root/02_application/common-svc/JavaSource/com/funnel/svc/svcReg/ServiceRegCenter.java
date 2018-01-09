package com.funnel.svc.svcReg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.funnel.svc.Constant;
import com.funnel.svc.SvcException;

/**
 * 服务注册中心
 * 
 * @author wanghua4
 * 
 */
public class ServiceRegCenter {
	protected final Logger logger = Logger.getLogger(this.getClass());

	private static ServiceInfoLoader serviceInfoLoader;
	private static Map<String, ServiceInfo> serviceInfoMap = new HashMap<String, ServiceInfo>();

	/**
	 * 启动时执行创建
	 */
	public void start() {
		loadServiceRegInfo();
	}

	public static void loadServiceRegInfo() {
		List<ServiceInfo> serviceInfos = serviceInfoLoader.getAllServiceInfo();
		Map<String, ServiceInfo> serviceInfoMap = new HashMap<String, ServiceInfo>();
		for (ServiceInfo serviceInfo : serviceInfos) {
			serviceInfoMap.put(serviceInfo.getSvcCode(), serviceInfo);
		}
		ServiceRegCenter.serviceInfoMap = serviceInfoMap;
	}

	public void stop() {
	}

	public static ServiceInfo getServiceInfo(String svcCode) {
		if (null != ServiceRegCenter.serviceInfoMap
				&& null != ServiceRegCenter.serviceInfoMap.get(svcCode)) {
			return ServiceRegCenter.serviceInfoMap.get(svcCode);
		}
		throw new SvcException(Constant.ERROR_CODE_SYS_ERROR, "根据服务码:"
				+ svcCode + " 未查找到注册的服务信息");
	}

	public static Map<String, ServiceInfo> getServiceInfoMap() {
		return serviceInfoMap;
	}

	public void setServiceInfoLoader(ServiceInfoLoader serviceInfoLoader) {
		ServiceRegCenter.serviceInfoLoader = serviceInfoLoader;
	}

}

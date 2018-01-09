package com.funnel.svc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.funnel.svc.Service;

/**
 * 服务工具类 <br>
 * 根据服务码缓存服务对象<br>
 * 根据服务码获取服务对象
 * 
 * @author wanghua4
 * 
 */
public class ServiceUtil {
	// 服务缓存
	private final static Map<String, Service> svcs = new ConcurrentHashMap<String, Service>();

	// 根据服务码从缓存中获取服务对象
	public static Service getSvcByCode(String svcCode) {
		return svcs.get(svcCode);
	}

	// 添加服务到缓存中
	public static void addSvc(Service service) {
		if (StringUtils.hasText(service.getSvcCode())) {
			svcs.put(service.getSvcCode(), service);
		}
	}

}

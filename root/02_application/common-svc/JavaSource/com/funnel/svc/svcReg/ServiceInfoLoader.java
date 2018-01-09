package com.funnel.svc.svcReg;

import java.util.List;

/**
 * 服务信息服务
 * 
 * @author wanghua4
 * 
 */
public interface ServiceInfoLoader {
	/**
	 * 获取系统所以的服务信息
	 * 
	 * @return
	 */
	public List<ServiceInfo> getAllServiceInfo();
}

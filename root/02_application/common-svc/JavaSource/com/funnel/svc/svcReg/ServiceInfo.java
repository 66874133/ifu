package com.funnel.svc.svcReg;

/**
 * 服务信息
 * 
 * @author wanghua4
 * 
 */
public class ServiceInfo {
	// 服务码
	private String svcCode;
	// 服务的地址，多个用逗号分隔
	private String urls;

	public String getSvcCode() {
		return svcCode;
	}

	public void setSvcCode(String svcCode) {
		this.svcCode = svcCode;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

}

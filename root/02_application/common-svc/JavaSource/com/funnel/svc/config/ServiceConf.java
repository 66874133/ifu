package com.funnel.svc.config;

import org.apache.log4j.Logger;

import com.funnel.svc.comon.AsyncServiceUtil;


public class ServiceConf {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private static String serverName;

	// �첽�����������
	private int corePoolSize = 1;
	private int maxPoolSize = 1;
	private int queueCapacity = 1;
	private static String svcRegCenterUrl;

	/**
	 * ����ʱִ�д���
	 */
	public void start() {
		logger.info("starting...");

		AsyncServiceUtil.createDefaultExecutor(corePoolSize, maxPoolSize,
				queueCapacity);
		logger.info("started!");
	}

	public void stop() {
		logger.info("shutdown...");
		AsyncServiceUtil.shutdown();
		logger.info("shutdown!");
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		AsyncServiceUtil.setQueueCapacity(queueCapacity);
		this.queueCapacity = queueCapacity;
	}

	public static String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		ServiceConf.serverName = serverName;
	}

	public static String getSvcRegCenterUrl() {
		return svcRegCenterUrl;
	}

	public void setSvcRegCenterUrl(String svcRegCenterUrl) {
		ServiceConf.svcRegCenterUrl = svcRegCenterUrl;
	}

}

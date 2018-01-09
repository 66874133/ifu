package com.discover.crawler.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpClientUtil {
	private static Map<String, MultiThreadedHttpConnectionManager> modeConnectionManageMap = new HashMap<String, MultiThreadedHttpConnectionManager>();

	public static HttpClient getHttpClient() {
		return getHttpClient("test", 50, 500, 5000, 60000);
	}

	public static HttpClient getHttpClient(String modeName, int maxConnection,
			int maxTotalConnection, int collectionTimeOut, int readTimeOut) {
		if (null == modeConnectionManageMap.get(modeName)) {
			modeConnectionManageMap.put(
					modeName,
					createConnectionMgr(maxConnection, maxTotalConnection,
							collectionTimeOut, readTimeOut));
		}
		return new HttpClient(modeConnectionManageMap.get(modeName));
	}

	private static MultiThreadedHttpConnectionManager createConnectionMgr(
			int maxConnection, int maxTotalConnection, int collectionTimeOut,
			int readTimeOut) {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();// http连接池
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(
				maxConnection);
		connectionManager.getParams()
				.setMaxTotalConnections(maxTotalConnection);
		connectionManager.getParams().setConnectionTimeout(collectionTimeOut);
		connectionManager.getParams().setSoTimeout(readTimeOut);
		return connectionManager;
	}
}

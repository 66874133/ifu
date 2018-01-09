package com.duplex.crawler.core.executor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpFetcherClientPool {

	private final static Log logger = LogFactory
			.getLog(HttpFetcherExecutor.class);
	private static Map<String, HttpFetcherClient> nameToClient = new HashMap<String, HttpFetcherClient>();

	public static HttpFetcherClient getHttpFetcherClient(String name) {
		if (!nameToClient.containsKey(name)) {
			nameToClient.put(name, new HttpFetcherClient());
			logger.info("创建新的连接客户端name=" + name);
		}

		return nameToClient.get(name);
	}
}

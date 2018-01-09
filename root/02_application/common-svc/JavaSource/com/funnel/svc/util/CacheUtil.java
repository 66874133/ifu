package com.funnel.svc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.funnel.svc.cache.Cache;

public class CacheUtil {
	private final static Map<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	public static Cache getCacheByCode(String cacheCode) {
		return caches.get(cacheCode);
	}

	public static void addCache(Cache cache) {
		caches.put(cache.getCacheCode(), cache);
	}

}

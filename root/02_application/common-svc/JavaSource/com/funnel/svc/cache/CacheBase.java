package com.funnel.svc.cache;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcException;

public abstract class CacheBase implements Cache {
	public final Logger logger = Logger.getLogger(this.getClass());
	// 如何加载缓存数据，默认从数据库，可以配置
	private String loadSource = LOAD_SOURCE_DB;

	public String getLoadSource() {
		return loadSource;
	}

	public void setLoadSource(String loadSource) {
		this.loadSource = loadSource;
	}

	/**
	 * 从远程加载数据默认实现，需总线提供cacheMgr 获取缓存数据服务
	 * 
	 * @return
	 */
	public JSONObject getRemoteCacheData() {
		JSONObject reqData = new JSONObject();
		reqData.put("cacheCode", this.getCacheCode());
		reqData.put("action", "getCacheData");
		return (JSONObject) ServiceCallUtil.callService("cacheMgr", reqData,
				null);
	}

	@Override
	public void addToCache(Object cacheData) {
		throw new SvcException(this.getCacheCode()
				+ " method addToCache not impl");
	}

	@Override
	public void saveCacheData(Object cacheData) {
		throw new SvcException(this.getCacheCode()
				+ " method saveCacheData not impl");

	}

	@Override
	public void saveAllCache() {
		throw new SvcException(this.getCacheCode()
				+ " method saveAllCache not impl");

	}

	@Override
	public Object getDbCacheData() {
		throw new SvcException(this.getCacheCode()
				+ " method getDbCacheData not impl");
	}

	@Override
	public void load() {
		throw new SvcException(this.getCacheCode() + " method load not impl");
	}

}

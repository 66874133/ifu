package com.funnel.sys.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import com.funnel.datasource.DataBaseUtil;
import com.funnel.datasource.connection.MongoConnection;
import com.funnel.datasource.connection.MongoConnectionManager;
import com.funnel.svc.cache.CacheBase;
import com.funnel.svc.util.JsonUtil;
import com.funnel.sys.SysParam;

/**
 * 系统参数缓存
 * 
 * @author wanghua4
 */
@SuppressWarnings({ "unchecked" })
public class SysParamCache extends CacheBase {

	private static final Logger logger = Logger.getLogger(SysParamCache.class);
	private static Map<String, Map<String, SysParam>> params = new HashMap<String, Map<String, SysParam>>();

	public String getCacheCode() {
		return "sysParamCache";
	}

	public String getCacheDataForView() {
		return JsonUtil.toJSONString(params);
	}

	@Override
	public void addToCache(Object cacheData) {
		SysParam sysParam = buildSysParam(cacheData);
		if (!params.containsKey(sysParam.getParamType())) {
			params.put(sysParam.getParamType(), new HashMap<String, SysParam>());
		}
		params.get(sysParam.getParamType()).put(sysParam.getParamKey(),
				sysParam);
	}

	@Override
	public void saveCacheData(Object cacheData) {
		SysParam sysParam = buildSysParam(cacheData);
		if (params.containsKey(sysParam.getParamType())
				&& params.get(sysParam.getParamType()).containsKey(
						sysParam.getParamKey())) {
			// 做更新
			DataBaseUtil.update("cache.updateParam", buildSysParam(cacheData));
		} else {
			// 做insert
			DataBaseUtil.insert("cache.insertParam", buildSysParam(cacheData));
		}
	}

	private SysParam buildSysParam(Object cacheData) {
		SysParam sysParam = null;
		// 如果是管理中心过来的会是JSONObject类型
		if (cacheData instanceof JSONObject) {
			sysParam = buildSysParam((JSONObject) cacheData);
		} else {
			sysParam = (SysParam) cacheData;
		}
		return sysParam;
	}

	@Override
	public List<SysParam> getDbCacheData() {
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		try {
			return mongoConnection.editor().select(
					this.getClass().getSimpleName(), null, this.getClass());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void load() {
		if (CacheBase.LOAD_SOURCE_DB.equals(this.getLoadSource())) {
			loadCacheFromDb();
		} else {
			loadCacheFromRemote();
		}

	}

	private void loadCacheFromDb() {
		logger.info("开始从数据库装载sysParamCache");
		List<SysParam> paramList = getDbCacheData();
		Map<String, Map<String, SysParam>> curParams = new HashMap<String, Map<String, SysParam>>();
		for (SysParam sysParam : paramList) {
			if (!curParams.containsKey(sysParam.getParamType())) {
				curParams.put(sysParam.getParamType(),
						new HashMap<String, SysParam>());
			}
			curParams.get(sysParam.getParamType()).put(sysParam.getParamKey(),
					sysParam);
		}
		params = curParams;
		logger.info("从数据库装载sysParamCache完成");
	}

	private void loadCacheFromRemote() {
		logger.info("开始从远程装载sysParamCache");
		JSONObject result = this.getRemoteCacheData();
		JSONArray cacheDatas = (JSONArray) result.get("cacheData");

		Map<String, Map<String, SysParam>> curParams = new HashMap<String, Map<String, SysParam>>();
		for (int i = 0; i < cacheDatas.size(); i++) {
			SysParam sysParam = buildSysParam((JSONObject) cacheDatas.get(i));
			if (!curParams.containsKey(sysParam.getParamType())) {
				curParams.put(sysParam.getParamType(),
						new HashMap<String, SysParam>());
			}
			curParams.get(sysParam.getParamType()).put(sysParam.getParamKey(),
					sysParam);
		}
		params = curParams;
		logger.info("从远程装载sysParamCache完成");
	}

	private SysParam buildSysParam(JSONObject jsonData) {
		SysParam sysParam = new SysParam();
		sysParam.setParamType((String) jsonData.get("paramType"));
		sysParam.setParamKey((String) jsonData.get("paramKey"));
		sysParam.setParamValue((String) jsonData.get("paramValue"));
		sysParam.setDescript((String) jsonData.get("descript"));
		return sysParam;
	}

	public static SysParam getParamByKey(String type, String key) {
		if (params.containsKey(type)) {
			return params.get(type).get(key);
		}
		return null;
	}

	public static String getParamValueByKey(String type, String key) {
		if (params.containsKey(type) && params.get(type).containsKey(key)) {
			return params.get(type).get(key).getParamValue();
		}
		return null;
	}

}

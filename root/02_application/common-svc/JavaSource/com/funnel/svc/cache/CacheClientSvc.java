package com.funnel.svc.cache;

import net.minidev.json.JSONObject;

import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.CacheUtil;
import com.funnel.svc.util.JsonUtil;
import com.funnel.svc.util.StringUtils;

/**
 * 缓存客户端，根据缓存控制中心发送的命令，控制缓存 控制中心发送命令是以topic的形式发送，所以需判断当前是否有对应的缓存
 * 
 * @author wanghua4
 * 
 */
public class CacheClientSvc extends SyncService {

	@Override
	public String getSvcCode() {
		return "cacheClient";
	}

	@Override
	public String getSvcDesc() {
		return "缓存客户端服务服务";
	}

	@Override
	public void process(SvcContext context) {
		String action = (String) context.getRequestData().get("action");
		if (!StringUtils.hasText(action)) {
			throw new SvcException("action参数不能为空");
		}
		String cacheCode = (String) context.getRequestData().get("cache");
		if (!StringUtils.hasText(cacheCode)) {
			throw new SvcException("cache参数不能为空");
		}
		// 设置缓存内容
		if ("set".equals(action)) {
			Cache cache = CacheUtil.getCacheByCode(cacheCode);
			if (null == cache) {
				throw new SvcException("更新缓存:" + cacheCode + " 当前服务器中不存在该缓存");
			}

			cache.addToCache(context.getRequestData());
			logger.info("更新缓存:" + cacheCode + " 内容:"
					+ JsonUtil.toJSONString(context.getRequestData()) + " 完成");
			return;
		}
		// 重新装载缓存
		if ("load".equals(action)) {
			Cache cache = CacheUtil.getCacheByCode(cacheCode);
			if (null == cache) {
				throw new SvcException("重新装载缓存:" + cacheCode + " 当前服务器中不存在该缓存");
			}
			cache.load();
			logger.info("重新装载缓存:" + cacheCode + " 完成");
			return;
		}
		// 获取缓存字符串内容
		if ("view".equals(action)) {
			Cache cache = CacheUtil.getCacheByCode(cacheCode);
			if (null == cache) {
				throw new SvcException("获取缓存内容:" + cacheCode + " 当前服务器中不存在该缓存");
			}
			JSONObject result = new JSONObject();
			result.put("cacheData", cache.getCacheDataForView());
			context.setResponseData(result);
			logger.info("获取缓存:" + cacheCode + " 内容完成");
			return;
		}
	}

}

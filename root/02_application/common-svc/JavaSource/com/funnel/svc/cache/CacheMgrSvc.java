package com.funnel.svc.cache;

import net.minidev.json.JSONObject;

import com.funnel.svc.Constant;
import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.CacheUtil;
import com.funnel.svc.util.StringUtils;

/**
 * 缓存管理，控制中心 提供远程缓存获取缓存数据 推送缓存更新到远程 推送重新load命令到指定缓存 查看指定缓存内容
 * 
 * 所有的远程缓存，要接收控制中心管理，需提供cacheClient服务
 * 
 * @author wanghua4
 * 
 */
public class CacheMgrSvc extends SyncService {

	@Override
	public String getSvcCode() {
		return "cacheMgr";
	}

	@Override
	public String getSvcDesc() {
		return "缓存管理服务";
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
		// 提供远程缓存获取缓存数据服务,需在控制中心所在服务提供缓存如何查询缓存数据的方法
		if ("getCacheData".equals(action)) {
			Cache cache = CacheUtil.getCacheByCode(cacheCode);
			JSONObject result = new JSONObject();
			result.put("cacheData", cache.getDbCacheData());
			context.setResponseData(result);
			return;
		}
		// 设置缓存内容,先保存，在通知远程缓存更新
		if ("set".equals(action)) {
			Cache cache = CacheUtil.getCacheByCode(cacheCode);
			cache.saveCacheData(context.getRequestData());
			JSONObject reqData = new JSONObject();
			reqData.putAll(context.getRequestData());

			JSONObject header = new JSONObject();
			header.put(Constant.MESSAGE_HEAD_MODE_FIELD,
					Constant.MESSAGE_HEAD_MODE_TOPIC);
			reqData.put(Constant.MESSAGE_HEAD_FIELD, header);
			JSONObject result = ServiceCallUtil.callService("cacheClient",
					reqData, null);
			context.setResponseData(result);
			return;
		}
		// 重新装载缓存，通知所有的远程缓存
		if ("load".equals(action)) {
			JSONObject reqData = new JSONObject();
			reqData.putAll(context.getRequestData());
			JSONObject header = new JSONObject();
			header.put(Constant.MESSAGE_HEAD_MODE_FIELD,
					Constant.MESSAGE_HEAD_MODE_TOPIC);
			reqData.put(Constant.MESSAGE_HEAD_FIELD, header);
			JSONObject result = ServiceCallUtil.callService("cacheClient",
					reqData, null);
			context.setResponseData(result);
		}
		// 查看所有缓存中的内容
		if ("view".equals(action)) {
			JSONObject reqData = new JSONObject();
			reqData.putAll(context.getRequestData());

			JSONObject header = new JSONObject();
			header.put(Constant.MESSAGE_HEAD_MODE_FIELD,
					Constant.MESSAGE_HEAD_MODE_TOPIC);
			reqData.put(Constant.MESSAGE_HEAD_FIELD, header);
			JSONObject result = ServiceCallUtil.callService("cacheClient",
					reqData, null);
			context.setResponseData(result);
			return;
		}
	}

}

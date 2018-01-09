package com.funnel.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONObject;

import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.StringUtils;

public class RedisContralSvc extends SyncService {
	private RedisServcie redisServcie;
	private Map<String, RedisServcie> redisServiceMap = new HashMap<String, RedisServcie>();

	@Override
	public String getSvcCode() {
		return "redisContral";
	}

	@Override
	public String getSvcDesc() {
		return "redis控制服务";
	}

	@Override
	public void process(SvcContext context) {
		String method = context.getRequestData().get("method").toString();
		String key = context.getRequestData().get("key").toString();

		RedisServcie redisServcie = this.redisServcie;
		String redis = context.getRequestData().getAsString("redis");
		if (StringUtils.hasText(redis) && null != redisServiceMap.get(redis)) {
			redisServcie = redisServiceMap.get(redis);
		}

		JSONObject resp = new JSONObject();
		if ("get".equals(method)) {
			resp.put("result", redisServcie.get(key));
		}
		if ("hget".equals(method)) {
			if (null != context.getRequestData().get("field")) {
				String field = context.getRequestData().get("field").toString();
				resp.put("result", redisServcie.hget(key, field));
			} else {
				Map<String, String> result = redisServcie.hgetAll(key);
				resp.put("result", result);
			}
		}
		if ("smembers".equals(method)) {
			Set<String> result = redisServcie.smembers(key);
			resp.put("result", result);
		}
		if ("keys".equals(method)) {
			resp.put("result", redisServcie.keys(key));
		}
		if ("del".equals(method)) {
			resp.put("result", redisServcie.del(key));
		}
		if ("llen".equals(method)) {
			resp.put("result", redisServcie.llen(key));
		}
		if ("lpush".equals(method)) {
			String value = context.getRequestData().get("value").toString();
			redisServcie.lpush(key, value);
		}
		if ("lrange".equals(method)) {
			Integer start = Integer.parseInt(context.getRequestData()
					.get("start").toString());
			Integer end = Integer.parseInt(context.getRequestData().get("end")
					.toString());
			resp.put("result", redisServcie.lrange(key, start, end));
		}
		context.setResponseData(resp);
	}

	public RedisServcie getRedisServcie() {
		return redisServcie;
	}

	public void setRedisServcie(RedisServcie redisServcie) {
		this.redisServcie = redisServcie;
	}

	public Map<String, RedisServcie> getRedisServiceMap() {
		return redisServiceMap;
	}

	public void setRedisServiceMap(Map<String, RedisServcie> redisServiceMap) {
		this.redisServiceMap = redisServiceMap;
	}

}

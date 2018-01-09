package com.funnel.svc.util;

import com.funnel.redis.RedisServcie;

public class ApplicationUtil {

	private static RedisServcie goodsRedisService;

	public static RedisServcie getGoodsRedisService() {
		return goodsRedisService;
	}

	public void setGoodsRedisService(RedisServcie goodsRedisService) {
		ApplicationUtil.goodsRedisService = goodsRedisService;
	}

}

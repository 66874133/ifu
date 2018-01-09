package com.funnel.svc.transformer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SvcCodeAndTransformerCodeMapping {
	// 转换器缓存
	private final static Map<String, String> transfomers = new ConcurrentHashMap<String, String>();

	// 根据服务码从缓存中获取服务对象
	public static String getTransfomerCodeBySvcCode(String svcCode) {
		return transfomers.get(svcCode);
	}

	// 添加转换器到缓存中
	public static void addMapping(String svcCode, String transfomerCode) {
		transfomers.put(svcCode, transfomerCode);
	}
}

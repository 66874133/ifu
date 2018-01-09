package com.funnel.svc.transformer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransformerUtil {
	// 转换器缓存
	private final static Map<String, SvcMsgTransformer> transfomers = new ConcurrentHashMap<String, SvcMsgTransformer>();

	// 根据服务码从缓存中获取服务对象
	public static SvcMsgTransformer getTransfomerByCode(String transfomerCode) {
		return transfomers.get(transfomerCode);
	}

	// 添加转换器到缓存中
	public static void addTransfomer(SvcMsgTransformer transfomer) {
		transfomers.put(transfomer.getCode(), transfomer);
	}
}

package com.funnel.redis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JacksonUtil {
	protected static final Logger logger = Logger.getLogger(JacksonUtil.class);
	private final static ObjectMapper objectMapper = new ObjectMapper();

	private JacksonUtil() {

	}

	public static ObjectMapper getInstance() {

		return objectMapper;
	}

	/**
	 * javaBean,list,array convert to json string
	 */
	public static String obj2json(Object obj) {
		try {
			String reslutJson = objectMapper.writeValueAsString(obj);
			if (reslutJson.startsWith("{") || reslutJson.startsWith("[")) {
				return reslutJson;
			}
			return obj.toString();
		} catch (Exception e) {
			throw new RuntimeException("obj2json error", e);
		}

	}

	/**
	 * json string convert to javaBean
	 */
	public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
		long t = System.currentTimeMillis();
		try {
			return objectMapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			throw new RuntimeException("json2pojo error", e);
		} finally {
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 10) {
				logger.warn("json2pojo time:" + userTime);
			}
		}
	}

	/**
	 * json string convert to map
	 */
	public static <T> Map<String, Object> json2map(String jsonStr) {
		long t = System.currentTimeMillis();
		try {
			return objectMapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			throw new RuntimeException("json2map error", e);
		} finally {
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 10) {
				logger.warn("json2map time:" + userTime);
			}
		}
	}

	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
		long t = System.currentTimeMillis();
		try {
			Map<String, Map<String, Object>> map = objectMapper.readValue(
					jsonStr, new TypeReference<Map<String, T>>() {
					});
			Map<String, T> result = new HashMap<String, T>();
			for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
				result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("json2map error", e);
		} finally {
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 10) {
				logger.warn("json2map time:" + userTime);
			}
		}
	}

	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
		long t = System.currentTimeMillis();
		try {
			List<Map<String, Object>> list = objectMapper.readValue(
					jsonArrayStr, new TypeReference<List<T>>() {
					});
			List<T> result = new ArrayList<T>();
			for (Map<String, Object> map : list) {
				result.add(map2pojo(map, clazz));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("json2list error", e);
		} finally {
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 10) {
				logger.warn("json2list time:" + userTime);
			}
		}
	}

	/**
	 * map convert to javaBean
	 */
	public static <T> T map2pojo(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}
}

package com.funnel.datasource.connection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ObjectValueMapper {

	private Logger logger = Logger.getLogger(this.getClass());

	public Map<String, String> toFieldValuesMap(Object o)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, String> map = new HashMap<String, String>();
		// 得到类对象
		Class<?> userCla = (Class<?>) o.getClass();
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			Object val = f.get(o);// 得到此属性的值
			if (null != val) {
				map.put(f.getName(), val.toString());
			} else {
				map.put(f.getName(), null);
			}

			logger.debug("name:" + f.getName() + "\t value = " + val);

		}
		return map;
	}

	public Object toObject(Map<String, Object> map, Class<?> userCla)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		// 得到类对象
		Object o = userCla.newInstance();
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的

			if (null != map.get(f.getName())) {
				f.set(o, map.get(f.getName()));
			} else {
				f.set(o, null);
			}

			logger.debug("name:" + f.getName() + "\t value = "
					+ map.get(f.getName()));

		}
		return o;
	}
}

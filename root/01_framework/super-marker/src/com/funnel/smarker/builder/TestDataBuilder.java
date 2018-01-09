package com.funnel.smarker.builder;

import java.util.Map;

public class TestDataBuilder implements IDataBuilder {

	public void build(Map<String, Object> datas) {

		datas.put("user", "lavasoft");
		datas.put("url", "http://www.baidu.com/");
		datas.put("name", "dfdfdfdf");
	}

}

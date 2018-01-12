package com.sieve.core.condition;

import java.util.Map;

/**
 * 连接运算
 * 
 * @author jiangbo3
 * 
 */
public class ConnetConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		String value = input.get(inputVariable);

		return value + confValue;
	}

}

package com.sieve.core.condition;

import java.util.Map;

public class GreaterConditionAnalyzer implements IOneAnalyzer {


	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		int value = Integer.parseInt(input.get(inputVariable));
		//如果比配置的值大则满足
		if (value > Integer.parseInt(confValue)) {
			return SIMPLE_MATCH;
		}

		return NOT_MATCH;
	}

}

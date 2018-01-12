package com.sieve.core.condition;

import java.util.Map;

public class ContainsConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {
		String value = input.get(inputVariable);
		if (value.contains(confValue)) {
			return "1";
		}
		return "0";
	}
}

package com.sieve.core.condition;

import java.util.Map;

public class StartwithConditionAnalyzer implements IOneAnalyzer {


	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		String value = input.get(inputVariable);
		if (!value.startsWith(confValue)) {
			return "1";
		}
		return "0";
	}

}

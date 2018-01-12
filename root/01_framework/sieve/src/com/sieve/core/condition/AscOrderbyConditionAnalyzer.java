package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

public class AscOrderbyConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		long s = 1000000000L - Long.parseLong(input.get(inputVariable));
		return StringUtil.getNumberString(s, 10);
	}

}

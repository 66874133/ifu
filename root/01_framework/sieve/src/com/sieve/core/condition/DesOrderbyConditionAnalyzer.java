package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

public class DesOrderbyConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		return StringUtil.getNumberString(input.get(inputVariable), 10);
	}

}

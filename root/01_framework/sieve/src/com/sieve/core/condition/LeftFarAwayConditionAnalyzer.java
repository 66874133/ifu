package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

public class LeftFarAwayConditionAnalyzer implements IOneAnalyzer {


	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		int value = Integer.parseInt(input.get(inputVariable));

		if (value > Integer.parseInt(confValue)) {
			return StringUtil.getNumberString(0, 5);
		}

		else {
			return StringUtil.getNumberString(Integer.parseInt(confValue)
					- value, 5);
		}

	}

}

package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

public class RandomOrderbyConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		long s = 0;
		if (null != confValue && !"".equals(confValue)) {

			s = Long.parseLong(confValue);
		} else {
			s = (long) (Math.random() * 1000000000L);
		}

		return StringUtil.getNumberString(s, 10);
	}

}

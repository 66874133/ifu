package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

/**
 * 加法运算
 * 
 * @author jiangbo3
 * 
 */
public class AddingConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		double value = Double.parseDouble(input.get(inputVariable));

		double d = StringUtil.keepDouble(Double.parseDouble(confValue));

		return StringUtil.getDoubleString(value + d, 10);
	}

}

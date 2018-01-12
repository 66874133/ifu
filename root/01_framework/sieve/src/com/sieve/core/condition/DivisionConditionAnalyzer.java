package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

/**
 * 除法运算
 * 
 * @author jiangbo3
 * 
 */
public class DivisionConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		double value = Double.parseDouble((input.get(inputVariable)));

		double d = StringUtil.keepDouble(value / Double.parseDouble(confValue));
		
		return String.valueOf(d);
	}

}

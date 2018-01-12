package com.sieve.core.condition;

import java.util.Date;
import java.util.Map;

import com.sieve.util.DateUtil;
import com.sieve.util.StringUtil;

/**
 * 
 * 
 * @author jiangbo3
 * 
 */
public class CurrentDateDistanceConditionAnalyzer implements IOneAnalyzer {

	

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		String value = input.get(inputVariable);

		Date date = DateUtil.parseStr2Date(value);
		Date current = new Date();
		

		long currentMinute = Long.parseLong(confValue);

		long t = current.getTime() - date.getTime();

		long i = (long) ((t / DateUtil.ONE_MINUTE_MS)/currentMinute);
		
		

		return StringUtil.getNumberString(String.valueOf(1000000000L-i), 10);
	}
}

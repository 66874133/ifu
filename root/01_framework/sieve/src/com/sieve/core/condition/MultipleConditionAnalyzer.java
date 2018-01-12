package com.sieve.core.condition;

import java.util.Map;

import org.apache.log4j.Logger;

import com.sieve.util.StringUtil;

/**
 * 乘法法运算
 * 
 * @author jiangbo3
 * 
 */
public class MultipleConditionAnalyzer implements IOneAnalyzer {
	private final Logger logger = Logger.getLogger(this.getClass());

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {
		double value = 0d;
		try {
			value = Double.parseDouble((input.get(inputVariable)));

			double d = StringUtil.keepDouble(value
					* Double.parseDouble(confValue));

			return StringUtil.getNumberString(String.valueOf((long) d), 10);
		} catch (Exception e) {
			logger.error("inputVariable=" + inputVariable + " value=" + value
					+ " confValue=" + confValue, e);
		}
		return null;
	}

}

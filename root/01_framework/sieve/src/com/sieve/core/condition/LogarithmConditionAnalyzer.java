package com.sieve.core.condition;

import java.util.Map;

import com.sieve.util.StringUtil;

/**
 * 对数运算
 * 
 * @author jiangbo3
 * 
 */
public class LogarithmConditionAnalyzer implements IOneAnalyzer {

	public String analyse(String inputVariable, String confValue,
			Map<String, String> input) {

		double value = 10.00;
		if (null != input.get(inputVariable)
				&& !"".equals(input.get(inputVariable))) {
			value = Double.parseDouble((input.get(inputVariable)));
		}

		if (value > 0) {
			double v = log(value, Double.parseDouble(confValue));

			return StringUtil.getNumberString(
					String.valueOf(StringUtil.keepDouble(v)), 10);
		} else {
			return StringUtil.getNumberString("0.00", 10);
		}

	}

	/**
	 * 取对数
	 * 
	 * @param value
	 *            变量
	 * @param base
	 *            底数
	 * @return
	 */
	private static double log(double value, double base) {

		return Math.log(value) / Math.log(base);

	}
}

package com.sieve.util;

import java.math.BigDecimal;

public class StringUtil {

	public static String getNumberString(long number, int digit) {
		String strs = String.valueOf(number);

		int len = strs.length();
		if (len < digit) {
			int size = digit - len;

			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < size; i++) {
				builder.append("0");
			}
			builder.append(strs);

			return builder.toString();
		}
		return strs;

	}

	/**
	 * 四舍五入保留两位小数
	 * 
	 * @param f
	 * @return
	 */
	public static double keepDouble(double f) {
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * 四舍五入保留两位小数
	 * 
	 * @param doubleString
	 * @return
	 */
	public static String keepDouble(String doubleString) {

		return String.valueOf(keepDouble(Double.parseDouble(doubleString)));

	}

	public static String getNumberString(String number, int digit) {
		String strs = String.valueOf(number);

		int len = strs.length();
		if (len < digit) {
			int size = digit - len;

			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < size; i++) {
				builder.append("0");
			}
			builder.append(strs);

			return builder.toString();
		}
		return strs;

	}

	public static String getDoubleString(String number, int digit) {
		double d = Double.parseDouble(number);
		d = keepDouble(d);

		String dStr = String.valueOf(d);

		if ((dStr.length() - (dStr.indexOf(".")) <= 2)) {
			dStr = dStr + "0";
		}

		return getNumberString(dStr, digit);

	}
	
	public static String getDoubleString(double d, int digit) {
		d = keepDouble(d);

		String dStr = String.valueOf(d);

		if ((dStr.length() - (dStr.indexOf(".")) <= 2)) {
			dStr = dStr + "0";
		}

		return getNumberString(dStr, digit);

	}

	public static void main(String[] args) {
		System.out.println(getNumberString(21, 6));
		System.out.println(keepDouble(0.00));
		System.out.println(getDoubleString("2", 10));
		System.out.println(getDoubleString("0.1", 10));
		System.out.println(getDoubleString("0", 10));

	}
}

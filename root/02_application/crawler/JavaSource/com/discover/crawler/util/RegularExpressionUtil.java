package com.discover.crawler.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtil {
	public static final String CHINESE_CHARACTER = "\u4e00-\u9fa5";
	public static final String ENGLISH_CHARACTER = "A-Za-z";
	public static final String NUMBER_CHARACTER = "0-9";
	public static final String ENGLISH_NUMBER = "A-Za-z0-9";
	public static final String SEGMENT_FRAG = "\r\n";
	public static final String SENTENCE_FRAG = ".?!;。？！；:： ";
	public static final String PAUSE_FRAG = "、";
	public static final String PHRASE_FRAG = "，,";

	private static final String SCOPE_START = "[";
	private static final String SCOPE_END = "]";

	public static String getChineseExpression() {
		return getScopedExpression(CHINESE_CHARACTER);
	}

	public static String getNumberExpression() {
		return getScopedExpression(NUMBER_CHARACTER);
	}

	public static String getCharacterAndNumberExpression() {
		return getScopedExpression(ENGLISH_NUMBER);
	}

	public static String getEnglishExpression() {
		return getScopedExpression(ENGLISH_CHARACTER);
	}

	public static String getScopedExpression(String expression) {
		return SCOPE_START + expression + SCOPE_END;
	}

	public static String getScopedExpression(String[] expressions) {
		String s = "";
		for (String expression : expressions) {
			s = s + expression;
		}

		return getScopedExpression(s);
	}

	public static boolean matches(String value, String pattern) {
		if (value == null || pattern == null) {
			return false;
		}
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(value);
		return matcher.matches();
	}

	public static String[] getSinglePatternValue(String source, String pattern) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(pattern);

		Matcher matcher = p.matcher(source);
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				if (matcher.start(i) > -1) {
					list.add(matcher.group(i).trim());
				}
			}
		}

		return list.toArray(new String[] {});
	}

	public static String[] getPatternValue(String source, String pattern) {

		if (pattern.contains("->")) {
			String[] patterns = pattern.split("->");

			for (String p : patterns) {
				String[] strs = getSinglePatternValue(source, p);

				if (null != strs && strs.length > 0 && null != strs[0]
						&& !"".equals(strs[0])) {
					return strs;
				}
			}

		} else {
			return getSinglePatternValue(source, pattern);
		}
		List<String> list = new ArrayList<String>();

		return list.toArray(new String[] {});
	}
}
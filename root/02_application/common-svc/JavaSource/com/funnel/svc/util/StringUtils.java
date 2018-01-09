package com.funnel.svc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class StringUtils {
	public static final String SPLITER_RECORD = "원";
	public static final String SPLITER_FIELD = "빈";
	public static final String UNIQUE_STRING = "어";

	private static final Pattern pattern = Pattern.compile("(&#(\\p{XDigit}{1,5});)");    
	
	public static boolean hasLength(CharSequence str) {
		return (str != null) && (str.length() > 0);
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String unicode10ToWord(String unicode) {
		try {
			if (unicode.contains("&#")) {
				String[] ss = unicode.split("&#");
				String newString = "";
				for (String s : ss) {
					if (containsNumber(s) && s.contains(";")) {
						int index = s.indexOf(";");
						String number = s.substring(0, index);
						String s1 = "";
						int a = Integer.parseInt(number, 10);
						s1 = s1 + (char) a;

						newString = newString + s1 + s.substring(index + 1);
					} else {
						newString = newString + s;
					}
				}

				return newString;
			} else {
				return unicode;
			}
		} catch (Exception e) {
			return unicode;
		}
	}

	public static boolean containsNumber(String source) {
		for (char i = '0'; i < '9' + 1; i++) {
			if (source.contains(String.valueOf(i))) {
				return true;
			}
		}
		return false;
	}

	public static int[] getAllIndex(String source, String rex) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int position = 0;
		while (position < source.length()) {
			int index = source.indexOf(rex, position);
			if (index > -1) {
				list.add(source.indexOf(rex, position));
				position = index + 1;
			} else {
				break;
			}
		}

		int[] ins = new int[list.size()];
		for (int i = 0; i < ins.length; i++) {
			ins[i] = list.get(i);
		}

		return ins;
	}

	public static String[] split(String string, String rex) {
		int[] indexs = getAllIndex(string, rex);

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < indexs.length; i++) {
			if (i == 0) {
				list.add(string.substring(0, indexs[i]));
			}

			if (i < indexs.length - 1) {
				list.add(string.substring(indexs[i] + rex.length(),
						indexs[i + 1]));
			} else if (i == indexs.length - 1) {
				list.add(string.substring(indexs[i] + rex.length()));
			}
		}

		if (indexs.length == 0) {
			list.add(string);
		}

		return list.toArray(new String[] {});
	}

	/**
	 * 把字符串列用空格连接成一个字符串
	 * 
	 * @param strings
	 *            字符串列
	 * @return 字符串
	 */
	public static String getStringFromStrings(String[] strings) {
		StringBuffer buf = new StringBuffer();
		for (String string : strings) {
			buf.append(string);
			buf.append(" ");
		}

		return buf.toString().trim();
	}

	public static String getStringFromStringsWithUnique(String[] strings) {
		StringBuffer buf = new StringBuffer();
		if (strings.length > 0) {
			for (int i = 0; i < strings.length - 1; i++) {
				buf.append(strings[i]);
				buf.append(UNIQUE_STRING);
			}
			buf.append(strings[strings.length - 1]);
		}

		return buf.toString().trim();
	}

	public static String getStringFromStrings(List<String> list, String spliter) {
		return getStringFromStrings(list.toArray(new String[] {}), spliter);
	}

	/**
	 * 把字符串列用指定的方式连接成一个字符串
	 * 
	 * @param strings
	 *            字符串列
	 * @return 字符串
	 */
	public static String getStringFromStrings(String[] strings, String spliter) {
		if (strings == null || strings.length == 0) {
			return "";
		} else {
			if (spliter == null) {
				spliter = "";
			}
			StringBuffer buf = new StringBuffer();
			for (String string : strings) {
				buf.append(string);
				buf.append(spliter);
			}

			return buf.toString().substring(0,
					buf.toString().length() - spliter.length());
		}
	}

	public static String[] getStringsFromString(String stirng, String spliter) {
		return stirng.split(spliter);
	}

	public static boolean isNullOrEmpty(String string) {
		return !(string != null && string.trim().length() != 0);
	}

	/**
	 * 去掉字符串中的空格和tab
	 * 
	 * @param string
	 *            字符串
	 * @return 去掉后的值
	 */
	public static String removeWhiteSpace(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		} else {
			string = string.replace(" ", "");
			string = string.replace("\t", "");

			return string;
		}
	}

	/**
	 * 判断是否为英文或者数字字符串
	 * 
	 * @param string
	 *            字符串
	 * @return true则是，false则否
	 */
	public static boolean isCharOrNumberString(String string) {
		char[] cs = string.toCharArray();
		for (char c : cs) {
			if (!Character.isDigit(c) && !isEnglishCharacter(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断是否为数字字符串
	 * 
	 * @param string
	 *            字符串
	 * @return true则是，false则否
	 */
	public static boolean isNumberString(String string) {
		char[] cs = string.toCharArray();
		for (char c : cs) {
			if (!Character.isDigit(c) && c != '.') {
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断是否为英文字符
	 * 
	 * @param ch
	 *            字符
	 * @return true为英文字符，false则不是
	 */
	public static boolean isEnglishCharacter(char ch) {
		String a = String.valueOf(ch).toLowerCase();
		return a.charAt(0) >= 'a' && a.charAt(0) <= 'z';
	}

	public static boolean isEnglishOrNumberCharacter(char ch) {
		return isEnglishCharacter(ch) || Character.isDigit(ch);
	}

	public static boolean isNumberCharacter(char ch) {
		return Character.isDigit(ch);
	}

	/**
	 * 全角转半角
	 * 
	 * @param QJstr
	 *            全角字符
	 * @return
	 */
	public static String qBchange(String QJstr) {
		if (isNullOrEmpty(QJstr)) {
			return "";
		}

		char[] c = QJstr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 去除字符串中的特殊字符
	 * 
	 * @param str
	 *            原始字符串
	 * @return 去除特殊字符后的字符串
	 */
	public static String removeSpecialChars(String str) {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&* ()_+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Matcher m = null;
		try {
			Pattern p = Pattern.compile(regEx);
			m = p.matcher(str);
		} catch (PatternSyntaxException p) {
			p.printStackTrace();
		}
		return m.replaceAll("").trim();
	}

	public static String getTimeString(String time, int length) {
		if (time.contains(".")) {
			int dl = time.length() - time.indexOf(".") - 1;
			if (dl > length) {
				time = time.substring(0, time.length() - dl + length);
			}
		}

		return time;
	}

	public static boolean parseBoolean(String bool) {
		try {
			return Boolean.parseBoolean(bool);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean stringEquals(String a, String b) {
		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null) {
			return a.equals(b);
		} else {
			return false;
		}
	}

	/**
	 * 约定table field和object field之间的关系
	 * 
	 * ccDdEfg = > cc_dd_efg
	 * 
	 * @param tableField
	 * @return Object field name
	 */
	public static String getTableField(String objectField) {

		if (StringUtils.isNullOrEmpty(objectField)) {
			throw new RuntimeException("empty string is not allowed");
		}

		try {
			char[] chars = objectField.toCharArray();

			List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] >= 'A' && chars[i] <= 'Z') {
					ids.add(i);
				}
			}

			if (ids.size() == 0) {
				return objectField;
			} else {
				StringBuffer sb = new StringBuffer();
				sb.append(objectField.substring(0, ids.get(0)));
				for (int i = 0; i < ids.size() - 1; i++) {
					sb.append("_"
							+ objectField.substring(ids.get(i), ids.get(i + 1))
									.toLowerCase());
				}
				sb.append("_"
						+ objectField.substring(ids.get(ids.size() - 1),
								objectField.length()).toLowerCase());
				return sb.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static String replace(String temple, HashMap<String, String> map) {
		HashMap<Integer, String> indexMap = new HashMap<Integer, String>();
		HashSet<Integer> set = new HashSet<Integer>();
		for (String key : map.keySet()) {
			int index = temple.indexOf(key);
			if (index >= 0) {
				indexMap.put(index, key);
				for (int i = index; i < index + key.length(); i++) {
					set.add(i);
				}
			}
		}
		String s = "";
		for (int i = 0; i < temple.length(); i++) {
			if (!set.contains(i)) {
				s = s + temple.charAt(i);
			} else {
				if (indexMap.containsKey(i)) {
					s = s + map.get(indexMap.get(i));
				}
			}
		}

		return s;
	}

	public static boolean contains(String big, String small, String spliter) {
		String[] ss = big.split(spliter);

		for (String s : ss) {
			if (s.trim().toLowerCase().equals(small.trim().toLowerCase())) {
				return true;
			}
		}

		return false;
	}
	
	public static String unicodeToString(String str)
	{
        Matcher matcher = pattern.matcher(str);    
        char ch;    
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2));     
            str = str.replace(matcher.group(1), ch + "");
        }  
        
		return str;
	}


}
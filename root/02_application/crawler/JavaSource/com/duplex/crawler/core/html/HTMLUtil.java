package com.duplex.crawler.core.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Tag;

import com.funnel.svc.util.CombinationUtil;





public class HTMLUtil {

	private final static String NBSP = "&nbsp;";
	private final static String AMP = "&amp;";
	private final static String QUOT = "&quot;";
	private final static String LT = "&lt;";
	private final static String GT = "&gt;";
	public static final String PATTERN_SPLITER = "PATTEN_SPILTER";

	private static final ThreadLocal<Map<String, Matcher>> TL_MATCHER_MAP = new ThreadLocal<Map<String, Matcher>>() {
		protected Map<String, Matcher> initialValue() {
			return new HashMap<String, Matcher>(50);
		}
	};

	public static String getNodeValue(HTMLPath parser, String path) {
		String[] values = parser.SelectNodes(path);

		if (values.length > 0) {
			return values[0];
		} else {
			return "";
		}
	}

	public static String getNodeCount(HTMLPath parser, String path) {
		String[] values = parser.SelectNodes(path);

		return String.valueOf(values.length);
	}

	public static String getBooleanNodeValue(HTMLPath parser, String path,
			String existedValue) {
		String notExistedValue = "";
		if ("false".equals(existedValue)) {
			notExistedValue = "true";
		} else {
			notExistedValue = "false";
		}
		return getBooleanNodeValue(parser, path, existedValue, notExistedValue);

	}

	public static String getBooleanNodeValue(HTMLPath parser, String path,
			String existedValue, String notExistedValue) {
		String[] values = parser.SelectNodes(path);

		if (values.length > 0) {
			return existedValue;
		} else {
			if ("false".equals(existedValue)) {
				return "true";
			} else if ("true".equals(existedValue)) {
				return "false";
			} else {
				return notExistedValue;
			}
		}
	}

	public static String[] getBooleanNodeValues(HTMLPath parser, String path,
			String existedValue) {
		String notExistedValue = "";
		if ("false".equals(existedValue)) {
			notExistedValue = "true";
		} else {
			notExistedValue = "false";
		}
		return getBooleanNodeValues(parser, path, existedValue, notExistedValue);
	}

	public static int getNodeOrder(HTMLPath parser, String path) {
		Node[] nodes = parser.SelectOneConditionNodes(path);
		int index = 1;
		if (nodes.length == 1) {
			Node n = nodes[0];
			String tagName = ((Tag) n).getTagName();
			while(true){
				n = n.getPreviousSibling();
				if(n == null){
					break;
				}
				if(n instanceof Tag){
					if(tagName.equals(((Tag)n).getTagName())){
						index++;
					}
				}
			}
		}

		return index;
	}

	public static String[] getBooleanNodeValues(HTMLPath parser, String path,
			String existedValue, String notExistedValue) {
		String[] values = parser.SelectNodes(path);
		for (int i = 0; i < values.length; i++) {
			if (values.length > 0) {
				values[i] = existedValue;
			} else {
				if ("false".equals(existedValue)) {
					values[i] = "true";
				} else if ("true".equals(existedValue)) {
					values[i] = "false";
				} else {
					values[i] = notExistedValue;
				}
			}
		}

		return values;
	}

	public static String getNodeValue(HTMLPath parser, String path,
			String pattern) {
		String[] values = parser.SelectNodes(path);

		if (values.length > 0) {
			return extractMatches(pattern, preparePattern(values[0])).split(
					PATTERN_SPLITER)[0];
		} else {
			return "";
		}
	}

	public static String[] getNodeValues(HTMLPath parser, String path,
			String pattern) {
		String[] values = parser.SelectNodes(path);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) {
			if (values.length > 0) {
				values[i] = extractMatches(pattern, preparePattern(values[i]));
				list.addAll(Arrays.asList(values[i].split(PATTERN_SPLITER)[0]));
			} else {
				return new String[] {};
			}
		}

		return list.toArray(new String[] {});
	}

	public static String[] getNodeValues(HTMLPath parser, String path) {
		String[] values = parser.SelectNodes(path);
		if (values.length > 0) {
			{
				return new String[] { values[0] };
			}
		} else {
			return new String[] {};
		}

	}

	public static String[] getAllNodeValues(HTMLPath parser, String path) {
		String[] values = parser.SelectNodes(path);
		if (values.length > 0) {
			{
				return values;
			}
		} else {
			return new String[] {};
		}

	}

	public static String getNodeHTML(HTMLPath parser, String path) {
		String[] values = parser.GetNodeHtml(path);
		// if(values.length > 0)
		// System.out.println(values[0]);
		if (values.length > 0) {
			return values[0];
		} else {
			return "";
		}
	}

	public static String[] getNodeHTMLs(HTMLPath parser, String path) {
		return parser.GetNodeHtml(path);
	}

	public static String[] getValuesFromValue(String value) {
		if (value == null) {
			return new String[] {};
		} else {
			return value.split("PARAMTER_VALUE");
		}
	}

	public static String getValueFromValues(String[] values) {
		StringBuffer sb = new StringBuffer();
		if (values.length > 0) {
			sb.append(values[0]);
		}
		for (int i = 1; i < values.length; i++) {
			sb.append("PARAMTER_VALUE");
			sb.append(values[i]);
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static String[] getUrlsPattern(String template, String[] patterns,
			String[] values) {
		ArrayList<String> list = new ArrayList<String>();
		List<List> llist = new ArrayList<List>();
		for (String value : values) {
			llist.add(Arrays.asList(analyzeValue(value)));
		}
		llist = CombinationUtil.getCombination(llist);
		for (List l : llist) {
			if (l.size() == patterns.length) {
				String url = template;
				for (int i = 0; i < patterns.length; i++) {
					url = url.replace(patterns[i], (String) l.get(i));
				}
				list.add(url);
			}
		}

		return list.toArray(new String[] {});
	}

	@SuppressWarnings("unchecked")
	public static String[] getUrlsPattern(String template,
			String[] autosources, String[] paths, String[] patterns,
			String[] manualsources, String[] values, HTMLPath parser) {
		ArrayList<String> list = new ArrayList<String>();
		List<List> llist = new ArrayList<List>();
		for (String value : values) {
			llist.add(Arrays.asList(analyzeValue(value)));
		}
		for (int i = 0; i < paths.length; i++) {
			String[] vs = HTMLUtil.getNodeValues(parser, paths[i], patterns[i]);
			StringBuffer sb = new StringBuffer();
			for (String v : vs) {
				sb.append(v);
				sb.append(",");
			}
			if (vs.length > 0) {
				llist.add(Arrays.asList(analyzeValue(sb.toString().substring(0,
						sb.toString().length() - 1))));
			}
		}
		llist = CombinationUtil.getCombination(llist);
		ArrayList<String> newsources = new ArrayList<String>();
		newsources.addAll(Arrays.asList(manualsources));
		newsources.addAll(Arrays.asList(autosources));
		for (List l : llist) {
			if (l.size() == newsources.size()) {
				String url = template;
				for (int i = 0; i < newsources.size(); i++) {
					url = url.replace(newsources.get(i), (String) l.get(i));
				}
				list.add(url);
			}
		}

		return list.toArray(new String[] {});
	}

	@SuppressWarnings("unchecked")
	public static String[] getUrlsPattern(String template, String[] sources,
			String[] paths, String[] patterns, HTMLPath parser) {
		ArrayList<String> list = new ArrayList<String>();
		List<List> llist = new ArrayList<List>();
		for (int i = 0; i < paths.length; i++) {
			String[] values = HTMLUtil.getNodeValues(parser, paths[i],
					patterns[i]);
			StringBuffer sb = new StringBuffer();
			for (String v : values) {
				sb.append(v);
				sb.append(",");
			}
			if (values.length > 0) {
				llist.add(Arrays.asList(analyzeValue(sb.toString().substring(0,
						sb.toString().length() - 1))));
			}
		}
		llist = CombinationUtil.getCombination(llist);
		for (List l : llist) {
			if (l.size() == sources.length) {
				String url = template;
				for (int i = 0; i < sources.length; i++) {
					url = url.replace(sources[i], (String) l.get(i));
				}
				list.add(url);
			}
		}

		return list.toArray(new String[] {});
	}

	public static List<String> multiplePattern(String content, String patternStr) {

		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(content);

		List<String> contents = new ArrayList<String>();
		while (matcher.find()) {
			String item = matcher.group(1);

			if (!contents.contains(item)) {
				contents.add(item);
			}
		}
		return contents;

	}

	private static String[] analyzeValue(String value) {
		ArrayList<String> list = new ArrayList<String>();
		String spliter = "spliterspliterspliter";
		value = value.replace("\\,", spliter);
		String[] values = value.split(",");
		for (String va : values) {
			va = va.replace(spliter, ",");
			int div = 1;
			if (va.contains("|")) {
				try {
					div = Integer.parseInt(va.split("\\|")[1]);
					va = va.split("\\|")[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (va.contains("-")) {
				String[] nums = va.split("-");
				if (nums.length != 2) {
					list.add(va);
				} else {
					try {
						int start = Integer.parseInt(nums[0]);
						int end = Integer.parseInt(nums[1]);
						for (int i = start; i <= end; i++) {
							if (i % div == 0) {
								list.add(String.valueOf(i));
							}
						}
					} catch (Exception e) {
						list.add(va);
					}
				}
			} else {
				list.add(va);
			}
		}

		return list.toArray(new String[] {});
	}

	private static String preparePattern(String string) {
		string = string.replace("\r\n", "");
		string = string.replace("\n", "");
		string = string.replace("\r", "");

		return string;
	}

	private static String trimFullSpace(String text) {
		if (text.equals("") || text == null) {
			return text;
		}
		char[] textArray = text.toCharArray();
		if ((int) textArray[0] == 12288
				|| (int) textArray[textArray.length - 1] == 12288) {
			if ((int) textArray[0] == 12288) {
				text = text.substring(1);
			}
			if ((int) textArray[textArray.length - 1] == 12288) {
				text = text.substring(0, text.length() - 1);
			}
			return trimFullSpace(text);
		} else {
			return text;
		}
	}

	public static String extractMatches(String pattern, String content) {
		if (pattern == null || pattern.equals(""))
			return content;
		Matcher matcher = getMatcher(pattern, content);
		StringBuffer matched = new StringBuffer();
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				if (matcher.start(i) > -1) {
					matched.append(matcher.group(i));
				}
			}
			matched.append(PATTERN_SPLITER);
		}

		if (matched.toString().contains(PATTERN_SPLITER)) {
			return matched.toString().substring(0,
					matched.toString().length() - PATTERN_SPLITER.length());
		}

		return matched.toString();
	}

	private static Matcher getMatcher(String pattern, CharSequence input) {
		if (pattern == null) {
			throw new IllegalArgumentException(
					"String 'pattern' must not be null");
		}
		final Map<String, Matcher> matchers = TL_MATCHER_MAP.get();
		Matcher m = (Matcher) matchers.get(pattern);
		if (m == null) {
			m = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(
					input);
		} else {
			matchers.put(pattern, null);
			m.reset(input);
		}
		return m;
	}

	/**
	 * 清除转义字符
	 * 
	 * @param text
	 * @return
	 */
	public static String ridOfRubbish(String text) {
		/* delete all &nbsp */
		text = text.replace(NBSP, "").replace(AMP, "&").replace(QUOT, "")
				.replace(LT, "<").replace(GT, ">");
		/* delete all trailing and leading space */
		text = text.trim();
		/* delete all trailing and leading full space */
		text = trimFullSpace(text);
		return text;
	}

	
}

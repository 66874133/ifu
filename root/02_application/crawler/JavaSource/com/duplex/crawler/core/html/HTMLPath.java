package com.duplex.crawler.core.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.RegexFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.discover.crawler.util.HttpClientUtil;
import com.duplex.crawler.core.html.compress.CompressHandler;
import com.duplex.crawler.core.html.compress.CompressHandlerFactory;
import com.funnel.svc.util.StringUtils;

public class HTMLPath {

	private Parser parser;

	public static final String CHILD_SPLIT = "/";

	public static final String MULTI_CONDITION = "|";

	public static final String PARENT = "parent()";

	public static final String NEXT = "next()";

	public static final String PREVIOUS = "previous()";

	public static final String CHILD = "child()";

	public static final char ESCAPTED_CHILD_SPLIT = '^';

	public String content = "";

	public HTMLPath(String content) {
		try {
			parser = new Parser();
			this.content = content;
			parser.setInputHTML(content);
			parser.setNodeFactory(NodeFactoryMaker.getPrototypicalNodeFactory());
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public HTMLPath(URL url, String charset) {
		try {
			parser = new Parser();
			content = getContent(url.toString(), charset);
			parser.setInputHTML(content);
			parser.setNodeFactory(NodeFactoryMaker.getPrototypicalNodeFactory());
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public void setEncoding(String encoding) {
		try {
			parser.setEncoding(encoding);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String escapeChildSplitInQuotes(String path) {
		if (path == null || !path.contains("/"))
			return path;
		char[] array = path.toCharArray();
		boolean inQuote = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == '\'')
				inQuote = !inQuote;
			if (array[i] == '/') {
				if (inQuote)
					array[i] = ESCAPTED_CHILD_SPLIT;
			}
		}
		return new String(array);
	}

	private void recoverChildSplitInQuotes(String[] conditions) {
		for (int i = 0; i < conditions.length; i++) {
			conditions[i] = conditions[i].replace(ESCAPTED_CHILD_SPLIT, '/');
		}
	}

	public String[] SelectNodes(String path) {
		if (path.contains(MULTI_CONDITION)) {
			String[] paths = path.split("\\" + MULTI_CONDITION);
			ArrayList<String> list = new ArrayList<String>();
			for (String xpath : paths) {
				list.addAll(Arrays.asList(SelectOneConditionNodesValues(xpath)));
			}
			return list.toArray(new String[] {});
		} else {
			return SelectOneConditionNodesValues(path);
		}
	}

	public Node[] SelectOneConditionNodes(String path) {
		parser.reset();
		if (path.equals("") || path == null)
			return new Node[] {};
		path = escapeChildSplitInQuotes(path);
		String[] conditions = path.split(CHILD_SPLIT);
		recoverChildSplitInQuotes(conditions);
		NodeList list = new NodeList();
		int length = conditions.length;
		if (conditions[length - 1].charAt(0) == '@') {
			length = length - 1;
		}
		try {
			for (int i = 0; i < length; i++) {
				if (i == 0) {
					list.add(getNodes(conditions[i]));
					if (conditions[i].contains("[@")) {
						Node[] nodes = list.toNodeArray();
						list.removeAll();
						for (int c = 0; c < nodes.length; c++) {
							if (hasAttribute(nodes[c], conditions[0])) {
								list.add(nodes[c]);
							}
						}
					}
					if (conditions[i].contains("[tag=")) {
						Node[] nodes = list.toNodeArray();
						list.removeAll();
						for (int c = 0; c < nodes.length; c++) {
							if (hasTagName(nodes[c], conditions[0])) {
								list.add(nodes[c]);
							}
						}
					}
					if (conditions[i].contains("[text=")) {
						Node[] nodes = list.toNodeArray();
						list.removeAll();
						for (int c = 0; c < nodes.length; c++) {
							if (hasText(nodes[c], conditions[0])) {
								list.add(nodes[c]);
							}
						}
					}
					if (conditions[i].contains("[order=")) {
						if (getOrder(conditions[i]) <= list.size()) {
							Node[] nodes = list.toNodeArray();
							list.removeAll();
							list.add(nodes[getOrder(conditions[i]) - 1]);
						} else {
							list.removeAll();
						}
					}
					if (conditions[i].contains("[order>")) {
						int size = list.size();
						if (getOrder(conditions[i]) <= list.size()) {
							Node[] nodes = list.toNodeArray();
							list.removeAll();
							for (int o = getOrder(conditions[i]); o < size; o++) {
								list.add(nodes[o]);
							}
						} else {
							list.removeAll();
						}
					}

				} else {
					Node[] nodes = list.toNodeArray();
					list.removeAll();
					for (int j = 0; j < nodes.length; j++) {
						list.add(getConditionNodes(nodes[j], conditions[i]));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list.toNodeArray();
	}

	private String[] SelectOneConditionNodesValues(String path) {
		if (path.equals("") || path == null)
			return new String[] {};
		Node[] finalNodes = SelectOneConditionNodes(path);
		String[] results = new String[finalNodes.length];
		path = escapeChildSplitInQuotes(path);
		String[] conditions = path.split(CHILD_SPLIT);
		recoverChildSplitInQuotes(conditions);
		int length = conditions.length;
		if (conditions[length - 1].charAt(0) == '@') {
			length = length - 1;
		}
		for (int i = 0; i < results.length; i++) {
			if (finalNodes[i] != null) {
				if (conditions.length == length) {
					results[i] = finalNodes[i].toPlainTextString();
				} else {
					if (finalNodes[i] instanceof TagNode) {
						results[i] = ((TagNode) finalNodes[i])
								.getAttribute(conditions[length].substring(1));
					}

				}
			}
		}

		ArrayList<String> urllist = new ArrayList<String>();
		for (String re : results) {
			if (re != null) {
				urllist.add(re);
			}
		}
		return urllist.toArray(new String[] {});
	}

	public String[] GetNodeHtml(String path) {
		ArrayList<String> list = new ArrayList<String>();
		if (path.contains(MULTI_CONDITION)) {
			String[] paths = path.split("\\" + MULTI_CONDITION);
			for (String xpath : paths) {
				Node[] nodes = SelectOneConditionNodes(xpath);
				for (Node node : nodes) {
					if (!StringUtils.isNullOrEmpty(node.toHtml())) {
						list.add(node.toHtml());
					}
				}
			}
		} else {
			Node[] nodes = SelectOneConditionNodes(path);
			for (Node node : nodes) {
				if (node != null && !StringUtils.isNullOrEmpty(node.toHtml())) {
					list.add(node.toHtml());
				}
			}
		}

		return list.toArray(new String[] {});
	}

	private NodeList getConditionNodes(Node node, String condition) {
		NodeList list = new NodeList();
		if (node != null) {
			if (condition.contains(PARENT)) {
				list.add(node.getParent());
			} else if (condition.contains(NEXT)) {
				list.add(getNext(node));
			} else if (condition.contains(PREVIOUS)) {
				list.add(getPrevious(node));
			} else if (condition.contains(CHILD)) {
				list.add(node.getChildren());
			} else {
				list.add(getChildNodes(node, condition));
			}

			if (condition.contains("[@")) {
				Node[] nodes = list.toNodeArray();
				list.removeAll();
				for (int i = 0; i < nodes.length; i++) {
					if (hasAttribute(nodes[i], condition)) {
						list.add(nodes[i]);
					}
				}
			}
			if (condition.contains("[tag=")) {
				Node[] nodes = list.toNodeArray();
				list.removeAll();
				for (int i = 0; i < nodes.length; i++) {
					if (hasTagName(nodes[i], condition)) {
						list.add(nodes[i]);
					}
				}
			}
		}

		return list;
	}

	private Node getPrevious(Node node) {
		Node[] nodes = node.getParent().getChildren().toNodeArray();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].equals(node)) {
				if (i > 0) {
					return nodes[i - 1];
				}
			}
		}

		return null;
	}

	private Node getNext(Node node) {
		Node[] nodes = node.getParent().getChildren().toNodeArray();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].equals(node)) {
				i++;
				while (i < nodes.length - 1) {
					if (HTMLUtil.ridOfRubbish(nodes[i].getText()).equals(""))
						i++;
					else
						return nodes[i];
				}
			}
		}

		return null;
	}

	private NodeList getNodes(String condition) {
		String tagName;
		if (condition.contains("[@")) {
			tagName = condition.substring(0, condition.indexOf("[@"));
		} else if (condition.contains("[order")) {
			tagName = condition.substring(0, condition.indexOf("[order"));
		} else {
			tagName = condition;
		}
		NodeFilter filter;
		if (!tagName.contains("[text=")) {
			filter = new TagNameFilter(tagName);

		} else {
			String text = tagName.substring(7, tagName.length() - 2);
			filter = new RegexFilter(text);
		}
		try {
			NodeList list = parser.extractAllNodesThatMatch(filter);
			return list;
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		}
	}

	private NodeList getChildNodes(Node node, String condition) {
		String tagName;
		if (condition.contains("[")) {
			tagName = condition.substring(0, condition.indexOf("["));
		} else {
			tagName = condition;
		}

		NodeList list = new NodeList();
		if (node != null && node.getChildren() != null) {
			Node[] nodes = node.getChildren().toNodeArray();
			if (!condition.contains("[text=")) {
				TagNameFilter filter = new TagNameFilter(tagName);
				for (int i = 0; i < nodes.length; i++) {
					if (filter.accept((nodes[i])) || "alltag".equals(tagName)) {
						list.add(nodes[i]);
					}
				}
				if (condition.contains("[order=")) {
					if (getOrder(condition) <= list.size()) {
						nodes = list.toNodeArray();
						list.removeAll();
						list.add(nodes[getOrder(condition) - 1]);
					} else {
						list.removeAll();
					}
				}
				if (condition.contains("[order>")) {
					int size = list.size();
					if (getOrder(condition) <= list.size()) {
						nodes = list.toNodeArray();
						list.removeAll();
						for (int o = getOrder(condition); o < size; o++) {
							list.add(nodes[o]);
						}
					} else {
						list.removeAll();
					}
				}

			} else {
				RegexFilter filter = new RegexFilter(
						condition.substring(condition.indexOf("text=") + 6,
								condition.length() - 2), RegexFilter.FIND);

				for (int i = 0; i < nodes.length; i++) {
					if (filter.accept(nodes[i])) {
						list.add(nodes[i]);
					}
				}
			}
		}
		return list;
	}

	private String[] getAttributeName(String condition) {
		ArrayList<String> list = new ArrayList<String>();
		int position = -1;
		while (true) {
			position = condition.indexOf("[@", position) + 1;
			if (position == 0)
				break;
			list.add(condition.substring(position + 1,
					condition.indexOf("='", position)));
		}

		return list.toArray(new String[] {});
	}

	private String getTagName(String condition) {
		ArrayList<String> list = new ArrayList<String>();
		int position = -1;
		while (true) {
			position = condition.indexOf("[tag='", position) + 1;
			if (position == 0)
				break;
			list.add(condition.substring(position + 5,
					condition.indexOf("']", position)));
		}

		return list.toArray(new String[] {})[0];
	}

	private String getText(String condition) {
		ArrayList<String> list = new ArrayList<String>();
		int position = -1;
		while (true) {
			position = condition.indexOf("[text='", position) + 1;
			if (position == 0)
				break;
			list.add(condition.substring(position + 6,
					condition.indexOf("']", position)));
		}

		return list.toArray(new String[] {})[0];
	}

	private String[] getAttributeValue(String condition) {
		ArrayList<String> list = new ArrayList<String>();
		// int position = -1;
		String[] cons = condition.split("\\[");
		for (String con : cons) {
			if (con.startsWith("@") && con.contains("='")) {
				list.add(con
						.substring(con.indexOf("='") + 2, con.indexOf("']"))
						.trim());
			}
		}
		// while (true) {
		// position = condition.indexOf("='", position) + 1;
		// // 去掉[@class='asdf=']的后一个=号
		// if (position == 0 || position == condition.length() - 2)
		// break;
		// list.add(condition.substring(position + 1, condition.indexOf("']",
		// position)));
		// }

		return list.toArray(new String[] {});
	}

	private boolean hasAttribute(Node node, String condition) {
		String[] name = getAttributeName(condition);
		String[] value = getAttributeValue(condition);
		HasAttributeFilter[] filters = new HasAttributeFilter[name.length];
		for (int i = 0; i < name.length; i++) {
			filters[i] = new HasAttributeFilter(name[i], value[i]);
			if (!filters[i].accept(node)) {
				return false;
			}
		}

		return true;
	}

	private boolean hasTagName(Node node, String condition) {
		String tagName = getTagName(condition);
		TagNameFilter filter = new TagNameFilter(tagName);

		return filter.accept(node);
	}

	private boolean hasText(Node node, String condition) {
		String text = getText(condition);
		RegexFilter filter = new RegexFilter(text);

		return filter.accept(node);
	}

	private int getOrder(String condition) {
		return Integer.parseInt(condition.substring(
				condition.indexOf("[order") + 8,
				condition.indexOf("[order") + 9));
	}

	public String getContent() {
		return content;
	}

	public static String getContent(String destUrl, String charset) {
		HttpClient hc = HttpClientUtil.getHttpClient();

		HttpMethod httpGet = new GetMethod(destUrl);

		// PostMethod method = new PostMethod(destUrl);
		// method.setFollowRedirects(true);
		// method.setRequestHeader("Accept-Encoding", "gzip, deflate");
		try {
			int state = hc.executeMethod(httpGet);
			String str = "";
			if (200 == state) {
				byte[] responseBody = httpGet.getResponseBody();

				Header[] heads = httpGet.getRequestHeaders("Content-Encoding");
				if (null != heads && heads.length > 0 && heads[0] != null) {
					String acceptEncoding = httpGet
							.getRequestHeaders("Content-Encoding")[0]
							.getValue();
					CompressHandler compressHandler = CompressHandlerFactory
							.getCompressHandler(acceptEncoding);

					if (null != compressHandler) {
						try {
							responseBody = compressHandler.handle(responseBody);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				str = new String(responseBody, charset);
			}

			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}

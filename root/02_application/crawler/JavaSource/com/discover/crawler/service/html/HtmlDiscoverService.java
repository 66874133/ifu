package com.discover.crawler.service.html;

import java.net.URLDecoder;
import java.util.List;

import com.discover.crawler.util.JsonUtil;
import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.executor.HttpFetcherClient;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

public class HtmlDiscoverService extends SyncService {


	@Override
	public String getSvcCode() {
		return "htmlDiscoverService";
	}

	@Override
	public String getSvcDesc() {
		return "爬虫辅助工具服务";
	}

	@Override
	public void process(SvcContext context) {
		logger.info("context.getRequestData()=" + context.getRequestData());
		try {
			HtmlDiscoverData discoverData = new HtmlDiscoverData();
			discoverData.setContent("HtmlDiscoverService is ok!");

			String url = (String) context.getRequestData().get("url");

			url = URLDecoder.decode(url, "utf-8");

			String encode = (String) context.getRequestData().get("charset");
			encode = URLDecoder.decode(encode, "utf-8");
			String xpath = (String) context.getRequestData().get("xpath");
			xpath = URLDecoder.decode(xpath, "utf-8");
			String pattern = (String) context.getRequestData().get("pattern");
			pattern = URLDecoder.decode(pattern, "utf-8");
			String jsonField = (String) context.getRequestData().get("jsonField");
			jsonField = URLDecoder.decode(jsonField, "utf-8");
			String type = (String) context.getRequestData().get("type");
			String useProxy = (String) context.getRequestData().get("useProxy");
			String proxyIp = (String) context.getRequestData().get("proxyIp");
			String proxyPort = (String) context.getRequestData().get(
					"proxyPort");
			String referer = (String) context.getRequestData().get("referer");
			referer = URLDecoder.decode(referer, "utf-8");
			String[] result = new String[0];
			String content = "";
			if ("true".equalsIgnoreCase(useProxy)) {
				HttpFetcherClient client = new HttpFetcherClient();
				content = client.doCrawlerByProxy(url, encode, "test", proxyIp,
						proxyPort);

			} else {
				HttpFetcherClient client = new HttpFetcherClient();
				content = client.doCrawler(url, encode, referer);
			}

			if ("1".equalsIgnoreCase(type)) {
				HTMLPath parser = new HTMLPath(content);
				result = HTMLUtil.getNodeValues(parser, xpath, pattern);
			} else if ("2".equalsIgnoreCase(type)) {
				result = RegularExpressionUtil
						.getPatternValue(content, pattern);
			} else {
				List<String> values = JsonUtil.getValues(jsonField, content);
				result = values.toArray(new String[0]);
			}

			discoverData.setResult(result);
			discoverData.setCount(result.length);
			discoverData.setContent2(content);
			context.setResponseData(discoverData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class HtmlDiscoverData {
		private String content;
		/**
		 * 原始内容
		 */
		private String content2;
		private int count;

		private String[] result;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String[] getResult() {
			return result;
		}

		public void setResult(String[] result) {
			this.result = result;
		}

		public String getContent2() {
			return content2;
		}

		public void setContent2(String content2) {
			this.content2 = content2;
		}

	}

	public static void main(String[] args) throws Exception {
		HttpFetcherClient client = new HttpFetcherClient();
		String content = client.doCrawler("https://cd.lianjia.com/ershoufang/106100553749.html", "utf-8", "");
		System.out.println(content);
	}
}

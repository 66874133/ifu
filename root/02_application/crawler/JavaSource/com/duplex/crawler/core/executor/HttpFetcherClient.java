package com.duplex.crawler.core.executor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.discover.crawler.util.HttpClientUtil;
import com.discover.crawler.util.ProxyIpUtil;
import com.discover.crawler.util.UserAgentUtil;
import com.duplex.crawler.CrawlerConstant;
import com.funnel.svc.SvcException;
import com.funnel.svc.util.HttpUtil;
import com.funnel.svc.util.UrlUtil;

public class HttpFetcherClient {

	private final static Log logger = LogFactory
			.getLog(HttpFetcherClient.class);

	public synchronized String doCrawler(String url, String encode,
			String referer) throws Exception {
		Map<String, Object> reqParams = buildReqParams(url, encode, referer);
		Exception lastException = null;
		for (int i = 0; i < CrawlerConstant.CRAWLER_RETY_NUM; i++) {
			try {
				String htmlStr = HttpUtil.getRequest(reqParams);
				Thread.sleep(1000);
				return htmlStr;
			} catch (Exception e) {
				lastException = e;
				//
				// if (e.getLocalizedMessage().contains("403")) {
				// logger.error("抓取天猫商品,url:" + url + " 失败,本地ip已被封,尝试使用代理",
				// lastException);
				// try {
				// doCrawlerByProxy(url, encode);
				// } catch (Exception e1) {
				// lastException = e1;
				// }
				// }
			}
		}
		if (null != lastException) {
			logger.error("抓取天猫商品,url:" + url + " 失败", lastException);
		}
		return "";
	}

	public String doCrawlerByProxy(String url, String encode, String modeName)
			throws Exception {

		Map<String, Object> ipInfoMap = ProxyIpUtil.getProxyIp();

		return doCrawlerByProxy(url, encode, modeName, ipInfoMap.get("ip")
				.toString(), ipInfoMap.get("port").toString());

	}

	public String doCrawlerByProxy(String url, String encode, String modeName,
			String proxyHost, String proxyPort) throws Exception {

		HttpMethod method = new GetMethod(url);
		String content = "";
		HttpClient httpClient = HttpClientUtil.getHttpClient(modeName, 50, 500,
				5000, 60000);
		httpClient.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
		if (null != proxyHost && null != proxyPort) {
			// 设置代理服务器的ip地址和端口
			httpClient.getHostConfiguration().setProxy(proxyHost,
					Integer.parseInt(proxyPort));
			// 使用抢先认证
			httpClient.getParams().setAuthenticationPreemptive(true);
		}

		try {
			int state = httpClient.executeMethod(method);
			if (200 == state) {
				content = method.getResponseBodyAsString();
				logger.info("使用代理ip:" + proxyHost + " 端口:" + proxyPort + " 请求:"
						+ url + " 响应:" + content);

			}
		} catch (Exception e) {
			throw new SvcException("使用代理请求失败", e);
		} finally {
			method.releaseConnection();
		}
		return content;
	}

	private static Map<String, Object> buildReqParams(String url,
			String encode, String referer) throws Exception {
		Map<String, Object> reqParams = new HashMap<String, Object>();
		// 使用连接池
		reqParams.put(HttpUtil.ClIENT_KEY, "crawlerPool");
		reqParams.put(HttpUtil.CONNECTION_TIME_OUT,
				CrawlerConstant.CRAWLER_CONNECTION_TIMEOUT);
		reqParams.put(HttpUtil.READ_TIME_OUT,
				CrawlerConstant.CRAWLER_READ_TIMEOUT);
		reqParams.put(HttpUtil.REQ_CHARSET_PARAM, encode);
		// reqParams.put(HttpUtil.REQ_URL_PARAM, URLEncoder.encode(url,
		// "utf-8"));
		reqParams.put(HttpUtil.REQ_URL_PARAM, UrlUtil.encodeUrlParameter(url));

		Map<String, String> heads = new HashMap<String, String>();
		heads.put(HttpMethodParams.USER_AGENT, UserAgentUtil.getUserAgent());
		heads.put(HttpUtil.REQ_REFERER_PARAM, referer);

		// heads.put(HttpUtil.REQ_COOKIE_PARAM,
		// "welcomeBannerShowed=2; PHPSESSID=fl1dlj0c8es2r2sc4ut26t0dr2; amvid=e7f1edc06952422fc8506780e99edb60; Hm_lvt_4685836040bee0a1bec411ef8520188f=1469428575,1470794925,1470815715,1471242941; Hm_lpvt_4685836040bee0a1bec411ef8520188f=1471422986");
		reqParams.put(HttpUtil.REQ_HEADER_PARAM, heads);

		return reqParams;
	}
}

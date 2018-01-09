package com.funnel.svc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import com.funnel.svc.config.ServiceConf;
import com.funnel.svc.util.HttpUtil;
import com.funnel.svc.util.JsonUtil;
import com.funnel.svc.util.StringUtils;

public class CallServiceUtil {
	protected static final Logger logger = Logger
			.getLogger(CallServiceUtil.class);
	private static final AtomicInteger regCenterSeq = new AtomicInteger();
	private static final Map<String, AtomicInteger> svcSeqMap = new HashMap<String, AtomicInteger>();

	/**
	 * 根据服务码获取服务地址
	 * 
	 * @param svcCode
	 * @return
	 */
	public static String getSvcUrlBySvcCode(String svcCode) {
		String[] svcRegCenterUrls = ServiceConf.getSvcRegCenterUrl().split(",");
		int m = regCenterSeq.incrementAndGet() % svcRegCenterUrls.length;
		String msg = "";
		JSONObject requestObj = new JSONObject();
		requestObj.put("svcCode", svcCode);
		for (int i = 0, j = m; i < svcRegCenterUrls.length; ++i) {
			String url = svcRegCenterUrls[j];
			try {
				String respText = callSvcWithSigleMode(url, requestObj);
				if (!StringUtils.hasText(respText)) {
					throw new SvcException("通过url:" + url + " 获取服务:" + svcCode
							+ " 地址失败，注册中心响应为空");
				}
				JSONObject resp = JsonUtil.toJSONObject(respText);
				if (null == resp) {
					throw new SvcException("通过url:" + url + " 获取服务:" + svcCode
							+ " 地址失败，注册中心响应:" + respText);
				}
				String svcUrlsStr = resp.getAsString("urls");
				if (!StringUtils.hasText(svcUrlsStr)) {
					throw new SvcException("通过url:" + url + " 获取服务:" + svcCode
							+ " 地址失败，注册中心响应:" + respText);
				}
				return svcUrlsStr;
			} catch (Exception ex) {
				// 当一个主机不可用时就尝试另一个主机
				if (++j == svcRegCenterUrls.length)
					j = 0;
				msg = msg + "通过url：" + url + "获取服务:" + svcCode + " 地址失败"
						+ ex.getMessage() + " 重试: ";
			}
		}

		throw new SvcException(Constant.ERROR_CODE_SYS_ERROR, msg);
	}

	/**
	 * 调用远程服务
	 */
	public static JSONObject callRemoteSvc(String svcCode, JSONObject reqMsgObj) {
		String urlStr = getSvcUrlBySvcCode(svcCode);
		String responseText = callRemoteSvcByLoadBanlance(svcCode, urlStr,
				reqMsgObj);
		if (!StringUtils.hasText(responseText)) {
			throw new SvcException("调用远程服务失败, 响应为空");
		}
		JSONObject resp = JsonUtil.toJSONObject(responseText);
		if (null == resp) {
			throw new SvcException("调用服务:" + svcCode + " 失败，响应:" + responseText);
		}
		return resp;
	}

	public static JSONObject callRemoteSvc(String svcCode, SvcContext context) {
		String urlStr = getSvcUrlBySvcCode(svcCode);
		String responseText = callRemoteSvcByLoadBanlance(svcCode, urlStr,
				context.getRequestData());
		if (!StringUtils.hasText(responseText)) {
			throw new SvcException("调用远程服务失败, 响应为空");
		}
		JSONObject resp = JsonUtil.toJSONObject(responseText);
		if (null == resp) {
			throw new SvcException("调用服务:" + svcCode + " 失败，响应:" + responseText);
		}
		return resp;
	}

	/**
	 * 负载均衡调用服务
	 * 
	 * @param serverInfo
	 * @return
	 */
	private static String callRemoteSvcByLoadBanlance(String svcCode,
			String urlStr, JSONObject reqMsgObj) {
		String[] urls = urlStr.split(",");
		if (urls.length > 1) {
			return callSvcWithLoadBanlanceMode(svcCode, urls, reqMsgObj);
		} else {
			String url = urls[0];
			return callSvcWithSigleMode(url, reqMsgObj);
		}
	}

	/**
	 * 以负载+容错模式调用服务
	 * 
	 * @param serviceInfo
	 * @param urls
	 * @param reqMsg
	 * @return
	 */
	private static String callSvcWithLoadBanlanceMode(String svcCode,
			String[] urls, JSONObject reqMsgObj) {
		int m = getSvcSeq(svcCode).incrementAndGet() % urls.length;
		String msg = "";
		for (int i = 0, j = m; i < urls.length; ++i) {
			String url = urls[j];
			try {
				return callSvcWithSigleMode(url, reqMsgObj);
			} catch (Exception ex) {
				// 当一个主机不可用时就尝试另一个主机
				if (++j == urls.length)
					j = 0;
				msg = msg + "通过url：" + url + "调用服务失败: " + ex.getMessage()
						+ " 重试: ";
			}
		}

		throw new SvcException(Constant.ERROR_CODE_SYS_ERROR, msg);
	}

	private static AtomicInteger getSvcSeq(String svcCode) {
		if (null == svcSeqMap.get(svcCode)) {
			svcSeqMap.put(svcCode, new AtomicInteger());
		}
		return svcSeqMap.get(svcCode);
	}

	/**
	 * 单个url直接调用
	 * 
	 * @param url
	 * @param reqMsg
	 * @return
	 */
	private static String callSvcWithSigleMode(String url, JSONObject reqMsg) {
		Map<Object, Object> reqParams = new HashMap<Object, Object>();
		reqParams.put(HttpUtil.REQ_DATA_PARAM, reqMsg);
		try {
			reqParams.put(HttpUtil.REQ_URL_PARAM, url);

			reqParams.put(HttpUtil.ClIENT_KEY, "svcClient");
			reqParams.put(HttpUtil.CONNECTION_TIME_OUT, 10000);
			reqParams.put(HttpUtil.READ_TIME_OUT, 300000);

			return HttpUtil.postRequest(reqParams);
		} catch (Exception ex) {
			logger.error("通过url：" + url + "调用服务失败", ex);
			throw new SvcException(Constant.ERROR_CODE_SYS_ERROR, "通过url："
					+ url + "调用服务失败: " + ex.getMessage());
		}
	}
}

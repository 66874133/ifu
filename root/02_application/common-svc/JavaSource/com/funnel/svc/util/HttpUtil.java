package com.funnel.svc.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.funnel.svc.SvcException;
import com.funnel.svc.http.compress.CompressHandler;
import com.funnel.svc.http.compress.CompressHandlerFactory;

/**
 * http请求发送工具类<br>
 * 创建人: wanghua4 <br>
 * 创建时间:2014-7-1
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HttpUtil {
	private static Logger logger = Logger.getLogger(HttpUtil.class);

	// url参数名
	public final static String REQ_URL_PARAM = "url";

	// 请求数据参数名
	public final static String REQ_DATA_PARAM = "reqData";
	// header参数名
	public final static String REQ_HEADER_PARAM = "header";

	// contentType参数名
	public final static String REQ_CONTENT_TYPE_PARAM = "Content-Type";

	// contentType参数名
	public final static String KEY_VALUE_PARAM = "keyValue";

	// 请求方式
	public final static String REQ_METHOD_PARAM = "reqMethod";

	// 请求方式post
	public final static String REQ_METHOD_POST = "post";

	// 请求方式get
	public final static String REQ_METHOD_GET = "get";

	// 响应流拦截处理器
	public final static String RESP_STREAM_HANDLER = "respStreamHandler";

	// 字符集参数名
	public final static String REQ_CHARSET_PARAM = "charset";
	// 字符集参数名
	public final static String REQ_REFERER_PARAM = "Referer";

	public final static String REQ_COOKIE_PARAM = "Cookie";

	public final static String REQ_ACCEPT_ENCODING_PARAM = "Accept-Encoding";

	public final static String CONNECTION_TIME_OUT = "connectionTimeOut";

	public final static String READ_TIME_OUT = "readTimeOut";

	public final static String ClIENT_KEY = "clientKey";

	private static HttpClient defaultHttpClient;

	private static Map<String, HttpClient> clientMap = new HashMap<String, HttpClient>();

	/**
	 * 以指定方式 post或get发送http请求， 返回结果
	 * 
	 * @param reqParams
	 *            请求参数
	 * @return 响应结果
	 */
	public static String request(Map reqParams) throws Exception {
		HttpMethod method = null;
		String resp = null;
		try {
			long t = System.currentTimeMillis();
			defaultReqParams(reqParams);
			method = createMethod(reqParams);
			HttpClient httpClient = createHttpClient(reqParams);
			if (null != reqParams.get(RESP_STREAM_HANDLER)) {
				resp = callHttpAndPorcessResultWithHandler(httpClient, method,
						reqParams.get(REQ_CHARSET_PARAM).toString(),
						(ResponseStreamHandler) reqParams
								.get(RESP_STREAM_HANDLER));
			} else {
				resp = callHttpAndRetrunStrResutl(httpClient, method, reqParams
						.get(REQ_CHARSET_PARAM).toString());
			}
			if ((System.currentTimeMillis() - t) > 30000) {
				logger.info("调用ur:" + reqParams.get(REQ_URL_PARAM) + " 耗时:"
						+ (System.currentTimeMillis() - t));
			}
			return resp;
		} finally {
			if (null != method) {// 使用连接池，要释放
				method.releaseConnection();
			}
		}
	}

	/**
	 * 发送http请求，post方式 返回结果
	 * 
	 * @param reqParams
	 *            请求参数
	 * @return 响应结果
	 */
	public static String postRequest(Map reqParams) throws Exception {
		reqParams.put(REQ_METHOD_PARAM, REQ_METHOD_POST);
		return request(reqParams);
	}

	/**
	 * 发送http请求，get方式 返回结果
	 * 
	 * @param reqParams
	 *            请求参数
	 * @return 响应结果
	 */
	public static String getRequest(Map reqParams) throws Exception {
		reqParams.put(REQ_METHOD_PARAM, REQ_METHOD_GET);
		return request(reqParams);
	}

	/**
	 * 设置默认的请求参数
	 * 
	 * @param reqParams
	 *            请求参数对象
	 */
	private static void defaultReqParams(Map reqParams) throws Exception {
		if (!StringUtils.hasText((String) reqParams.get(REQ_URL_PARAM))) {
			throw new Exception("HTTP请求URL不能为空");
		}
		if (null == reqParams.get(REQ_DATA_PARAM)) {
			reqParams.put(REQ_DATA_PARAM, "");
		}
		if (!StringUtils.hasText((String) reqParams.get(REQ_CHARSET_PARAM))) {
			reqParams.put(REQ_CHARSET_PARAM, Charset.defaultCharset()
					.toString());
		}
		if (null == reqParams.get(KEY_VALUE_PARAM)) {
			reqParams.put(KEY_VALUE_PARAM, false);
		}
		if (null == reqParams.get(REQ_METHOD_PARAM)) {
			reqParams.put(REQ_METHOD_PARAM, REQ_METHOD_POST);
		}
	}

	/**
	 * 根据制度
	 * 
	 * @param reqParams
	 * @return
	 * @throws Exception
	 */
	private static HttpMethod createMethod(Map reqParams) throws Exception {
		HttpMethod method = null;
		String url = (String) reqParams.get(REQ_URL_PARAM);
		String contentType = (String) reqParams.get(REQ_CONTENT_TYPE_PARAM);
		String charset = (String) reqParams.get(REQ_CHARSET_PARAM);
		String referer = (String) reqParams.get(REQ_REFERER_PARAM);
		String cookie = (String) reqParams.get(REQ_COOKIE_PARAM);
		String acceptEncoding = (String) reqParams
				.get(REQ_ACCEPT_ENCODING_PARAM);
		if (REQ_METHOD_POST.equals(reqParams.get(REQ_METHOD_PARAM))) {
			method = new PostMethod(url);
			Object postData = buildHttpReqData(
					(Boolean) reqParams.get(KEY_VALUE_PARAM),
					reqParams.get(REQ_DATA_PARAM), contentType, charset);
			if (postData instanceof StringRequestEntity) {
				((PostMethod) method)
						.setRequestEntity((StringRequestEntity) postData);
			} else {
				((PostMethod) method)
						.setRequestBody((NameValuePair[]) postData);
			}
		} else {
			method = new GetMethod(url);
			Object getData = buildHttpReqData(true,
					reqParams.get(REQ_DATA_PARAM), contentType, charset);
			if (getData instanceof NameValuePair[]) {
				((GetMethod) method).setQueryString((NameValuePair[]) getData);
			}
		}
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				charset);
		method.addRequestHeader("Accept-Charset", charset);
		if (StringUtils.hasText(contentType)) {
			method.addRequestHeader("Content-Type", contentType);
		}
		if (StringUtils.hasText(referer)) {
			method.addRequestHeader(REQ_REFERER_PARAM, referer);
		}
		if (StringUtils.hasText(cookie)) {
			method.addRequestHeader(REQ_COOKIE_PARAM, cookie);
		}
		if (StringUtils.hasText(acceptEncoding)) {
			method.addRequestHeader(REQ_ACCEPT_ENCODING_PARAM, acceptEncoding);
		}
		if (null != reqParams.get(REQ_HEADER_PARAM)) {
			Map<String, Object> headerMap = (Map<String, Object>) reqParams
					.get(REQ_HEADER_PARAM);
			for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
				method.addRequestHeader(entry.getKey(), entry.getValue()
						.toString());
			}
		}
		return method;
	}

	/**
	 * 创建http请求参数
	 * 
	 * @param keyValueParam
	 * @param reqData
	 * @param contentType
	 * @param charset
	 * @throws Exception
	 */
	private static Object buildHttpReqData(boolean keyValueParam,
			Object reqData, String contentType, String charset)
			throws Exception {
		if (true == keyValueParam) {
			if (null == reqData || "".equals(reqData)) {
				return null;
			}
			// 使用key-value时，业务数据必须为json格式字符串,javaBean,JsonObject
			Map reqDataMap = JsonUtil.toJSONObject(reqData);
			if (null != reqDataMap && !reqDataMap.isEmpty()) {
				NameValuePair[] data = new NameValuePair[reqDataMap.keySet()
						.size()];
				Iterator it = reqDataMap.entrySet().iterator();
				int i = 0;
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (null != key && null != value) {
						data[i] = new NameValuePair(key.toString(),
								value.toString());
					}
					i++;
				}
				return data;
			}
			return null;
		} else {
			// 不是key-value时，可以是任意字符串，或javaBean,JsonObject
			if (reqData instanceof String) {
				return new StringRequestEntity((String) reqData, contentType,
						charset);
			} else {
				return new StringRequestEntity(JsonUtil.toJSONString(reqData),
						contentType, charset);
			}
		}
	}

	/**
	 * 创建HttpClient对象
	 * 
	 * @param reqParams
	 *            请求参数对象
	 * @return HttpClient
	 */
	private static HttpClient createHttpClient(Map reqParams) {
		if (null != reqParams.get(ClIENT_KEY)) {
			if (null != clientMap.get(reqParams.get(ClIENT_KEY))) {
				return clientMap.get(reqParams.get(ClIENT_KEY));
			} else {
				clientMap.put((String) reqParams.get(ClIENT_KEY),
						new HttpClient(createConnectionMgr(reqParams)));
				return clientMap.get(reqParams.get(ClIENT_KEY));
			}
		}
		if (null == defaultHttpClient) {
			createDefaultClient();
		}
		return defaultHttpClient;
	}

	private static MultiThreadedHttpConnectionManager createConnectionMgr(
			Map reqParams) {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();// http连接池
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(200);
		connectionManager.getParams().setMaxTotalConnections(1000);
		connectionManager.getParams().setConnectionTimeout(
				null == reqParams.get(CONNECTION_TIME_OUT) ? 3000
						: Integer.parseInt(reqParams.get(CONNECTION_TIME_OUT)
								.toString()));
		connectionManager.getParams().setSoTimeout(
				null == reqParams.get(READ_TIME_OUT) ? 5000 : Integer
						.parseInt(reqParams.get(READ_TIME_OUT).toString()));
		return connectionManager;
	}

	private static void createDefaultClient() {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();// http连接池
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(50);
		connectionManager.getParams().setMaxTotalConnections(200);
		connectionManager.getParams().setConnectionTimeout(5000);
		connectionManager.getParams().setSoTimeout(60000);
		defaultHttpClient = new HttpClient(connectionManager);
	}

	/**
	 * 发送请求，读取响应结果并返回
	 * 
	 * @param httpClient
	 * @param post
	 * @return 响应结果字符串
	 */
	private static String callHttpAndRetrunStrResutl(HttpClient httpClient,
			HttpMethod method, String charset) throws Exception {
		int status = httpClient.executeMethod(method);
		if (200 == status) {
			byte[] bytes = method.getResponseBody();
			if (method.getResponseHeader("Content-Encoding") != null
					&& method.getResponseHeader("Content-Encoding").getValue()
							.toLowerCase().indexOf("gzip") > -1) {
				String acceptEncoding = method.getResponseHeader(
						"Content-Encoding").getValue();
				CompressHandler compressHandler = CompressHandlerFactory
						.getCompressHandler(acceptEncoding);
				if (null != compressHandler) {
					try {
						bytes = compressHandler.handle(bytes);
					} catch (Exception e) {
						throw new SvcException("httpState" + status,
								"HTTP请求失败，状态码：" + status);
					}
				}

			}

			if (method.getResponseHeader("Content-Type") != null) {
				if (method.getResponseHeader("Content-Type").getValue()
						.toLowerCase().indexOf("gbk") > -1) {
					return new String(bytes, "GBK");
				} else {
					return new String(bytes, "UTF-8");
				}

			} else {
				return new String(bytes, charset);
			}

		}
		return "";
	}

	// private static String callHttpAndRetrunStrResutl(HttpClient httpClient,
	// HttpMethod method, String charset)
	// throws Exception
	// {
	// int status = httpClient.executeMethod(method);
	// if (200 == status)
	// {
	// InputStream ins = null;
	// BufferedReader br = null;
	// try
	// {
	// ins = method.getResponseBodyAsStream();
	// // 按指定的字符集构建文件流
	// br = new BufferedReader(new InputStreamReader(ins, charset));
	// StringBuffer sbf = new StringBuffer();
	// String line = null;
	// while ((line = br.readLine()) != null)
	// {
	// sbf.append(line);
	// }
	//
	// return sbf.toString();
	// }
	// finally
	// {
	// if (ins != null)
	// {
	// ins.close();
	// }
	// if (br != null)
	// {
	// br.close();
	// }
	// }
	// }
	// else
	// {
	// throw new SvcException("httpState" + status, "HTTP请求失败，状态码：" + status);
	// }
	// }

	/**
	 * 发送http请求，并调用流处理器处理结果
	 * 
	 * @param httpClient
	 * @param post
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	private static String callHttpAndPorcessResultWithHandler(
			HttpClient httpClient, HttpMethod method, String charset,
			ResponseStreamHandler responseStreamHandler) throws Exception {
		int status = httpClient.executeMethod(method);
		if (200 == status) {
			InputStream ins = null;
			try {
				long total = 0;
				int n = 0;
				ins = method.getResponseBodyAsStream();

				long totalLength = 0;
				if (method instanceof PostMethod) {
					totalLength = ((PostMethod) method)
							.getResponseContentLength();
				} else {
					totalLength = ((GetMethod) method)
							.getResponseContentLength();
				}
				byte[] buffer = new byte[1024 * 8];
				responseStreamHandler.init(totalLength);
				while ((n = ins.read(buffer)) >= 0) {
					total += n;
					responseStreamHandler.process(buffer, 0, n, totalLength);
				}

				return "" + total;
			} finally {
				if (ins != null) {
					ins.close();
				}
				responseStreamHandler.destory();
			}
		} else {
			throw new SvcException("httpState" + status, "HTTP请求失败，状态码："
					+ status);
		}
	}

	public interface ResponseStreamHandler {
		public void init(long downFileSize);

		public void process(byte[] b, int off, int len, long totalSize);

		public void destory();
	}
}

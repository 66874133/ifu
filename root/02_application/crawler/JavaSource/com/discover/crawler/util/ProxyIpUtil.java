package com.discover.crawler.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.funnel.svc.util.StringUtils;

public class ProxyIpUtil {
	private static final Log logger = LogFactory.getLog(ProxyIpUtil.class);
	private static List<String> validIps = Collections
			.synchronizedList(new ArrayList<String>());
	private static List<String> validErrorIps = Collections
			.synchronizedList(new ArrayList<String>());
	private static List<String> fromError2ValidIps = Collections
			.synchronizedList(new ArrayList<String>());
	private static String proxyIpFilePath;
	static Properties proxyIpProp = new Properties();
	private static boolean proxyIpCrawlerThreadRunFlag = true;

	public void setProxyIpFilePath(String proxyIpFilePath) {
		ProxyIpUtil.proxyIpFilePath = proxyIpFilePath;
	}

	public static void main(String[] args) {
		ProxyIpUtil.get89ipProxyIps();

	}

	public void init() {
		InputStream in = null;
		try {
			in = new FileInputStream(proxyIpFilePath);
			proxyIpProp.load(in);
			String validsIpsStr = proxyIpProp.getProperty("validsIps");
			if (StringUtils.hasText(validsIpsStr)) {
				String[] validIps = validsIpsStr.split(",");
				for (String validIp : validIps) {
					if (validIp.contains(":")) {
						ProxyIpUtil.validIps.add(validIp);
					}
				}
				logger.info("装载有效的代理ip个数:" + ProxyIpUtil.validIps.size());
			}
			String validErrorIpsStr = proxyIpProp.getProperty("validErrorIps");
			if (StringUtils.hasText(validErrorIpsStr)) {
				String[] validErrorIps = validErrorIpsStr.split(",");
				for (String validErrorIp : validErrorIps) {
					if (validErrorIp.contains(":")) {
						ProxyIpUtil.validErrorIps.add(validErrorIp);
					}
				}
				logger.info("装载无效效的代理ip个数:" + ProxyIpUtil.validErrorIps.size());
			}

		} catch (IOException e) {
			logger.error("装载代理ip失败", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error("关闭代理ip属性文件失败", e);
			}
		}

		new Thread(new Runnable() {

			public void run() {
				while (proxyIpCrawlerThreadRunFlag) {
					logger.info("从验证失败ip中，再次验证成功的ip数量:"
							+ fromError2ValidIps.size());
					for (String ip : fromError2ValidIps) {
						if (!validIps.contains(ip)) {
							validIps.add(ip);
						}
					}
					fromError2ValidIps.clear();
					logger.info("开始对当前有效的ip做验证");
					ProxyIpUtil.testValidIps();
					logger.info("开始从89ip获取代理ip");
					ProxyIpUtil.get89ipProxyIps();
					logger.info("开始保存获取的代理ip");
					saveProxyIps();
					logger.info("保存完成");
					try {
						Thread.sleep(600000);
					} catch (InterruptedException e) {
						logger.error("代理ip抓取线程睡眠失败");
					}
				}

			}
		}).start();
		// 验证失败的ip再次验证
		new Thread(new Runnable() {
			public void run() {
				while (proxyIpCrawlerThreadRunFlag) {
					logger.info("开始对当前无效的ip再次做验证");
					ProxyIpUtil.testValidErrorIps();
					logger.info("对当前无效的ip再次做验证完成");
					try {
						Thread.sleep(36000000);
					} catch (InterruptedException e) {
						logger.error("代理ip验证线程睡眠失败");
					}
				}

			}
		}).start();
	}

	public void stop() {
		proxyIpCrawlerThreadRunFlag = false;
		saveProxyIps();
	}

	public static void saveProxyIps() {
		String validIps = "";
		for (String validIp : ProxyIpUtil.validIps) {
			validIps = validIps + "," + validIp;
		}
		proxyIpProp.setProperty("validsIps", validIps);

		String validErrorIps = "";
		for (String validErrorIp : ProxyIpUtil.validErrorIps) {
			validErrorIps = validErrorIps + "," + validErrorIp;
		}
		proxyIpProp.setProperty("validErrorIps", validErrorIps);
		OutputStream out = null;
		try {
			out = new FileOutputStream(proxyIpFilePath);
			proxyIpProp.store(out, "");
			logger.info("保存代理ip成功");
		} catch (Exception e) {
			logger.error("保存代理ip失败", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("关闭代理ip属性文件失败", e);
			}
		}
	}

	public static Map<String, Object> getProxyIp() {
		List<String> ips = validIps;
		if (CollectionUtils.isEmpty(ips)) {
			return new HashMap<String, Object>();
		}
		int index = (int) (Math.random() * ips.size());
		String[] ipStr = ips.get(index).split(":");
		Map<String, Object> ipInfo = new HashMap<String, Object>();
		ipInfo.put("ip", ipStr[0]);
		ipInfo.put("port", new Integer(ipStr[1]));
		return ipInfo;
	}

	public static void testValidErrorIps() {
		List<String> validErrorProxyIps = new ArrayList<String>();
		validErrorProxyIps.addAll(validErrorIps);
		for (String ip : validErrorProxyIps) {
			String[] ipAndPort = ip.split(":");
			HttpClient httpClient = HttpClientUtil.getHttpClient("proxIp", 50,
					500, 5000, 10000);
			// 设置代理服务器的ip地址和端口
			httpClient.getHostConfiguration().setProxy(ipAndPort[0],
					Integer.parseInt(ipAndPort[1]));
			// 使用抢先认证
			httpClient.getParams().setAuthenticationPreemptive(true);
			HttpMethod method = new GetMethod("http://item.jd.com/2165667.html");
			try {
				httpClient.executeMethod(method);
				if (!fromError2ValidIps.contains(ip)) {
					fromError2ValidIps.add(ip);
				}
				validErrorIps.remove(ip);
				logger.info("上次验证失败的ip，再次验证成功:" + ip);
			} catch (Exception e) {
				// logger.error("上次验证失败的ip:" + ip + " test error:"
				// + e.getMessage());
			} finally {
				method.releaseConnection();
			}
		}
	}

	public static void testValidIps() {
		List<String> validProxyIps = Collections
				.synchronizedList(new ArrayList<String>());
		List<String> validErrorProxyIps = new ArrayList<String>();
		for (String ip : validIps) {
			String[] ipAndPort = ip.split(":");
			HttpClient httpClient = HttpClientUtil.getHttpClient("proxIp", 50,
					500, 5000, 10000);
			// 设置代理服务器的ip地址和端口
			httpClient.getHostConfiguration().setProxy(ipAndPort[0],
					Integer.parseInt(ipAndPort[1]));
			// 使用抢先认证
			httpClient.getParams().setAuthenticationPreemptive(true);
			HttpMethod method = new GetMethod("http://item.jd.com/2165667.html");
			try {
				httpClient.executeMethod(method);
				if (!validProxyIps.contains(ip)) {
					validProxyIps.add(ip);
				}
				logger.info("验证成功ip:" + ip);
			} catch (Exception e) {
				// logger.error("ip:" + ip + " test error:" + e.getMessage());
				validErrorProxyIps.add(ip);
			} finally {
				method.releaseConnection();
			}
		}
		for (String errorIp : validErrorProxyIps) {
			if (!validErrorIps.contains(errorIp)) {
				validErrorIps.add(errorIp);
			}
		}
		validIps = validProxyIps;
	}

	public static void testIp(List<String> waitValidIps) {
		List<String> validErrorProxyIps = new ArrayList<String>();

		for (String ip : waitValidIps) {
			String[] ipAndPort = ip.split(":");
			try {
				Integer.parseInt(ipAndPort[1]);
			} catch (Exception e) {
				continue;
			}
			HttpClient httpClient = HttpClientUtil.getHttpClient("proxIp", 50,
					500, 5000, 10000);
			// 设置代理服务器的ip地址和端口
			httpClient.getHostConfiguration().setProxy(ipAndPort[0],
					Integer.parseInt(ipAndPort[1]));
			// 使用抢先认证
			httpClient.getParams().setAuthenticationPreemptive(true);
			HttpMethod method = new GetMethod("http://item.jd.com/2165667.html");
			try {
				httpClient.executeMethod(method);
				if (!validIps.contains(ip)) {
					validIps.add(ip);
				}
				logger.info("验证成功ip:" + ip);
			} catch (Exception e) {
				// logger.error("ip:" + ip + " test error:" + e.getMessage());
				validErrorProxyIps.add(ip);
			} finally {
				method.releaseConnection();
			}

		}
		for (String errorIp : validErrorProxyIps) {
			if (!validErrorIps.contains(errorIp)) {
				validErrorIps.add(errorIp);
			}
		}
	}

	public static void get89ipProxyIps() {
		String url = "http://www.89ip.cn/tiqu.php?sxb=&tqsl=3000&ports=&ktip=&xl=on&submit=%CC%E1++%C8%A1";

		HttpClient httpClient = HttpClientUtil.getHttpClient("proxIp", 50, 500,
				5000, 10000);
		HttpMethod method = new GetMethod(url);
		List<String> ipList = new ArrayList<String>();
		int allIpCount = 0;
		int existIpCount = 0;
		try {
			httpClient.executeMethod(method);
			String html = method.getResponseBodyAsString();
			String ipHtml = html.substring(
					html.lastIndexOf("</script>") + "</script>".length(),
					html.lastIndexOf("<br>")).trim();
			String[] ips = ipHtml.split("<BR>");
			allIpCount = ips.length;
			for (String ip : ips) {
				if (!validErrorIps.contains(ip) && !validIps.contains(ip)) {
					ipList.add(ip);
				} else {
					existIpCount++;
				}
			}
		} catch (Exception e) {
			logger.error("获取代理地址失败:" + url, e);
		} finally {
			method.releaseConnection();
		}
		logger.info(url + " 获取到代理ip总数:" + allIpCount + " 已存在的ip数量:"
				+ existIpCount + " 不存在的ip数量:" + ipList.size());
		testIp(ipList);
	}

	/**
	 * 判断是否为合法IP
	 * 
	 * @return the ip
	 */
	public static boolean isboolIp(String ipAddress) {
		String ip = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}
}

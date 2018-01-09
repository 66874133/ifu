package com.funnel.svc.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class UrlUtil {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getUrlProtocol(String str) {
		return str.split("://")[0];
	}

	public static String toMD5String(String string) {

		try {
			byte[] bytes = string.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(bytes);
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = HEX_DIGITS[byte0 >>> 4 & 0x0f];
				myChar[k++] = HEX_DIGITS[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isRelativeURL(String url) {
		if (url == null)
			return false;
		return (!url.equals("")) && !url.startsWith("http:");
	}

	/**
	 * encodeurl中的参数值
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static String encodeUrlParameter(String url) throws Exception {

		if (url.contains("?")) {
			StringBuffer buffer = new StringBuffer();
			String[] oneUrlParts = url.split("\\?");

			if (oneUrlParts.length > 1) {
				buffer.append(oneUrlParts[0]).append("?");
				String strPart = oneUrlParts[1];

				String[] strs = strPart.split("&");

				for (int i = 0; i < strs.length; i++) {
					String[] oneParameter = strs[i].split("=");

					buffer.append(oneParameter[0]).append("=");

					if (oneParameter.length > 1) {
						buffer.append(URLEncoder.encode(oneParameter[1],
								"utf-8"));
					}
					if (i != strs.length - 1) {
						buffer.append("&");
					}

				}

				return buffer.toString();
			}

		}
		return url;

	}

	/**
	 * Convert a relative url string to an absolute url string
	 * 
	 * @param relativeURL
	 * @param curi
	 * @return the result string of conversion
	 * @throws Exception
	 */
	public static String getAbsoluteURL(String relativeURL, String baseURL)
			throws Exception {
		if (isRelativeURL(relativeURL)) {
			URI base;
			try {
				base = new URI(baseURL);
				String path = new URL(baseURL).getPath();
				relativeURL = replaceSpeicalString(relativeURL);
				if (relativeURL.startsWith("?")) {
					relativeURL = path + relativeURL;
				}
				URI abs = base.resolve(relativeURL);// 解析于上述网页的相对URL，得到绝对URI
				URL absURL = abs.toURL();// 转成URL
				String abURL = absURL.toString();
				return replaceSpeicalString(abURL);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getLocalizedMessage() + ":relativeURL="
						+ relativeURL + " baseURL=" + baseURL);
			}

		}

		return replaceSpeicalString(relativeURL);
	}

	/**
	 * 替换一些特殊字符，去掉一些特殊的定位符
	 * 
	 * @param url
	 * @return
	 */
	private static String replaceSpeicalString(String url) {

		if (url.contains("#")) {
			url = url.substring(0, url.indexOf("#"));
		}
		if (url.contains("|")) {
			url = url.replace("|", "%7C");
		}
		if (url.contains(" ")) {
			url = url.replace(" ", "%20");
		}
		if (url.contains("\\")) {
			url = url.replace("\\", "/");
		}

		return url;
	}

	/**
	 * 获取正常url地址
	 * 
	 * @param proxyUrl
	 *            代理地址
	 * @return
	 */
	public static String getAccessUrl(String proxyUrl) {

		try {
			return URLDecoder.decode(
					proxyUrl.substring(proxyUrl.indexOf("?url=") + 5,
							proxyUrl.length()), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return proxyUrl;

	}

	public static void main(String[] args) throws Exception {
		System.out
				.println(encodeUrlParameter("https://mdskip.taobao.com/core/initItemDetail.htm?sellerPreview=false&manufactureCity=上海市&service3C=false&isUseInventoryCenter=false&addressLevel=4&isApparel=false&itemId=524513355440&tmallBuySupport=true&household=true&queryMemberRight=true&isRegionLevel=false&volume=1.2&showShopProm=false&cartEnable=true&isForbidBuyItem=false&cachedTimestamp=1471317008107&isSecKill=false&tryBeforeBuy=false&isAreaSell=false&isPurchaseMallPage=false&offlineShop=false"));
		try {
			System.out
					.println("相对地址转换:"
							+ getAbsoluteURL("detail.php?id=2773567",
									"http://guangdiu.com/cate.php?k=baby&m=%E5%A4%A9%E7%8C%AB"));
			System.out
					.println("相对地址转换:"
							+ getAbsoluteURL(
									"/product\\/525\\/emporio-armani-2000052501880001.html",
									"http://www.uemall.com//catalog/category/viewByAjax/api/v2/id/470/limit/24/p/2"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out
				.println("MD5编码转换:"
						+ toMD5String("http://www.yidianda.com/category/three/1-7-24.html"));

	}
}

package com.duplex.crawler.core;



import java.net.URL;

import com.discover.crawler.util.RegularExpressionUtil;
import com.duplex.crawler.core.html.HTMLPath;
import com.duplex.crawler.core.html.HTMLUtil;

public class Test {
	public static void main(String[] args) throws Exception {
		HTMLPath htmlPath = new HTMLPath(
				new URL(
						"https://item.taobao.com/item.htm?id=534944926988"),
				"utf-8");

//		int orders = HTMLUtil.getNodeOrder(htmlPath,
//				"div");
		String[] result = HTMLUtil.getNodeValues(htmlPath,
				"script[order='1']", "");

		for (int i = 0; i < result.length; i++) {
			System.out.println(i + ":" + result[i]);
		}
		String[] values = RegularExpressionUtil.getPatternValue(htmlPath.getContent(),
				"\"promotionPrice\"[:]{\"name.*price\"[:]\"(\\d+\\.?\\d*).*}}->price\":\"(\\d+\\.?\\d*)");
		
		String[] result2 = HTMLUtil.getNodeValues(htmlPath,
				"div[@id='J_ItemList']/div/div/p/a/@href", "");

		for (int i = 0; i < result.length; i++) {
			System.out.println(i + ":" + result2[i]);
		}
		
		

	}
}

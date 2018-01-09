package com.discover.crawler.util;

import java.util.ArrayList;
import java.util.List;

public class UserAgentUtil {
	private static List<String> USERAGENT_LIST = new ArrayList<String>();
	
	private static int size = 0;
	static
	{
		USERAGENT_LIST.add("Baiduspider+(+http://www.baidu.com/search/spider.htm”)");
		USERAGENT_LIST.add("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
		USERAGENT_LIST.add("Googlebot/2.1 (+http://www.googlebot.com/bot.html)");
		USERAGENT_LIST.add("Googlebot/2.1 (+http://www.google.com/bot.html)");
		USERAGENT_LIST.add("Mozilla/5.0 (compatible; Yahoo! Slurp China; http://misc.yahoo.com.cn/help.html)");
		USERAGENT_LIST.add("iaskspider/2.0(+http://iask.com/help/help_index.html”)");
		USERAGENT_LIST.add("Mozilla/5.0 (compatible; iaskspider/1.0; MSIE 6.0)");
		USERAGENT_LIST.add("Sogou web spider/3.0(+http://www.sogou.com/docs/help/webmasters.htm#07)");
		USERAGENT_LIST.add("Sogou Push Spider/3.0(+http://www.sogou.com/docs/help/webmasters.htm#07)");
		USERAGENT_LIST.add("Mozilla/5.0 (compatible; YodaoBot/1.0;http://www.yodao.com/help/webmaster/spider/;)");
		USERAGENT_LIST.add("msnbot/1.0 (+http://search.msn.com/msnbot.htm)");
		size = USERAGENT_LIST.size();
	}
	
	
	public static String getUserAgent()
	{
		return USERAGENT_LIST.get((int) (Math.random()*size));
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getUserAgent());
		}
	}
}

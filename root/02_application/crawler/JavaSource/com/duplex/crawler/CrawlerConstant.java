package com.duplex.crawler;

/**
 * 爬虫常量
 * 
 * @author wanghua4
 */
public class CrawlerConstant {

	/**
	 * 抓取电商Code
	 */
	public static final String CRAWLER_MALL_CODE = "crawlerMallCode";

	public static final String CRAWLER_SVC_CODE = "crawlerSvcCode";

	/**
	 * 抓取类别
	 */
	public static final String CRAWLER_CATEGORY = "crawlerCategory";
	/**
	 * 抓取参数
	 */
	public static final String CRAWLER_PARAMS = "crawlerParams";

	/**
	 * 爬取类型，爬取商品信息
	 */
	public static final Integer CRAWLER_TYPE_GOODS = 1;
	/**
	 * 爬取类型，爬取商品标签信息
	 */
	public static final Integer CRAWLER_TYPE_GOODS_LABEL = 2;

	/**
	 * 抓取应用-连接超时时间
	 */
	public static final int CRAWLER_CONNECTION_TIMEOUT = 20000;

	/**
	 * 抓取应用-读取超时时间
	 */
	public static final int CRAWLER_READ_TIMEOUT = 60000;
	/**
	 * 抓取-重试次数
	 */
	public static final int CRAWLER_RETY_NUM = 3;
	/**
	 * 回调-重试次数
	 */
	public static final int CALLBACK_RETY_NUM = 3;

	/**
	 * 回调服务参数
	 */
	public static final String PARAMS_CALLBACK_SVC_CODE = "callbackSvcCode";
	/**
	 * 爬虫模板文件名称
	 */
	public static final String PARAMS_CRAWLER_CONF_XML = "confXml";
	/**
	 * 抓取到的信息参数
	 */
	public static final String PARAMS_CRAWLER_INFO = "crawlerInfo";

	/**
	 * 获取商品真实url时，允许最大的跳转次数
	 */
	public static final int MAX_REDIRECT_NUM = 10;
	/**
	 * 爬虫爬取未知错误
	 */
	public static final String ERROR_CODE_UNKNOWN = "unknown";
	/**
	 * 爬虫爬取可以不关心的错误
	 */
	public static final String ERROR_CODE_NOT_CARE = "notCare";
	/**
	 * 需要商品详情爬虫抓取的字段，所有能抓取到的字段
	 */
	public static final String CREAWLER_DETAIL_FIELD_ALL = "allField";
	/**
	 * 需要商品详情爬虫抓取的字段，图片
	 */
	public static final String CREAWLER_DETAIL_FIELD_IMG = "imgField";
	/**
	 * 需要商品详情爬虫抓取的字段，价格
	 */
	public static final String CREAWLER_DETAIL_FIELD_PRICE = "priceField";
	/**
	 * 需要商品详情爬虫抓取的字段，标题
	 */
	public static final String CREAWLER_DETAIL_FIELD_TITLE = "titleField";

	/**
	 * 列表页爬取最大页数
	 */
	public static final int MAX_CRAWLER_PAGE = 20;
	/**
	 * 成人海淘列表页爬取最大页数
	 */
	public static final int ADULT_OVERSEAS_MAX_CRAWLER_PAGE = 5;

	/**
	 * 通用Code 适用于使用模板引擎驱动的爬虫
	 */
	public static final String SOURCE_CODE_DEFAULT = "0";

	/**
	 * 逛丢Code
	 */
	public static final String SOURCE_CODE_GUANGDIU = "1";

	/**
	 * 亚马逊Code
	 */
	public static final String SOURCE_CODE_AMAZON = "2";

	/**
	 * 达令Code
	 */
	public static final String SOURCE_CODE_DALING = "4";
	/**
	 * 当当Code
	 */
	public static final String SOURCE_CODE_DANGDANG = "5";

	/**
	 * 国美Code
	 */
	public static final String SOURCE_CODE_GUOMEI = "7";

	/**
	 * 京东Code
	 */
	public static final String SOURCE_CODE_JD = "9";

	/**
	 * 淘宝Code
	 */
	public static final String SOURCE_CODE_TAOBAO = "10";
	/**
	 * 考拉Code
	 */
	public static final String SOURCE_CODE_KAOLA = "12";
	/**
	 * 苏宁Code
	 */
	public static final String SOURCE_CODE_SUNING = "15";

	/**
	 * 天猫Code
	 */
	public static final String SOURCE_CODE_TMALL = "16";

	/**
	 * 一号店Code
	 */
	public static final String SOURCE_CODE_YHD = "17";
	/**
	 * 易迅Code
	 */
	public static final String SOURCE_CODE_YIXUN = "19";

}

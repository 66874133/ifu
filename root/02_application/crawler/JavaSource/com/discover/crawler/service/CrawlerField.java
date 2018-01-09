package com.discover.crawler.service;

/**
 * 爬虫模板中支持的字段
 * 
 * @author jiangbo3
 * 
 */
public interface CrawlerField {

	public static final String HTTP_HEAD_FIELD_REFERER = "Referer";

	public static final String FIELD_VOLUME_MONTH = "volume_month";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_URL = "url";
	public static final String FIELD_SOURCE_URL = "source_url";
	public static final String FIELD_PICTURE = "pic";
	public static final String FIELD_PAGE = "page";
	public static final String FIELD_INDEX_IN_PAGE = "index_in_page";
	
	public static final String FIELD_DETAIL_TITILE = "detail_titile";
	public static final String FIELD_DETAIL_ABSTRACT = "detail_abstract";
	public static final String FIELD_DETAIL_PIC = "detail_pic";
	public static final String FIELD_COUPON = "coupon";
	public static final String FIELD_COUPON_LINK = "coupon_link";
	/**
	 * 商品在商城里的唯一分类id
	 */
	public static final String FIELD_CATEGORY_ID = "catId";
	/**
	 * 商品在商城里的唯一商品id
	 */
	public static final String FIELD_SOURCE_GOOD_ID = "sourceGoodId";
	public static final String FIELD_COMMENT_COUNT = "comment_count";
	public static final String FIELD_ORIGINAL_PRICE = "oriPrice";
}

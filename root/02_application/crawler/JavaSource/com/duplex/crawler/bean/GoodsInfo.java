package com.duplex.crawler.bean;

import java.util.List;

public class GoodsInfo {

	public static final int TYPE_CRAWLER = 0;
	public static final int TYPE_MANUAL = 1;
	/**
	 * 商品在商城里唯一的id
	 */
	private String sourceGoodsId;
	private String sourceMallUrl;
	private String pcUrl;
	private String mobileUrl;
	private String pcSourceDetailPageUrl;
	private String mobileSourceDetailPageUrl;
	private String imgUrl;
	private String name;
	private String sourceCode;
	private String price;
	private int priceUnitCode;
	/**
	 * 商品抓取时人工设置的标记tag
	 */
	private String sourceCategory;

	private String oriPrice;
	private String commentCount;

	/**
	 * 抓取类型 0为爬虫抓取 1为人工指定
	 */
	private int type;

	/**
	 * 优惠券信息
	 */
	private String coupon;
	/**
	 * 优惠券地址
	 */
	private String couponLink;
	private String page;
	private String indexInPage;

	private List<GoodsImgTextInfo> goodsImgTextInfos;
	private String sourceMallCode;

	/**
	 * 商品在商城里的分类id
	 */
	private String cid;

	/**
	 * 商品在导购中里的唯一分类id
	 */
	private String classId;

	private String saleNum;
	private String taobaoOpenId;

	private String dateTime;

	/**
	 * 商品在商城里唯一的id
	 * 
	 * @return
	 */
	public String getSourceGoodsId() {
		return sourceGoodsId;
	}

	public void setSourceGoodsId(String sourceGoodsId) {
		this.sourceGoodsId = sourceGoodsId;
	}

	public String getSourceMallUrl() {
		return sourceMallUrl;
	}

	public void setSourceMallUrl(String sourceMallUrl) {
		this.sourceMallUrl = sourceMallUrl;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public String getPcSourceDetailPageUrl() {
		return pcSourceDetailPageUrl;
	}

	public void setPcSourceDetailPageUrl(String pcSourceDetailPageUrl) {
		this.pcSourceDetailPageUrl = pcSourceDetailPageUrl;
	}

	public String getMobileSourceDetailPageUrl() {
		return mobileSourceDetailPageUrl;
	}

	public void setMobileSourceDetailPageUrl(String mobileSourceDetailPageUrl) {
		this.mobileSourceDetailPageUrl = mobileSourceDetailPageUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSourceCategory() {
		return sourceCategory;
	}

	public void setSourceCategory(String sourceCategory) {
		this.sourceCategory = sourceCategory;
	}

	public List<GoodsImgTextInfo> getGoodsImgTextInfos() {
		return goodsImgTextInfos;
	}

	public void setGoodsImgTextInfos(List<GoodsImgTextInfo> goodsImgTextInfos) {
		this.goodsImgTextInfos = goodsImgTextInfos;
	}

	public int getPriceUnitCode() {
		return priceUnitCode;
	}

	public void setPriceUnitCode(int priceUnitCode) {
		this.priceUnitCode = priceUnitCode;
	}

	public String getSourceMallCode() {
		return sourceMallCode;
	}

	public void setSourceMallCode(String sourceMallCode) {
		this.sourceMallCode = sourceMallCode;
	}

	public String getCid() {
		return cid;
	}

	/**
	 * 商品在商城里的分类id
	 * 
	 * @param cid
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}

	public String getTaobaoOpenId() {
		return taobaoOpenId;
	}

	public void setTaobaoOpenId(String taobaoOpenId) {
		this.taobaoOpenId = taobaoOpenId;
	}

	public String getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(String oriPrice) {
		this.oriPrice = oriPrice;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getIndexInPage() {
		return indexInPage;
	}

	public void setIndexInPage(String indexInPage) {
		this.indexInPage = indexInPage;
	}

	public String getCouponLink() {
		return couponLink;
	}

	public void setCouponLink(String couponLink) {
		this.couponLink = couponLink;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "GoodsInfo [sourceGoodsId=" + sourceGoodsId + ", sourceMallUrl="
				+ sourceMallUrl + ", pcUrl=" + pcUrl + ", mobileUrl="
				+ mobileUrl + ", pcSourceDetailPageUrl="
				+ pcSourceDetailPageUrl + ", mobileSourceDetailPageUrl="
				+ mobileSourceDetailPageUrl + ", imgUrl=" + imgUrl + ", name="
				+ name + ", sourceCode=" + sourceCode + ", price=" + price
				+ ", priceUnitCode=" + priceUnitCode + ", sourceCategory="
				+ sourceCategory + ", oriPrice=" + oriPrice + ", commentCount="
				+ commentCount + ", type=" + type + ", coupon=" + coupon
				+ ", couponLink=" + couponLink + ", page=" + page
				+ ", indexInPage=" + indexInPage + ", goodsImgTextInfos="
				+ goodsImgTextInfos + ", sourceMallCode=" + sourceMallCode
				+ ", cid=" + cid + ", classId=" + classId + ", saleNum="
				+ saleNum + ", taobaoOpenId=" + taobaoOpenId + "]";
	}

}

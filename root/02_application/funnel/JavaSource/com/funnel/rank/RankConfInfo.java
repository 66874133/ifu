package com.funnel.rank;

public class RankConfInfo {

	private String rankCode;
	private String rankDataSourceCode;
	private String rankCategorys;
	private String rankPriceBegin;
	private String rankPriceEnd;
	private String rankClassify;
	private String rankMallCode;
	private String rankDesc;
	private String rankExpression;
	private String rankExpressionConf;
	private String code;
	private String label;
	private String rankGroup;
	/**
	 * 榜单类型 0为自动计算榜单 1为人工审核榜单
	 */
	private String type;
	/**
	 * 榜单使用的最大商品个数
	 */
	private String rankRecordSize;

	/**
	 * 榜单使用的商品抓取发起者,同商品详情中的type
	 */
	private String rankRecordType;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getRankDataSourceCode() {
		return rankDataSourceCode;
	}

	public void setRankDataSourceCode(String rankDataSourceCode) {
		this.rankDataSourceCode = rankDataSourceCode;
	}

	public String getRankCategorys() {
		return rankCategorys;
	}

	public void setRankCategorys(String rankCategorys) {
		this.rankCategorys = rankCategorys;
	}

	public String getRankDesc() {
		return rankDesc;
	}

	public void setRankDesc(String rankDesc) {
		this.rankDesc = rankDesc;
	}

	public String getRankPriceBegin() {
		return rankPriceBegin;
	}

	public void setRankPriceBegin(String rankPriceBegin) {
		this.rankPriceBegin = rankPriceBegin;
	}

	public String getRankPriceEnd() {
		return rankPriceEnd;
	}

	public void setRankPriceEnd(String rankPriceEnd) {
		this.rankPriceEnd = rankPriceEnd;
	}

	public String getRankClassify() {
		return rankClassify;
	}

	public void setRankClassify(String rankClassify) {
		this.rankClassify = rankClassify;
	}

	public String getRankMallCode() {
		return rankMallCode;
	}

	public void setRankMallCode(String rankMallCode) {
		this.rankMallCode = rankMallCode;
	}

	public String getRankGroup() {
		return rankGroup;
	}

	public void setRankGroup(String rankGroup) {
		this.rankGroup = rankGroup;
	}

	public String getRankRecordSize() {
		return rankRecordSize;
	}

	public void setRankRecordSize(String rankRecordSize) {
		this.rankRecordSize = rankRecordSize;
	}

	public String getRankExpression() {
		return rankExpression;
	}

	public void setRankExpression(String rankExpression) {
		this.rankExpression = rankExpression;
	}

	public String getRankExpressionConf() {
		return rankExpressionConf;
	}

	public void setRankExpressionConf(String rankExpressionConf) {
		this.rankExpressionConf = rankExpressionConf;
	}

	/**
	 * 榜单使用的商品抓取发起者,同商品详情中的type
	 */
	public String getRankRecordType() {
		return rankRecordType;
	}

	public void setRankRecordType(String rankRecordType) {
		this.rankRecordType = rankRecordType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "GoodsRankConfInfo [rankCode=" + rankCode
				+ ", rankDataSourceCode=" + rankDataSourceCode
				+ ", rankCategorys=" + rankCategorys + ", rankPriceBegin="
				+ rankPriceBegin + ", rankPriceEnd=" + rankPriceEnd
				+ ", rankClassify=" + rankClassify + ", rankMallCode="
				+ rankMallCode + ", rankDesc=" + rankDesc + ", rankExpression="
				+ rankExpression + ", rankExpressionConf=" + rankExpressionConf
				+ ", code=" + code + ", label=" + label + ", rankGroup="
				+ rankGroup + ", type=" + type + ", rankRecordSize="
				+ rankRecordSize + ", rankRecordType=" + rankRecordType + "]";
	}

}

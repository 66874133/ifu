package com.funnel.rank;

public class GoodsRankInfoBean {
	private String goodsId;
	private Integer orderNum;
	private String rankCode;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	@Override
	public String toString() {
		return "GoodsRankInfoBean [goodsId=" + goodsId + ", orderNum="
				+ orderNum + ", rankCode=" + rankCode + "]";
	}

}

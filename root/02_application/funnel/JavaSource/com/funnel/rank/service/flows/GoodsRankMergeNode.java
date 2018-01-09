package com.funnel.rank.service.flows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.funnel.rank.GoodsRankInfoBean;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

public class GoodsRankMergeNode extends SyncService {

	@Override
	public String getSvcCode() {
		return "goodsRankMergeNode";
	}

	@Override
	public String getSvcDesc() {
		return "商品榜单合并节点";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(SvcContext context) {

		long t = System.currentTimeMillis();
		String rankCode = context.getRequestData().getAsString("rankCode");
		logger.info("开始合并榜单数据,rankCode:" + rankCode);
		List<GoodsRankInfoBean> beMergeRankList = (List<GoodsRankInfoBean>) context
				.getRequestData().get("beMergeRankList");

		beMergeRankList = sortRankListByOrderNum(beMergeRankList);

		List<GoodsRankInfoBean> mergeRankList = (List<GoodsRankInfoBean>) context
				.getRequestData().get("mergeRankList");

		mergeRankList = sortRankListByOrderNum(mergeRankList);

		List<GoodsRankInfoBean> mergedRankList = doMerge(beMergeRankList,
				mergeRankList);
		context.getRequestData().put("mergedRankList", mergedRankList);

		logger.info("合并榜单数据完成,rankCode:" + rankCode + " 耗时:"
				+ (System.currentTimeMillis() - t));

	}

	private List<GoodsRankInfoBean> sortRankListByOrderNum(
			List<GoodsRankInfoBean> goodsRankDatas) {
		Collections.sort(goodsRankDatas, new Comparator<GoodsRankInfoBean>() {
			public int compare(GoodsRankInfoBean arg0, GoodsRankInfoBean arg1) {
				// 根据信息中的orderNum排序
				return arg0.getOrderNum().compareTo(arg1.getOrderNum());
			}

		});
		return goodsRankDatas;
	}

	private List<GoodsRankInfoBean> doMerge(
			List<GoodsRankInfoBean> beMergeRankList,
			List<GoodsRankInfoBean> mergeRankList) {
		if (CollectionUtils.isEmpty(beMergeRankList)) {
			return mergeRankList;
		}
		if (CollectionUtils.isEmpty(mergeRankList)) {
			return refreshOrderNum(beMergeRankList);
		}
		List<GoodsRankInfoBean> afterRemoveRepeatBeMergerRankList = removeRepeatDataFromBeMergeRankList(
				beMergeRankList, mergeRankList);
		for (GoodsRankInfoBean goodsRankInfoBean : mergeRankList) {
			int index = goodsRankInfoBean.getOrderNum() >= 1 ? (goodsRankInfoBean
					.getOrderNum() - 1) : 0;

			if (index >= afterRemoveRepeatBeMergerRankList.size()) {
				afterRemoveRepeatBeMergerRankList.add(goodsRankInfoBean);
			} else {
				afterRemoveRepeatBeMergerRankList.add(index, goodsRankInfoBean);
			}
		}
		return refreshOrderNum(afterRemoveRepeatBeMergerRankList);
	}

	private List<GoodsRankInfoBean> refreshOrderNum(
			List<GoodsRankInfoBean> mergerdRankList) {
		List<GoodsRankInfoBean> afterRefreshOrderRankList = new ArrayList<GoodsRankInfoBean>();
		for (int i = 0; i < mergerdRankList.size(); i++) {
			GoodsRankInfoBean goodsRankInfoBean = mergerdRankList.get(i);
			goodsRankInfoBean.setOrderNum(i + 1);
			afterRefreshOrderRankList.add(goodsRankInfoBean);
		}
		return afterRefreshOrderRankList;
	}

	private List<GoodsRankInfoBean> removeRepeatDataFromBeMergeRankList(
			List<GoodsRankInfoBean> beMergeRankList,
			List<GoodsRankInfoBean> mergeRankList) {
		List<GoodsRankInfoBean> afterRemoveRepeatRankList = new ArrayList<GoodsRankInfoBean>();
		for (GoodsRankInfoBean goodsRankInfoBean : beMergeRankList) {
			if (!isContains(mergeRankList, goodsRankInfoBean)) {
				afterRemoveRepeatRankList.add(goodsRankInfoBean);
			}
		}
		return afterRemoveRepeatRankList;
	}

	private boolean isContains(List<GoodsRankInfoBean> rankList,
			GoodsRankInfoBean goodsRankInfoBean) {
		for (GoodsRankInfoBean rankInfoBean : rankList) {
			if (rankInfoBean.getGoodsId()
					.equals(goodsRankInfoBean.getGoodsId())) {
				return true;
			}
		}
		return false;
	}
}

package com.funnel.rank.service.flows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.funnel.datasource.DataBaseUtil;
import com.funnel.rank.GoodsRankInfoBean;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

public class AtuoRankAndOperateDataQueryNodeV2 extends SyncService {

	@Override
	public String getSvcCode() {
		return "atuoRankAndOperateDataQueryNodeV2";
	}

	@Override
	public String getSvcDesc() {
		return "自动生成数据和运营配置数据查询节点";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(SvcContext context) {
		long t = System.currentTimeMillis();
		String rankCode = context.getRequestData().getAsString("rankCode");
		logger.info("开始查询榜单数据,rankCode:" + rankCode);
		Map<String, Object> autoRankDataQuereyMap = new HashMap<String, Object>();
		autoRankDataQuereyMap.put("rankCode", rankCode);

		List<GoodsRankInfoBean> atuoRankData = (List<GoodsRankInfoBean>) context
				.getRequestData().get("atuoRankData");

		Map<String, Object> operateRankDataQuereyMap = new HashMap<String, Object>();
		operateRankDataQuereyMap.put("rankCode", rankCode);

		List<GoodsRankInfoBean> operateRankData;
		try {
			operateRankData = (List<GoodsRankInfoBean>) DataBaseUtil.select(
					"goodsRank.quereyOperateRankData",
					operateRankDataQuereyMap, GoodsRankInfoBean.class);

			if (CollectionUtils.isEmpty(atuoRankData)
					&& CollectionUtils.isEmpty(operateRankData)) {
				logger.error("榜单生成时,自动生成数据和运营配置数据都为空,rankCode:" + rankCode);
				context.setFinish(true);
			}
			context.getRequestData().put("beMergeRankList", atuoRankData);
			context.getRequestData().put("mergeRankList", operateRankData);
			logger.info("查询榜单数据,rankCode:" + rankCode + " 完成,耗时:"
					+ (System.currentTimeMillis() - t));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}

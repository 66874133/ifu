package com.funnel.rank.service.flows;

import com.funnel.rank.RankConfInfo;
import com.funnel.rank.cache.RankConfCache;
import com.funnel.rank.service.GoodsRankScheduleSvc;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

public class GoodsRankStartServiceNode extends SyncService {

	public static final String RANK_TYPE_AUTO = "0";
	public static final String RANK_TYPE_MANUAL = "1";

	@Override
	public String getSvcCode() {
		return "goodsRankStartServiceNode";
	}

	@Override
	public String getSvcDesc() {
		return "商品榜单生成开始处理";
	}

	@Override
	public void process(SvcContext context) {

		String rankCode = context.getRequestData().getAsString("rankCode");
		RankConfInfo goodsRankConfInfo = RankConfCache.getRankConf(rankCode);

		if (null != goodsRankConfInfo) {
			logger.info("开始生成榜单流程,rankCode:" + rankCode);

			if (GoodsRankScheduleSvc.isRunning(rankCode)) {
				logger.info("榜单已经在运行,本次请求不处理,rankCode=" + rankCode);
				context.setFinish(true);
				return;
			} else if (RANK_TYPE_MANUAL.equals(goodsRankConfInfo.getType())) {
				logger.info("人工审核榜单不支持自动计算,本次请求不处理,rankCode=" + rankCode);
				context.setFinish(true);
				return;
			} else {
				GoodsRankScheduleSvc.setRunning(rankCode, true);
			}
		} else {
			logger.warn("未发现该榜单配置,本次请求不处理,rankCode:" + rankCode);
			context.setFinish(true);
			return;
		}

	}
}

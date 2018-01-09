package com.funnel.rank.service.flows;

import com.funnel.rank.service.GoodsRankScheduleSvc;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;

public class GoodsRankFinishedServiceNode extends SyncService {

	@Override
	public String getSvcCode() {
		return "goodsRankFinishedServiceNode";
	}

	@Override
	public String getSvcDesc() {
		return "商品榜单生成结束处理";
	}

	@Override
	public void process(SvcContext context) {

		String rankCode = context.getRequestData().getAsString("rankCode");

		logger.info("结束生成榜单流程,rankCode:" + rankCode);

		GoodsRankScheduleSvc.setRunning(rankCode, false);

	}

}

package com.funnel.rank.service.flows;

import java.util.HashMap;
import java.util.Map;

import com.funnel.rank.cache.RankConfCache;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.ApplicationUtil;
import com.funnel.svc.util.JsonUtil;

public class GoodsRankNotifyOtherServiceNode extends SyncService {
	private String goodsInfoGoodsServiceQueueName;

	@Override
	public String getSvcCode() {
		return "goodsRankNotifyOtherServiceNode";
	}

	@Override
	public String getSvcDesc() {
		return "商品榜单生成后通知其他服务节点";
	}

	@Override
	public void process(SvcContext context) {

		long t = System.currentTimeMillis();
		boolean skipNotify = false;
		String rankCode = context.getRequestData().getAsString("rankCode");
		if (context.getRequestData().containsKey("skipNotify")
				&& "true".equals(context.getRequestData().getAsString(
						"skipNotify"))) {
			skipNotify = true;
		}

		logger.info("开始通知其他业务榜单数据,rankCode:" + rankCode);
		Map<String, Object> queueMsg = new HashMap<String, Object>();
		queueMsg.put("subject", 2);
		queueMsg.put("type", 1);
		queueMsg.put("code", RankConfCache.getRankConf(rankCode)
				.getRankDataSourceCode());

		if (skipNotify) {
			logger.info("不发送榜单生成通知，榜单生成完成,rankCode:" + rankCode + " sendMsg:"
					+ JsonUtil.toJSONString(queueMsg) + " 耗时:"
					+ (System.currentTimeMillis() - t));
		} else {
			ApplicationUtil.getGoodsRedisService().lpush(
					goodsInfoGoodsServiceQueueName, queueMsg);
			logger.info("发送榜单生成通知,榜单生成完成,rankCode:" + rankCode + " sendMsg:"
					+ JsonUtil.toJSONString(queueMsg) + " 耗时:"
					+ (System.currentTimeMillis() - t));
		}

	}

	public String getGoodsInfoGoodsServiceQueueName() {
		return goodsInfoGoodsServiceQueueName;
	}

	public void setGoodsInfoGoodsServiceQueueName(
			String goodsInfoGoodsServiceQueueName) {
		this.goodsInfoGoodsServiceQueueName = goodsInfoGoodsServiceQueueName;
	}

}

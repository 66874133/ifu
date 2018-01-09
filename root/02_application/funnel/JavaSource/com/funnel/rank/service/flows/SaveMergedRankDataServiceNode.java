package com.funnel.rank.service.flows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.funnel.datasource.DataBaseUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.sys.SysParamContent;
import com.funnel.sys.cache.SysParamCache;

public class SaveMergedRankDataServiceNode extends SyncService {
	@Override
	public String getSvcCode() {
		return "saveMergedRankDataServiceNode";
	}

	@Override
	public String getSvcDesc() {
		return "保存生成的榜单节点";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(SvcContext context) {
		long t = System.currentTimeMillis();
		String rankCode = context.getRequestData().getAsString("rankCode");
		logger.info("开始保存榜单数据,rankCode:" + rankCode);
		List<Object> mergedRankList = (List<Object>) context.getRequestData()
				.get("mergedRankList");

		Map<String, Object> delLastRankDataParamMap = new HashMap<String, Object>();
		delLastRankDataParamMap.put("rankCode", rankCode);
		delLastRankDataParamMap.put("maxRankHisDay", SysParamCache
				.getParamValueByKey(SysParamContent.GOODS_PARAM_TYPE,
						SysParamContent.GOODS_PARAM_RANK_HIS_DAY));
		DataBaseUtil.delete("goodsRank.delLastRankData",
				delLastRankDataParamMap);

		try {
			DataBaseUtil.batchInsert("goodsRank.saveRankData", mergedRankList);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("---------------" + rankCode
				+ "榜单top10--------------------");
		int count = Math.min(mergedRankList.size(), 10);
		for (int i = 0; i < count; i++) {
			logger.info(mergedRankList.get(i));
		}
		logger.info("---------------" + rankCode
				+ "榜单top10--------------------");
		logger.info("保存榜单数据完成,rankCode:" + rankCode + " 耗时:"
				+ (System.currentTimeMillis() - t));
	}
}

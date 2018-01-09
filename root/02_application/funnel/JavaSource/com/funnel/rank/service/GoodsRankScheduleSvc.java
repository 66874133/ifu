package com.funnel.rank.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.funnel.datasource.DataBaseUtil;
import com.funnel.rank.RankConfInfo;
import com.funnel.rank.cache.RankConfCache;
import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.DateUtil;
import com.funnel.svc.util.StringUtils;
import com.funnel.sys.SysParamContent;
import com.funnel.sys.cache.SysParamCache;

public class GoodsRankScheduleSvc extends SyncService {

	public static Map<String, Boolean> rankToStatus = new HashMap<String, Boolean>();

	@Override
	public String getSvcCode() {
		return "goodsRankSchedule";
	}

	@Override
	public String getSvcDesc() {
		return "商品榜单生成调度服务";
	}

	@Override
	public void process(SvcContext context) {
		// 榜单代码
		if (context.getRequestData().containsKey("action")) {
			String action = context.getRequestData().getAsString("action");
			String rankCode = context.getRequestData().getAsString("rankCode");
			if ("queryRankProgress".equalsIgnoreCase(action)) {
				context.getResponseData().put("isRunning", isRunning(rankCode));
				return;
			}
		}

		String rankGennerFlow = context.getRequestData().getAsString("flowId");
		if (!StringUtils.hasText(rankGennerFlow)) {
			rankGennerFlow = "goodsRankGennerFlow";
		}
		String rankGroup = context.getRequestData().getAsString("rankGroup");

		long t = System.currentTimeMillis();
		logger.info("-----------------开始调用生产榜单服务--------------------rankGroup="
				+ rankGroup + "--rankGennerFlow=" + rankGennerFlow);
		if (StringUtils.hasText(rankGroup)) {
			List<RankConfInfo> currentGroupRankConfs = RankConfCache
					.getRankConfByRankGroup(rankGroup);
			// Map<String, List<Map<String, Object>>> depot =
			// queryAllGoodsInfos();

			for (RankConfInfo confInfo : currentGroupRankConfs) {
				Map<String, Object> rankParams = copyParams(context
						.getRequestData());
				rankParams.put("rankCode", confInfo.getRankCode());

				// rankParams.put("depot", depot);
				long s = System.currentTimeMillis();
				logger.info("调用生产榜单服务 rankCode=" + confInfo.getRankCode());
				ServiceCallUtil.callService(rankGennerFlow, rankParams);
				logger.info("调用生产榜单服务完成 rankCode=" + confInfo.getRankCode()
						+ " time=" + (System.currentTimeMillis() - s) + "ms");
			}
		} else {
			ServiceCallUtil.callService(rankGennerFlow,
					context.getRequestData());
		}

		logger.info("-----------------调用生产榜单服务完成--------------------time="
				+ (System.currentTimeMillis() - t) + "ms");

	}

	private Map<String, Object> copyParams(Map<String, Object> params) {
		Map<String, Object> afterCopyParams = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			afterCopyParams.put(entry.getKey(), entry.getValue());
		}
		return afterCopyParams;
	}


	private Map<String, List<Map<String, Object>>> queryAllGoodsInfos() {
		Map<String, Object> queryMap = new HashMap<String, Object>();

		String day = SysParamCache.getParamValueByKey(
				SysParamContent.GOODS_PARAM_TYPE,
				SysParamContent.GOODS_PARAM_MAX_RANK_CANDIDATE_GOODS_DAY);

		String max = SysParamCache.getParamValueByKey(
				SysParamContent.GOODS_PARAM_TYPE,
				SysParamContent.GOODS_PARAM_MAX_RANK_CANDIDATE_GOODS_NUM);

		int d = 90;
		long m = 10000;
		if (null != day && !"".equals(day)) {
			d = Integer.parseInt(day);
		}

		if (null != max && !"".equals(max)) {
			m = Long.parseLong(max);
		}

		Date current = new Date();

		queryMap.put("updateDateStart",
				DateUtil.getSimpleDateStrWithoutTime(DateUtil.getDateBefore(
						current, d)));
		queryMap.put("updateDateEnd",
				DateUtil.getSimpleDateStrWithoutTime(current));

		queryMap.put("limit", m);

		long s = System.currentTimeMillis();
		List<Map<String, Object>> queryResult = DataBaseUtil.select(
						"goodsInfo.queryGoodsInfos", queryMap);
		logger.info("查询商品记录共计" + queryResult.size() + "条,耗时"
				+ (System.currentTimeMillis() - s) + "ms");

		return mappingCategory(queryResult);

	}

	private Map<String, List<Map<String, Object>>> mappingCategory(
			List<Map<String, Object>> datas) {
		Map<String, List<Map<String, Object>>> categoryToDatas = new HashMap<String, List<Map<String, Object>>>();

		for (Map<String, Object> data : datas) {
			if (!categoryToDatas.containsKey(String.valueOf(data
					.get("CATEGORYCODE")))) {
				categoryToDatas.put(String.valueOf(data.get("CATEGORYCODE")),
						new ArrayList<Map<String, Object>>());

			}

			categoryToDatas.get(String.valueOf(data.get("CATEGORYCODE"))).add(
					data);

		}

		return categoryToDatas;
	}

	public synchronized static void setRunning(String rankGroup,
			boolean isRunning) {
		rankToStatus.put(rankGroup, isRunning);
	}

	public static synchronized boolean isRunning(String rankCode) {
		if (rankToStatus.containsKey(rankCode)) {
			return rankToStatus.get(rankCode);
		}
		return false;
	}
}

package com.funnel.rank.service.flows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funnel.datasource.DataBaseUtil;
import com.funnel.rank.GoodsOrderRateBean;
import com.funnel.rank.GoodsRankInfoBean;
import com.funnel.rank.OneGoodsOrderRateBean;
import com.funnel.rank.RankConfInfo;
import com.funnel.rank.cache.RankConfCache;
import com.funnel.rank.service.GoodsRankScheduleSvc;
import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.DateUtil;
import com.funnel.svc.util.JsonUtil;
import com.funnel.svc.util.StringUtils;
import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;
import com.sieve.policy.Policy;
import com.sieve.policy.PolicyService;
import com.sieve.util.StringUtil;


public class AutoRankEngineServiceNode extends SyncService {

	@Override
	public String getSvcCode() {
		return "autoRankEngineServiceNode";
	}

	@Override
	public String getSvcDesc() {
		return "自动计算生成商品榜单节点";
	}

	@Override
	public void process(SvcContext context) {
		long t = System.currentTimeMillis();
		String rankCode = context.getRequestData().getAsString("rankCode");
		try {

			RankConfInfo goodsRankConfInfo = RankConfCache
					.getRankConf(rankCode);
			logger.info("开始生成自动榜单," + JsonUtil.toJSONString(goodsRankConfInfo));

			// 查询数据

			long s = System.currentTimeMillis();

			List<Map<String, Object>> list = queryResult(goodsRankConfInfo);

			logger.info("查询商品记录rankCode=" + rankCode + "共计" + list.size()
					+ "条,耗时" + (System.currentTimeMillis() - s) + "ms");
			Map<String, Long> cidToRate = getCidSaleCount();
			logger.info("订单分类比例 cidToRate=" + cidToRate);
			// 组合数据
			List<Map<String, String>> input = buildPolicyInputlist(list,
					cidToRate);
			logger.info("组合数据rankCode=" + rankCode + ",耗时"
					+ (System.currentTimeMillis() - s) + "ms");
			// 调用引擎
			long s1 = System.currentTimeMillis();

			ItemPolicyAnalyseResults analyseResults = caculate(input,
					goodsRankConfInfo);

			logger.info("计算商品记录rankCode=" + rankCode + "共计" + list.size()
					+ "条,耗时" + (System.currentTimeMillis() - s1) + "ms");

			List<GoodsRankInfoBean> atuoRankData = toGoodsRankInfoBeans(
					analyseResults, goodsRankConfInfo);
			context.getRequestData().put("atuoRankData", atuoRankData);

			logger.info("生成自动榜单完成,榜单记录共" + atuoRankData.size() + "条,耗时:"
					+ (System.currentTimeMillis() - t) + "ms 榜单信息:"
					+ JsonUtil.toJSONString(goodsRankConfInfo));
		} catch (Exception e) {
			logger.info("生成自动榜单出错", e);
			GoodsRankScheduleSvc.setRunning(rankCode, false);
		}
	}

	// @SuppressWarnings("unchecked")
	// private List<Map<String, Object>> queryResult(
	// GoodsRankConfInfo goodsRankConfInfo, SvcContext context) {
	//
	// Map<String, List<Map<String, Object>>> depot = (Map<String,
	// List<Map<String, Object>>>) context
	// .getRequestData().get("depot");
	//
	// Map<String, Object> queryMap = new HashMap<String, Object>();
	// Date current = new Date();
	// queryMap.put("priceStart", goodsRankConfInfo.getRankPriceBegin());
	// queryMap.put("priceEnd", goodsRankConfInfo.getRankPriceEnd());
	// queryMap.put("updateDateStart", DateUtil
	// .getSimpleDateStrWithoutTime(DateUtil
	// .getDateBefore(current, 90)));
	// queryMap.put("updateDateEnd",
	// DateUtil.getSimpleDateStrWithoutTime(current));
	// List<Map<String, Object>> queryResult = new ArrayList<Map<String,
	// Object>>();
	// if (StringUtils.hasText(goodsRankConfInfo.getRankCategorys())) {
	// logger.info("分类筛选榜单goodsRankConfInfo=" + goodsRankConfInfo);
	// String cats = goodsRankConfInfo.getRankCategorys();
	// String[] categories = cats.split(",");
	//
	// for (int i = 0; i < categories.length; i++) {
	//
	// if (depot.containsKey(categories[i])) {
	// queryResult.addAll(depot.get(categories[i]));
	// } else {
	// logger.warn("没有发现分类category=" + categories[i]
	// + "的商品可用于此榜单goodsRankConfInfo=" + goodsRankConfInfo);
	// }
	// }
	//
	// }
	//
	// else {
	// logger.info("全员筛选榜单goodsRankConfInfo=" + goodsRankConfInfo);
	// Iterator<List<Map<String, Object>>> iterator = depot.values()
	// .iterator();
	// while (iterator.hasNext()) {
	// queryResult.addAll(iterator.next());
	// }
	//
	// }
	//
	// return queryResult;
	// }

	/**
	 * 按照榜单设置的参数查询出候选数据
	 * 
	 * @param goodsRankConfInfo
	 * @return
	 */
	private List<Map<String, Object>> queryResult(
			RankConfInfo goodsRankConfInfo) {
		logger.info("分类筛选榜单goodsRankConfInfo=" + goodsRankConfInfo);

		Map<String, Object> queryMap = new HashMap<String, Object>();
		List<Map<String, Object>> queryResult = null;
		// Date current = new Date();
		queryMap.put("priceStart", goodsRankConfInfo.getRankPriceBegin());
		queryMap.put("priceEnd", goodsRankConfInfo.getRankPriceEnd());
		// queryMap.put("updateDateStart", DateUtil
		// .getSimpleDateStrWithoutTime(DateUtil
		// .getDateBefore(current, 180)));
		// queryMap.put("updateDateEnd",
		// DateUtil.getSimpleDateStrWithoutTime(current));

		if (StringUtils.hasText(goodsRankConfInfo.getRankRecordType())) {

			int type = Integer.parseInt(goodsRankConfInfo.getRankRecordType());
			if (type >= 0) {
				queryMap.put("type", goodsRankConfInfo.getRankRecordType());
			}

		}

		if (StringUtils.hasText(goodsRankConfInfo.getRankRecordSize())) {

			queryMap.put("limit", goodsRankConfInfo.getRankRecordSize());

		} else {
			queryMap.put("limit", "500");
		}
		if (StringUtils.hasText(goodsRankConfInfo.getRankCategorys())) {

			queryMap.put("categoryCodes",
					getSqlInFieldString(goodsRankConfInfo.getRankCategorys()));

		}
		if (StringUtils.hasText(goodsRankConfInfo.getRankClassify())) {

			queryMap.put("classifyIds", goodsRankConfInfo.getRankClassify());

		}
		long s = System.currentTimeMillis();
		queryResult = DataBaseUtil.select(
				"goodsInfo.queryGoodsInfos", queryMap);
		logger.info("查询商品记录共计" + queryResult.size() + "条,耗时"
				+ (System.currentTimeMillis() - s) + "ms");

		return queryResult;
	}

	private String getSqlInFieldString(String str) {
		StringBuffer buffer = new StringBuffer();
		String[] strs = str.split(",");
		for (int i = 0; i < strs.length; i++) {
			if (i == strs.length - 1) {
				buffer.append("'").append(strs[i]).append("'");
			} else {
				buffer.append("'").append(strs[i]).append("'").append(",");
			}

		}

		return buffer.toString();
	}

	private List<GoodsRankInfoBean> toGoodsRankInfoBeans(
			ItemPolicyAnalyseResults analyseResults,
			RankConfInfo goodsRankConfInfo) {
		List<GoodsRankInfoBean> list = new ArrayList<GoodsRankInfoBean>();
		int i = 1;
		for (OneItemPolicyAnalyseResult oneItemPolicyAnalyseResult : analyseResults
				.getList()) {

			GoodsRankInfoBean bean = new GoodsRankInfoBean();
			bean.setOrderNum(i);
			bean.setRankCode(goodsRankConfInfo.getRankCode());
			bean.setGoodsId(oneItemPolicyAnalyseResult.getInput().get(
					"$goodsId"));

			list.add(bean);
			i++;
		}
		return list;
	}

	private ItemPolicyAnalyseResults caculate(List<Map<String, String>> input,
			RankConfInfo goodsRankConfInfo) {
		Policy policy = new Policy();
		policy.setName(goodsRankConfInfo.getRankCode());
		String condition = goodsRankConfInfo.getRankExpression();
		policy.setCondition(condition);
		policy.setConf(goodsRankConfInfo.getRankExpressionConf());

		ItemPolicyAnalyseResults analyseResult = PolicyService.caculate(input,
				policy);

		int count = analyseResult.getList().size();
		logger.info(policy.getName()
				+ "-------------------自动榜单top20--begin-------------------------"
				+ goodsRankConfInfo.getRankCode());

		count = Math.min(count, 20);
		for (int i = 0; i < count; i++) {
			logger.info(analyseResult.getList().get(i));

		}
		logger.info(policy.getName()
				+ "-------------------自动榜单top20--end-------------------------"
				+ goodsRankConfInfo.getRankCode());
		return analyseResult;
	}

	/**
	 * 获取分类的分布比例
	 * 
	 * @return
	 */
	private Map<String, Long> getCidSaleCount() {

		Map<String, Object> queryParamMap = new HashMap<String, Object>();
		queryParamMap.put("queryType", 1);
		String orderCreateDate = DateUtil.getSimpleDateStr(DateUtil
				.getDateBefore(new Date(), 7));
		queryParamMap.put("orderCreateDate", orderCreateDate);

		JSONObject orderRate = ServiceCallUtil.callService(
				"goodsOrderRateQuery", queryParamMap);
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			GoodsOrderRateBean orderRateBean = objectMapper.readValue(
					((JSONObject) orderRate.get("goodsOrderRate"))
							.toJSONString(), GoodsOrderRateBean.class);
			return getCidSaleCount(orderRateBean);
		} catch (Exception e) {
			logger.error("获取订单分类比例出错", e);
		}
		return null;

	}

	private Map<String, Long> getCidSaleCount(GoodsOrderRateBean orderRateBean) {
		Map<String, Long> groupToRate = new HashMap<String, Long>();

		Iterator<OneGoodsOrderRateBean> it = orderRateBean.getList().iterator();
		long all = 0L;
		while (it.hasNext()) {
			OneGoodsOrderRateBean oneGoodsOrderRateBean = it.next();
			long l = Long.parseLong(oneGoodsOrderRateBean.getCount());
			all = all + l;
			groupToRate.put(oneGoodsOrderRateBean.getGroupField(), l);
		}

		groupToRate.put("all", all);

		return groupToRate;

	}

	// private ItemPolicyAnalyseResults caculate(List<Map<String, String>>
	// input,GoodsRankConfInfo goodsRankConfInfo) {
	// Policy policy = new Policy();
	// policy.setName("P1");
	// String condition =
	// "$updateDate desorderby $updateDate&&$price ascorderby $price&&$sourceMallCode desorderby $sourceMallCode";
	// policy.setCondition(condition);
	// policy.setConf("{\"name\":\"儿童\",\"price\":\"23\",\"volume\":\"10\",\"review\":\"100\"}");
	//
	// ItemPolicyAnalyseResults analyseResult = PolicyService.caculate(input,
	// policy);
	//
	// int count = analyseResult.getList().size();
	// logger.info(policy.getName()
	// + "------自动榜单top20-------------------------");
	//
	// count = Math.min(count, 20);
	// for (int i = 0; i < count; i++) {
	// logger.info(analyseResult.getList().get(i));
	//
	// }
	// logger.info(policy.getName()
	// + "------自动榜单top20-------------------------");
	// return analyseResult;
	// }

	/**
	 * 获取策略需要的输入list
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, String>> buildPolicyInputlist(
			List<Map<String, Object>> list, Map<String, Long> cidToSaleCount) {
		List<Map<String, String>> input = new ArrayList<Map<String, String>>();
		for (Map<String, Object> record : list) {
			logger.debug("record:" + record);
			Map<String, String> map = new HashMap<String, String>();
			map.put("$goodsName", (String) record.get("GOODSNAME"));
			map.put("$goodsId", record.get("GOODSID").toString());
			Number decimal = (Number) record.get("PRICE");
			long price = (long) (decimal.doubleValue() * 100);
			map.put("$price", String.valueOf(price));
			map.put("$updateDate", (String) record.get("UPDATEDATE"));
			map.put("$createDate", (String) record.get("CREATEDATE"));
			map.put("$categoryName", (String) record.get("CATEGORYNAME"));
			map.put("$sourceMallCode", record.get("SOURCEMALLCODE").toString());

			if (null != record.get("CID")
					&& !"".equals(record.get("CID").toString())
					&& null != cidToSaleCount
					&& cidToSaleCount.containsKey(record.get("CID").toString())) {
				map.put("$cidSaleCount",
						cidToSaleCount.get(record.get("CID").toString())
								.toString());
			} else {
				map.put("$cidSaleCount", "0");
			}

			if (null != cidToSaleCount && cidToSaleCount.containsKey("all")) {
				map.put("$allSaleCount", cidToSaleCount.get("all").toString());
			} else {
				map.put("$allSaleCount", "0");
			}

			if (record.containsKey("SALECOUNT")
					&& null != record.get("SALECOUNT")) {
				map.put("$saleCount", record.get("SALECOUNT").toString());
			} else {
				map.put("$saleCount", "10");
			}

			if (record.containsKey("ORIPRICE")
					&& null != record.get("ORIPRICE")) {
				Number decimal2 = (Number) record.get("ORIPRICE");
				long oriPrice = (long) (decimal2.doubleValue() * 100);
				map.put("$oriPrice", String.valueOf(oriPrice));
			}

			if (record.containsKey("COMMENTCOUNT")
					&& null != record.get("COMMENTCOUNT")) {
				map.put("$commentCount", record.get("COMMENTCOUNT").toString());
			} else {
				map.put("$commentCount", "10");
			}

			if (record.containsKey("COUPON") && null != record.get("COUPON")) {
				map.put("$coupon", record.get("COUPON").toString());
			} else {
				map.put("$coupon", "");
			}

			input.add(map);
		}
		return input;
	}

	public static void main(String[] args) {
		long l = 12L;
		long all = 99L;

		double d = (double) l / all;
		System.out.println(StringUtil.keepDouble(d));
	}
}

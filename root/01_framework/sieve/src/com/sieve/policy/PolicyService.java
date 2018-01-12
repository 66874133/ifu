package com.sieve.policy;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sieve.core.analyse.CombineConditionAnalyzer;
import com.sieve.core.analyse.ConditionAnalyzer;
import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;
import com.sieve.core.condition.UndefinedConditionAnalyzerException;
import com.sieve.core.score.comparable.LargeCharScoreComparator;

public class PolicyService {

	private final static Logger logger = Logger.getLogger(PolicyService.class);

	private static Map<String, ConditionAnalyzer> operatorToConditionAnalyzer = new HashMap<String, ConditionAnalyzer>();

	private static Map<String, CombineConditionAnalyzer> operatorToCombineConditionAnalyzer = new HashMap<String, CombineConditionAnalyzer>();

	public static ItemPolicyAnalyseResults caculate(
			List<Map<String, String>> list, Policy policy) throws UndefinedConditionAnalyzerException {
		ItemPolicyAnalyseResults policyAnalyseResult = calculate(list, policy);

		sort(policyAnalyseResult, new LargeCharScoreComparator());
		return policyAnalyseResult;

	}

	public static ItemPolicyAnalyseResults combineCaculate(
			List<Map<String, String>> list, List<Policy> policys,
			String combineExpression) throws UndefinedConditionAnalyzerException {
		Map<String, ItemPolicyAnalyseResults> input = new HashMap<String, ItemPolicyAnalyseResults>();

		for (Policy policy : policys) {
			ItemPolicyAnalyseResults policyAnalyseResult = caculate(list,
					policy);

			input.put(policy.getName(), policyAnalyseResult);
		}

		return getCombineConditionAnalyzer(combineExpression).analyze(input);

	}

	private static void sort(ItemPolicyAnalyseResults policyAnalyseResult,
			Comparator<OneItemPolicyAnalyseResult> comparator) {

		int count = policyAnalyseResult.getList().size();
		logger.debug("---排序前----------------------");
		for (int i = 0; i < count; i++) {

			logger.debug(policyAnalyseResult.getList().get(i));

		}

		Collections.sort(policyAnalyseResult.getList(), comparator);

		logger.debug("---排序后----------------------");
		for (int i = 0; i < count; i++) {

			logger.debug(policyAnalyseResult.getList().get(i));

		}

	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getJsonMap(String json) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, String> map = objectMapper.readValue(json, Map.class);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static ItemPolicyAnalyseResults calculate(
			List<Map<String, String>> list, Policy policy) throws UndefinedConditionAnalyzerException {

		ItemPolicyAnalyseResults results = new ItemPolicyAnalyseResults();
		for (Map<String, String> input : list) {
			OneItemPolicyAnalyseResult analyseResult = calculate(input, policy);
			// 如果返回为空引擎会丢弃该条数据
			if (null != analyseResult) {
				// ScoreBuilderFactory.getScoreBuilder("&&").build(analyseResult);
				results.getList().add(analyseResult);
			}

		}
		return results;
	}

	/**
	 * 使用指定的策略来分析输入数据
	 * 
	 * @param input
	 *            输入数据
	 * @param policy
	 *            策略
	 * @return 如果返回为空引擎会丢弃该条数据
	 * @throws UndefinedConditionAnalyzerException 
	 */
	private static OneItemPolicyAnalyseResult calculate(
			Map<String, String> input, Policy policy) throws UndefinedConditionAnalyzerException {
		long s = System.currentTimeMillis();

		// 将配置值代入到表达式的变量
		String condition = policy.getCondition();
		Map<String, String> confValues = getJsonMap(policy.getConf());

		condition = replaceConditionConfValues(condition, confValues);

		// 获取解析器
		ConditionAnalyzer analyzer = getConditionAnalyzer(condition);
		OneItemPolicyAnalyseResult analyseResult = analyzer.analyze(input);
		// 如果返回为空引擎会丢弃该条数据
		if (null != analyseResult) {
			analyseResult.setInput(input);
		}

		logger.debug("OneItemPolicyAnalyseResult calculate time = "
				+ (System.currentTimeMillis() - s));

		return analyseResult;
	}

	private static String replaceConditionConfValues(String condition,
			Map<String, String> confValues) {

		Iterator<String> iterator = confValues.keySet().iterator();

		while (iterator.hasNext()) {
			String confKey = iterator.next();

			condition = condition.replace("#" + confKey,
					confValues.get(confKey));
		}

		return condition;
	}

	private static ConditionAnalyzer getConditionAnalyzer(String condition) {
		if (!operatorToConditionAnalyzer.containsKey(condition)) {
			operatorToConditionAnalyzer.put(condition, new ConditionAnalyzer(
					condition));

		}
		return operatorToConditionAnalyzer.get(condition);

	}

	private static CombineConditionAnalyzer getCombineConditionAnalyzer(
			String condition) {
		if (!operatorToCombineConditionAnalyzer.containsKey(condition)) {
			operatorToCombineConditionAnalyzer.put(condition,
					new CombineConditionAnalyzer(condition));

		}
		return operatorToCombineConditionAnalyzer.get(condition);

	}

}

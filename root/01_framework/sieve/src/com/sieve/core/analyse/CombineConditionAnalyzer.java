package com.sieve.core.analyse;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.analyse.result.combine.AnalyseResultCombineFactory;
import com.sieve.core.analyse.result.combine.IAnalyseResultCombine;

public class CombineConditionAnalyzer {

	private String condition;
	private Map<String, String> nameToExp = new HashMap<String, String>();

	public CombineConditionAnalyzer(String condition) {
		this.condition = condition;
		initExpression();
	}

	private void initExpression() {
		condition = initExpression(condition, 0);
		System.out.println("expression=" + condition);
	}

	private String initExpression(String str, int recurrenceCount) {
		System.out.println("father str = " + str);
		recurrenceCount++;
		if (!str.contains("(")) {
			return str;
		}
		Stack<Integer> stack = new Stack<Integer>();
		int index = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case '(':
				stack.add(i);
				break;
			case ')':
				index = stack.pop();
				if (index > 0) {
					String one = str.substring(index + 1, i);
					String newStr = str.replace("(" + one + ")", "EXP_"
							+ recurrenceCount);
					nameToExp.put("EXP_" + recurrenceCount, one);
					return initExpression(newStr, recurrenceCount);
				}
				break;
			default:
				break;
			}
		}
		return "";
	}

	public ItemPolicyAnalyseResults analyze(
			Map<String, ItemPolicyAnalyseResults> input) {
		System.out.println("condition = " + condition);
		if (!condition.contains("(")) {
			return analyze(condition, input);
		}
		return null;
	}

	/**
	 * 解析带括号的最小一组条件
	 * 
	 * @param condition
	 * @return
	 */
	private ItemPolicyAnalyseResults analyze(String condition,
			Map<String, ItemPolicyAnalyseResults> input) {

		String[] strs = condition.split(" ");

		ItemPolicyAnalyseResults results = null;
		ItemPolicyAnalyseResults results2 = null;
		if (strs[0].contains("EXP_")) {
			results = analyze(nameToExp.get(strs[0]), input);
		} else {
			results = input.get(strs[0]);
		}

		IAnalyseResultCombine combine = AnalyseResultCombineFactory
				.getAnalyseResultCombine(strs[1]);

		if (strs[2].contains("EXP_")) {
			results2 = analyze(nameToExp.get(strs[2]), input);
		} else {
			results2 = input.get(strs[2]);
		}

		return combine.combine(results, results2);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		Map<String, ItemPolicyAnalyseResults> input = new HashMap<String, ItemPolicyAnalyseResults>();

		input.put("P1", new ItemPolicyAnalyseResults());
		input.put("P2", new ItemPolicyAnalyseResults());
		input.put("P3", new ItemPolicyAnalyseResults());
		input.put("P5", new ItemPolicyAnalyseResults());
		String condition = "P5 ∩ (P3 across (P2 ∪ P1))";
		CombineConditionAnalyzer analyzer = new CombineConditionAnalyzer(
				condition);

		System.out.println(analyzer.analyze(input));

		System.out.println(System.currentTimeMillis() - s);
	}
}

package com.sieve.core.analyse;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;
import com.sieve.core.condition.ConditionAnalyzerFactory;
import com.sieve.core.condition.IOneAnalyzer;
import com.sieve.core.condition.UndefinedConditionAnalyzerException;

public class ConditionAnalyzer {
	private final Logger logger = Logger.getLogger(this.getClass());
	private String condition;
	private Map<String, String> nameToExp = new HashMap<String, String>();

	public ConditionAnalyzer(String condition) {
		this.condition = condition;
		initExpression();
	}

	private void initExpression() {
		condition = initExpression(condition, 0);
		logger.debug("expression=" + condition);
	}

	private String initExpression(String str, int recurrenceCount) {
		logger.info("father str = " + str);
		recurrenceCount++;
		if (!str.contains("(")) {
			return str;
		}
		Stack<Integer> stack = new Stack<Integer>();
		int index = -1;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case '(':
				stack.add(i);
				break;
			case ')':
				index = stack.pop();
				if (index >= 0) {
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

	/**
	 * 分析源数据并得到分析结果
	 * 
	 * @param input
	 *            被检测的源数据
	 * @return 如果返回为空引擎会丢弃该条数据
	 * @throws UndefinedConditionAnalyzerException 
	 */
	public OneItemPolicyAnalyseResult analyze(Map<String, String> input) throws UndefinedConditionAnalyzerException {
		logger.debug("condition = " + condition);
		if (!condition.contains("(")) {
			return analyze(condition, input);
		}
		return null;
	}

	

	/**
	 * 代入一个表达式进行分析
	 * 
	 * @param condition
	 * @param input
	 * @return 当未定义解析语法和出现输入值不匹配时返回null 如果返回为空引擎会丢弃该条数据
	 * @throws UndefinedConditionAnalyzerException 
	 */
	private OneItemPolicyAnalyseResult analyze(String condition,
			Map<String, String> input) throws UndefinedConditionAnalyzerException {

		OneItemPolicyAnalyseResult analyseResult = new OneItemPolicyAnalyseResult();
		analyseResult.setInput(input);

		String score = analyzeOne(condition, input);
		logger.debug("child score:" + score + " expression=" + condition);
		if (null != score) {
			analyseResult.addResultScore(condition, score);
		}// 如果返回为空引擎会丢弃该条数据
		else {
			logger.warn("null score ，abandon!  eachConditions=" + condition);
			return null;
		}

		return analyseResult;
	}

	/**
	 * 解析一个最小子表达式
	 * 
	 * @param oneCondition
	 *            最小子表达式
	 * @param input
	 *            输入值
	 * @return 如果返回为空引擎会丢弃该条数据
	 * @throws UndefinedConditionAnalyzerException
	 */

	private String analyzeOne(String oneCondition, Map<String, String> input)
			throws UndefinedConditionAnalyzerException {

		String[] strs = oneCondition.split(" ");
		String confValue = null;
		String inputVariable = strs[0];
		if (strs[0].startsWith("EXP_")) {
			String inputVariableValue = analyzeOne(nameToExp.get(strs[0]),
					input);
			input.put(strs[0], inputVariableValue);
		}

		if (strs.length > 2) {
			if (strs[2].startsWith("EXP_")) {
				confValue = analyzeOne(nameToExp.get(strs[2]), input);
			} else {
				confValue = strs[2];
			}
		}

		IOneAnalyzer oneAnalyzer = ConditionAnalyzerFactory
				.getOneAnalyzer(strs[1]);

		if (null != oneAnalyzer) {
			String ret = oneAnalyzer.analyse(inputVariable, confValue, input);
			logger.debug("score:" + ret + " father str = " + oneCondition);
			if (!IOneAnalyzer.NOT_MATCH.equals(ret)) {
				return ret;
			}

		}
		return null;

	}

}

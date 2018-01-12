package com.sieve.core.condition;

import java.util.Map;

public interface IOneAnalyzer {

	public String SIMPLE_MATCH = "1";

	/**
	 * 数据不匹配 遇到此情况引擎会从排序列表中丢弃包含此值的数据
	 */
	public String NOT_MATCH = "0";

	/**
	 * 解析一个策略表达式中的一个最小子表达式
	 * 
	 * @param inputVariable
	 *            源数据输入值
	 * @param confValue
	 *            策略配置值
	 * @param input
	 *            所有输入源键值对
	 * @return
	 */
	public String analyse(String inputVariable, String confValue,
			Map<String, String> input);
}

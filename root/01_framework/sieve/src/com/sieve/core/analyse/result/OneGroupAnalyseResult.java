package com.sieve.core.analyse.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有待处理数据在一个子策略下形成的结果集
 * 
 * @author jiangbo3
 * 
 */
public class OneGroupAnalyseResult {

	/**
	 * 每一个List<OneItemSubPolicyAnalyseResult>表示所有待处理数据在一个子策略下形成的结果集
	 * 每一个元素表示一个item在一个子策略下的分析结果
	 */
	private List<OneItemPolicyAnalyseResult> list = new ArrayList<OneItemPolicyAnalyseResult>();

	public List<OneItemPolicyAnalyseResult> getList() {
		return list;
	}

	public void setList(List<OneItemPolicyAnalyseResult> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "OneGroupAnalyseResult [list=" + list + "]";
	}

	

}

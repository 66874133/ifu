package com.sieve.core.analyse.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 多条数据在一个策略下产生的解析结果
 * 
 * @author jiangbo3
 * 
 */
public class ItemPolicyAnalyseResults {

	private List<OneItemPolicyAnalyseResult> list = new ArrayList<OneItemPolicyAnalyseResult>();

	public void addOneAnalyseResult(OneItemPolicyAnalyseResult oneAnalyseResult) {
		list.add(oneAnalyseResult);
	}

	public List<OneItemPolicyAnalyseResult> getList() {
		return list;
	}

	public void setList(List<OneItemPolicyAnalyseResult> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ItemPolicyAnalyseResults [list=" + list + "]";
	}

}

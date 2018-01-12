package com.sieve.core.analyse.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 按子策略分组的解析结果集合
 * 
 * @author jiangbo3
 * 
 */
public class SortedGroupAnalyseResult {

	/**
	 * 每一个List<OneAnalyseResult>表示一个表达始终的一组解析结果，一个完整的表达式有1个以上的分组结果
	 */
	private List<OneGroupAnalyseResult> list = new ArrayList<OneGroupAnalyseResult>();

	public List<OneGroupAnalyseResult> getList() {
		return list;
	}

	public void setList(List<OneGroupAnalyseResult> list) {
		this.list = list;
	}

	public void addOneGroup(OneGroupAnalyseResult group) {
		list.add(group);
	}

	@Override
	public String toString() {
		return "SortedGroupAnalyseResult [list=" + list + "]";
	}

}

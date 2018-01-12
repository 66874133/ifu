package com.sieve.core.analyse.result.combine;

import java.util.HashMap;
import java.util.Map;

public class AnalyseResultCombineFactory {

	private static Map<String, IAnalyseResultCombine> map = new HashMap<String, IAnalyseResultCombine>();

	static {
		map.put("∩", new IntersectionAnalyseCombine());
		map.put("∪", new UnionAnalyseResult());
		map.put("across", new AcrossAnalyseResult());
	}

	public static IAnalyseResultCombine getAnalyseResultCombine(String operator) {
		return map.get(operator);
	}
}

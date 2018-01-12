package com.sieve.core.analyse.result.combine;

import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;

public class IntersectionAnalyseCombine implements IAnalyseResultCombine {


	public ItemPolicyAnalyseResults combine(ItemPolicyAnalyseResults results,
			ItemPolicyAnalyseResults results2) {
		ItemPolicyAnalyseResults analyseResults = new ItemPolicyAnalyseResults();

		for (OneItemPolicyAnalyseResult oneItemPolicyAnalyseResult : results2
				.getList()) {
			if (results.getList().contains(oneItemPolicyAnalyseResult)) {
				analyseResults.addOneAnalyseResult(oneItemPolicyAnalyseResult);
			}
		}

		return analyseResults;
	}

}

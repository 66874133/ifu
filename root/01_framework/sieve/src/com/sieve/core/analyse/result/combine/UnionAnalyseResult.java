package com.sieve.core.analyse.result.combine;

import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;

public class UnionAnalyseResult implements IAnalyseResultCombine {

	

	public ItemPolicyAnalyseResults combine(ItemPolicyAnalyseResults results,
			ItemPolicyAnalyseResults results2) {
		// ItemPolicyAnalyseResults analyseResults = new
		// ItemPolicyAnalyseResults();

		for (OneItemPolicyAnalyseResult oneItemPolicyAnalyseResult : results2
				.getList()) {
			if (!results.getList().contains(oneItemPolicyAnalyseResult)) {
				results.addOneAnalyseResult(oneItemPolicyAnalyseResult);
			}
		}

		return results;
	}

}

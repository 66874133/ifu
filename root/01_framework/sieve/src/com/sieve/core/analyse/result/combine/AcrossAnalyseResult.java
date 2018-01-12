package com.sieve.core.analyse.result.combine;

import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;

public class AcrossAnalyseResult implements IAnalyseResultCombine {


	public ItemPolicyAnalyseResults combine(ItemPolicyAnalyseResults results,
			ItemPolicyAnalyseResults results2) {
		ItemPolicyAnalyseResults analyseResults2 = new ItemPolicyAnalyseResults();

		ItemPolicyAnalyseResults ret = new ItemPolicyAnalyseResults();

		for (OneItemPolicyAnalyseResult oneItemPolicyAnalyseResult : results2
				.getList()) {
			if (!results.getList().contains(oneItemPolicyAnalyseResult)) {
				analyseResults2.addOneAnalyseResult(oneItemPolicyAnalyseResult);
			}
		}

		int resultsSize = results.getList().size();
		int resultsSize2 = analyseResults2.getList().size();

		int size = Math.min(resultsSize, resultsSize2);
		for (int i = 0; i < size; i++) {
			ret.addOneAnalyseResult(results.getList().get(i));
			ret.addOneAnalyseResult(analyseResults2.getList().get(i));
		}

		int count = Math.abs(resultsSize - resultsSize2);
		if (resultsSize > resultsSize2) {
			for (int i = 0; i < count; i++) {
				ret.addOneAnalyseResult(results.getList().get(size + i));
			}
		} else {
			for (int i = 0; i < count; i++) {
				ret.addOneAnalyseResult(analyseResults2.getList().get(size + i));
			}
		}

		return results;
	}

}

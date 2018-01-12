package com.sieve.core.analyse.result.combine;

import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;

public interface IAnalyseResultCombine {

	public ItemPolicyAnalyseResults combine(ItemPolicyAnalyseResults results,
			ItemPolicyAnalyseResults results2);
}

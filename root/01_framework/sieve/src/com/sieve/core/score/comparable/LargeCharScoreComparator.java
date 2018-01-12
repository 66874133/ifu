package com.sieve.core.score.comparable;

import java.util.Comparator;

import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;

public class LargeCharScoreComparator implements Comparator<OneItemPolicyAnalyseResult> {


	public int compare(OneItemPolicyAnalyseResult o1, OneItemPolicyAnalyseResult o2) {
		return -o1.getScore().compareTo(o2.getScore());

	}

}

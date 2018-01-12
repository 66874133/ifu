package com.sieve.core.score.builder;

import java.util.Iterator;

import com.sieve.core.analyse.result.OneItemPolicyAnalyseResult;

/**
 * 每个表达的的得分汇总器
 * 
 * @author jiangbo3
 * 
 */
public class ScoreBuilder {

	public void build(OneItemPolicyAnalyseResult oneAnalyseResult) {

		Iterator<String> iterator = oneAnalyseResult.getExpressToScore()
				.keySet().iterator();

		StringBuilder builder = new StringBuilder();
		while (iterator.hasNext()) {
			builder.append(oneAnalyseResult.getExpressToScore().get(
					iterator.next()));
		}

		oneAnalyseResult.setScore(builder.toString());
	}

	// }
}

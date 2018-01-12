package com.sieve.core.analyse.result;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 一条数据产生的在一个策略表达下解析结果
 * 
 * @author jiangbo3
 * 
 */
public class OneItemPolicyAnalyseResult {

	public static String NOT_MATCH = "0";
	public static String SIMPLE_MATCH = "1";

	private Map<String, String> expressToScore = new LinkedHashMap<String, String>();
	private Map<String, String> input;
	private String score;

	public void addResultScore(String express, String score) {
		expressToScore.put(express, score);
		this.score = score;
	}

	public Map<String, String> getExpressToScore() {
		return expressToScore;
	}

	public void setExpressToScore(Map<String, String> expressToScore) {
		this.expressToScore = expressToScore;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Map<String, String> getInput() {
		return input;
	}

	public void setInput(Map<String, String> input) {
		this.input = input;
	}

	@Override
	public String toString() {
		return "OneAnalyseResult [expressToScore=" + expressToScore
				+ ", input=" + input + ", score=" + score + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OneItemPolicyAnalyseResult other = (OneItemPolicyAnalyseResult) obj;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		return true;
	}

}

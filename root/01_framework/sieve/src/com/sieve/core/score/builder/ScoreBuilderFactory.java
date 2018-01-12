package com.sieve.core.score.builder;


/**
 * ���ַ�����
 * 
 * @author jiangbo3
 * 
 */
public class ScoreBuilderFactory {

	private static ScoreBuilder builder = new ScoreBuilder();
	
	
	public static ScoreBuilder getScoreBuilder()
	{
		return builder;
	}
}

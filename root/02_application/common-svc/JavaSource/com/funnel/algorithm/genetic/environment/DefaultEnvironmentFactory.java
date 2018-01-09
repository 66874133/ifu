package com.funnel.algorithm.genetic.environment;

import com.funnel.algorithm.genetic.calculator.IGeneticScoreCalculator;
import com.funnel.algorithm.genetic.calculator.RateGeneticScoreCalculator;
import com.funnel.algorithm.genetic.environment.coordinate.AddCoordinate;
import com.funnel.algorithm.genetic.environment.coordinate.ICoordinate;

/**
 * 基础得分累加淘汰劣质个体环境
 * 
 * @author jiangbo3
 * 
 */
public class DefaultEnvironmentFactory extends EnvironmentFactory {

	@Override
	public ICoordinate getCoordinate() {
		IGeneticScoreCalculator defaultGeneticScoreCalculator = new RateGeneticScoreCalculator(
				24);
		ICoordinate enneagram = new AddCoordinate(
				defaultGeneticScoreCalculator, 100);
		return enneagram;
	}


}

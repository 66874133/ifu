package com.funnel.algorithm.genetic.environment;

import com.funnel.algorithm.genetic.calculator.IGeneticScoreCalculator;
import com.funnel.algorithm.genetic.calculator.RateGeneticScoreCalculator;
import com.funnel.algorithm.genetic.environment.coordinate.EnneagramCoordinate;
import com.funnel.algorithm.genetic.environment.coordinate.ICoordinate;

/**
 * 九型人格淘汰劣质个体环境
 * 
 * @author jiangbo3
 * 
 */
public class EnneagramEnvironmentFactory extends EnvironmentFactory {

	@Override
	public ICoordinate getCoordinate() {
		IGeneticScoreCalculator defaultGeneticScoreCalculator = new RateGeneticScoreCalculator(
				24);
		ICoordinate enneagram = new EnneagramCoordinate(defaultGeneticScoreCalculator);
		return enneagram;
	}

}

package com.funnel.algorithm.genetic.environment;

import com.funnel.algorithm.genetic.calculator.GaussianGeneticScoreCalculator;
import com.funnel.algorithm.genetic.calculator.IGeneticScoreCalculator;
import com.funnel.algorithm.genetic.environment.coordinate.ICoordinate;
import com.funnel.algorithm.genetic.environment.coordinate.MaslowCoordinate;

/**
 * 马斯洛需求层次下的个体环境
 * 
 * @author jiangbo3
 * 
 */
public class MaslowEnvironmentFactory extends EnvironmentFactory {

	private ICoordinate enneagram;

	public MaslowEnvironmentFactory() {
		IGeneticScoreCalculator geneticScoreCalculator = new GaussianGeneticScoreCalculator(
				24);
		enneagram = new MaslowCoordinate(geneticScoreCalculator);
	}

	@Override
	public ICoordinate getCoordinate() {

		return enneagram;
	}

}

package com.funnel.algorithm.genetic.evolve;

import com.funnel.algorithm.genetic.Population;
import com.funnel.algorithm.genetic.environment.EnvironmentFactory;

public interface IEvolve {


	/**
	 * 繁衍种群
	 * 
	 * @param population
	 *            当前代
	 * @param environment
	 *            参考环境
	 * @return
	 */
	Population evolve(Population population, EnvironmentFactory environment);

	
	 void statScore(Population population,EnvironmentFactory environment); 
}

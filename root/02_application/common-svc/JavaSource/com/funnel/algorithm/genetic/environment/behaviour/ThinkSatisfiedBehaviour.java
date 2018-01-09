package com.funnel.algorithm.genetic.environment.behaviour;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.IndividualBeanUtil;

/**
 * 思考是否满意
 * 
 * @author jiangbo3
 * 
 */
public class ThinkSatisfiedBehaviour implements IBehaviour {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void action(Individual individual) {
		think(individual);

	}

	public void think(Individual individual) {
		if (IndividualBeanUtil.individualIdToMaslowLevel.containsKey(individual
				.getId())) {
			Double[] doubles = IndividualBeanUtil.individualIdToMaslowLevel
					.get(individual.getId());

			double current = IndividualBeanUtil.getCurrentValue(
					individual.getId(), 0);

			// 当前的比想要的还要多
			if (current >= doubles[0] * 100) {
				logger.info("satisfied:" + individual.getId() + " current="
						+ current + " base=" + doubles[0]);
				IndividualBeanUtil.updateSatisfiedValue(individual.getId(), 0,
						true);
				IndividualBeanUtil.updatePressValue(individual.getId(), 0,
						false);
			} else {
				IndividualBeanUtil
						.updatePressValue(individual.getId(), 0, true);
				IndividualBeanUtil.updateSatisfiedValue(individual.getId(), 0,
						false);
			}
		}

	}
}

package com.funnel.algorithm.genetic.environment.behaviour;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.IndividualBeanUtil;

/**
 * 消耗行为 由压力驱动
 * 
 * @author jiangbo3
 * 
 */
public class ConsumeBehaviour implements IBehaviour {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void action(Individual individual) {
		consume(individual);

	}

	public void consume(Individual individual) {
		// 原始需求值
			Double d = IndividualBeanUtil.individualIdToMaslowLevel
					.get(individual.getId())[0];

			double current = IndividualBeanUtil.getCurrentValue(
					individual.getId(), 0);

			double dd= Math.max(20, d * 100);
			//食物消费有一个初始值
			double n = current - dd;

			
			
		
				// 有结余
				if (n > 0) {
					IndividualBeanUtil.updateCurrentValue(individual.getId(), 0,
							-dd);
				} else {
					IndividualBeanUtil.updateCurrentValue(individual.getId(), 0,
							-current);
					
					int len = (int) -n;
					for (int i = 0; i < len; i++) {
						IndividualBeanUtil
						.updatePressValue(individual.getId(), 0, true);
					}
					
				}
			
			

		

	}
}

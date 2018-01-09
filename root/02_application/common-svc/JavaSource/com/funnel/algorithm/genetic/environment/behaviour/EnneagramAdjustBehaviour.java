package com.funnel.algorithm.genetic.environment.behaviour;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.IndividualBeanUtil;
import com.funnel.algorithm.genetic.environment.enneagram.Enneagrams;

/**
 * 人格调整行为
 * 
 * @author jiangbo3
 * 
 */
public class EnneagramAdjustBehaviour implements IBehaviour {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void action(Individual individual) {
		adjust(individual);

	}

	public void adjust(Individual individual) {

		// 当前需求的压力
		double d = IndividualBeanUtil.getPressValue(individual.getId(), 0);

		/**
		 * 该值分先天和后天两种因素,先天由基因决定,后天被压力干扰
		 * 每个Double[]分别表示人格系数:0完美型,1全爱型,2成就型,3艺术型,4智慧型,5忠诚型,6活跃型,7领袖型,8和平型
		 */
		Enneagrams enneagrams = IndividualBeanUtil.map.get(individual.getId());

		// 压力干扰后前三个表现类型出现波动,自身下降,对端上升
		if (d > 0) {
			for (int i = 0; i < 3; i++) {
				int index = enneagrams.getValues()[i].getType();
				enneagrams.decrease(index);
			}

		}

	}

}

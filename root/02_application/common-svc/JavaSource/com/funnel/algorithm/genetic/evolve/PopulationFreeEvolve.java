package com.funnel.algorithm.genetic.evolve;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.Population;
import com.funnel.algorithm.genetic.environment.EnvironmentFactory;
import com.funnel.algorithm.genetic.environment.coordinate.ICoordinate;

/**
 * 种群大小自由繁殖模式
 * 
 * @author jiangbo3
 * 
 */
public class PopulationFreeEvolve implements IEvolve {

	private Logger logger = Logger.getLogger(this.getClass());

	public Population evolve(Population population,
			EnvironmentFactory environment) {
		// 计算新种群的适应度
		ICoordinate coordinate = environment.getCoordinate();
		//statScore(population,environment);
		coordinate.print();

		List<Individual> childIndividualList = new ArrayList<Individual>();
		int i = 0;
		// 生成下一代种群
		while (population.getIndividualList().size() > 0) {
			Individual p1 = coordinate.getParentRightIndividual(population
					.getIndividualList());
			Individual p2 = coordinate.getParentRightIndividual(population
					.getIndividualList());
			if (null != p1 && null != p2) {

				List<Individual> children = p1.evolve(p2);
				if (children != null) {
					for (Individual one : children) {
						one.setId(i);
						childIndividualList.add(one);
						// System.out
						// .println("p1:" + p1.getId() + "+p2:" +
						// p2.getId()+" ="+chro.getId());
						i++;
					}
					population.getIndividualList().remove(p1);
					population.getIndividualList().remove(p2);

				}
			} else {
				if (population.getIndividualList().size() > 0) {
					population.getIndividualList().remove(0);
				}
				// System.out.println("p1:" + p1 + "+p2:" + p2);
			}
			logger.debug("当前代待繁殖个体数 " + population.getIndividualList().size());
		}
		
		population.setIndividualList(childIndividualList);
		// 新种群替换旧种群
		return population;

	}

	/**
	 * 统计得分情况
	 */
	public void statScore(Population population,EnvironmentFactory environment) {

		for (Individual one : population.getIndividualList()) {
			environment.mapping(one);
		}

	}

	

}

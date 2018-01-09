package com.funnel.algorithm.genetic.evolve;

import java.util.ArrayList;
import java.util.List;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.Population;
import com.funnel.algorithm.genetic.environment.EnvironmentFactory;
import com.funnel.algorithm.genetic.environment.coordinate.ICoordinate;

/**
 * 保留种群个数繁殖模式
 * 
 * @author jiangbo3
 * 
 */
public class PopulationKeepEvolve implements IEvolve {

	public Population evolve(Population population,
			EnvironmentFactory environment) {
		// 计算新种群的适应度
		ICoordinate coordinate = environment.getCoordinate();
		statScore(population, environment);
		coordinate.print();

		List<Individual> childIndividualList = new ArrayList<Individual>();
		int i = 0;
		// 生成下一代种群
		while (childIndividualList.size() < population.getIndividualList()
				.size()) {
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

				}
			} else {
				// System.out.println("p1:" + p1 + "+p2:" + p2);
			}

		}

		population.setIndividualList(childIndividualList);

		return population;

	}

	/**
	 * 统计得分情况
	 */
	public void statScore(Population population, EnvironmentFactory environment) {

		for (Individual one : population.getIndividualList()) {
			environment.mapping(one);
		}

	}

}

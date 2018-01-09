package com.funnel.algorithm.genetic;

import java.util.ArrayList;
import java.util.List;

import com.funnel.algorithm.genetic.environment.EnvironmentFactory;
import com.funnel.algorithm.genetic.environment.MaslowEnvironmentFactory;
import com.funnel.algorithm.genetic.evolve.IEvolve;

/**
 * 种群
 * 
 * @author jiangbo3
 * 
 */
public class Population {

	/**
	 * 个体
	 */
	private List<Individual> individualList = null;
	private int generation = 1;// 当前遗传到第几代

	private List<EnvironmentFactory> environments;
	/**
	 * 繁殖模式
	 */
	private IEvolve evolve;

	/**
	 * 初始化种群，在遗传算法开始时，我们需要初始化一个原始种群，这就是原始的第一代
	 * 
	 * @param popSize
	 *            种群个数
	 * @param chromosomeSize
	 *            个体的染色体个数
	 * @param geneSize
	 *            染色体携带的基因长度
	 * @param evolve
	 *            繁殖模式
	 * @param environment
	 *            环境
	 */
	public Population(int popSize, int chromosomeSize, int geneSize,
			IEvolve evolve, List<EnvironmentFactory> environments) {
		init(popSize, chromosomeSize, geneSize, environments);
		this.evolve = evolve;
		this.environments = environments;
	}

	/**
	 * 初始化种群，在遗传算法开始时，我们需要初始化一个原始种群，这就是原始的第一代
	 * 
	 * @param popSize
	 *            种群个数
	 * @param chromosomeSize
	 *            个体的染色体个数
	 * @param geneSize
	 *            染色体携带的基因长度
	 */
	public void init(int popSize, int chromosomeSize, int geneSize,
			List<EnvironmentFactory> environments) {
		List<Individual> individualList = new ArrayList<Individual>();
		for (int i = 0; i < popSize; i++) {
			Individual individual = new Individual(chromosomeSize, geneSize,
					environments);
			individual.setId(i);
			individualList.add(individual);
		}

		this.individualList = individualList;
	}

	/**
	 * 繁殖
	 * 
	 * @param environment
	 * @return
	 */
	public Population evolve() {
		Population population = null;
		for (EnvironmentFactory environment : environments) {
			evolve.statScore(this, environment);
			if (environment instanceof MaslowEnvironmentFactory) {
				population = evolve.evolve(this, environment);
			}
		}
		return population;
	}

	/**
	 * 在产生下一代的过程中，可能会发生基因变异。
	 */
	public void mutation() {
		for (Individual individual : individualList) {
			individual.mutation();
		}
	}

	public void action() {
		for (Individual individual : individualList) {
			individual.action();
		}
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	/**
	 * 获得所有个体列表
	 * 
	 * @return
	 */
	public List<Individual> getIndividualList() {
		return individualList;
	}

	public void setIndividualList(List<Individual> individualList) {
		this.individualList = individualList;
	}
}

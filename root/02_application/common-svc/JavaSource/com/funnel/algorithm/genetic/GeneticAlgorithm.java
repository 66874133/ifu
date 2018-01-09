package com.funnel.algorithm.genetic;

import org.apache.log4j.Logger;

public class GeneticAlgorithm {

	private Logger logger = Logger.getLogger(this.getClass());
	private int popSize;// 种群数量

	/**
	 * 最大繁衍几代
	 */
	private int maxIterNum = 500;

	/**
	 * 选择可以产生下一代的个体之后，就要交配产生下一代
	 */
	private void evolve(Population population) {
		// 新种群替换旧种群
		population = population.evolve();
		// 基因突变
		population.mutation();

	}

	/**
	 * 开始种群进化
	 * 
	 * @param environment
	 *            进化环境
	 */
	public void caculte(Population population) {
		// 初始化种群
		this.popSize = population.getIndividualList().size();
		while (population.getGeneration() < maxIterNum
				&& population.getIndividualList().size() > 1) {
			// 种群遗传
			evolve(population);

			for (int i = 0; i < 100; i++) {
				population.action();
				IndividualBeanUtil.print();
			}

			print(population);
			population.setGeneration(population.getGeneration() + 1);
		}
	}

	private void print(Population population) {
		logger.info("--------------------------------");
		logger.info("第 " + population.getGeneration() + " 代");
		logger.info("设定的种群大小:" + popSize);
		logger.info("当前代种群大小:" + population.getIndividualList().size());
	}

}

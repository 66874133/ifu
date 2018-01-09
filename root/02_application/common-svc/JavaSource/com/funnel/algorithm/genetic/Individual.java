package com.funnel.algorithm.genetic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.environment.EnvironmentFactory;

/**
 * 个体 (种群中的个体)
 * 
 * @author jiangbo3
 * 
 */
public class Individual {

	private Logger logger = Logger.getLogger(this.getClass());

	private int id;

	/**
	 * 基因变异的概率
	 */
	private double mutationRate = 0.01;
	/**
	 * 最大变异步长
	 */
	private int maxMutationNum = 3;

	private int chromosomeSize;

	private int geneSize;

	private List<EnvironmentFactory> environments;
	/**
	 * 染色体集合
	 */
	private List<Chromosome> chromosomes = null;

	public List<Chromosome> getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(List<Chromosome> chromosomes) {
		this.chromosomes = chromosomes;
	}

	/**
	 * 初始化个体，在遗传算法开始时，我们需要初始化一个种群的个体，这就是原始的第一代
	 * 
	 * @param chromosomeSize
	 *            每个个体中染色体个数
	 * @param geneSize
	 *            每个染色体携带的基因长度
	 */
	public Individual(int chromosomeSize, int geneSize,
			List<EnvironmentFactory> environments) {
		this.chromosomeSize = chromosomeSize;
		this.geneSize = geneSize;
		this.environments = environments;
		init(chromosomeSize, geneSize);
	}

	/**
	 * 初始化个体，在遗传算法开始时，我们需要初始化一个种群的个体，这就是原始的第一代
	 * 
	 * @param chromosomeSize
	 *            染色体个数
	 * @param geneSize
	 *            每个染色体携带的基因长度
	 * @return
	 */
	public void init(int chromosomeSize, int geneSize) {
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		for (int i = 0; i < chromosomeSize; i++) {
			Chromosome chro = new Chromosome(geneSize);
			chro.setId(i);
			chromosomes.add(chro);
		}

		this.chromosomes = chromosomes;
	}

	/**
	 * 在产生下一代的过程中，可能会发生基因变异。
	 */
	public void mutation() {
		for (Chromosome chro : chromosomes) {
			if (Math.random() < mutationRate) { // 发生基因突变
				int mutationNum = (int) (Math.random() * maxMutationNum);
				chro.mutation(mutationNum);
				logger.info("发生基因突变");
			}
		}
	}

	/**
	 * 繁殖
	 * 
	 * @param individual
	 *            进行繁殖的个体(母系或父系)
	 * @return
	 */
	public List<Individual> evolve(Individual individual) {
		int count = 1;
		double d = Math.random();
		if (d < 0.01) {
			count = 2;
			logger.info("count=" + count);
		} else if (d < 0.0001) {
			count = 3;
			logger.info("count=" + count);
		}
		List<Individual> list = new ArrayList<Individual>();
		int id = 0;
		// 每个个体里的所有染色体都需要进行一次交叉组合，有可能产生一个或多个新个体(多胞胎)
		for (int i = 0; i < count; i++) {
			Individual individual2 = new Individual(chromosomeSize, geneSize,
					environments);
			List<Chromosome> childChromosomes = new ArrayList<Chromosome>();
			// j为染色体编号
			for (int j = 0; j < chromosomeSize; j++) {
				Chromosome p1 = getParentRightChromosome(
						individual.getChromosomes(), j);
				Chromosome p2 = getParentRightChromosome(chromosomes, j);
				Chromosome children = Chromosome.geneticOne(p1, p2);
				if (null != children) {
					children.setId(id);
					childChromosomes.add(children);
					id++;
				}
			}
			individual2.setChromosomes(childChromosomes);
			list.add(individual2);
		}
		return list;
	}

	public void action() {
		for (EnvironmentFactory environment : environments) {
			environment.action(this);
		}

	}

	/**
	 * 在计算完种群适应度之后，我们需要使用转盘赌法选取可以产生下一代的个体，这里有个条件就是只有个人的适应度不小于平均适应度才会长生下一代（适者生存）。
	 * 
	 * @return
	 */
	private Chromosome getParentRightChromosome(List<Chromosome> chromosomes,
			int j) {
		return chromosomes.get(j);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

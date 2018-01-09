package com.funnel.algorithm.genetic.environment.coordinate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.Chromosome;
import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.IndividualBeanUtil;
import com.funnel.algorithm.genetic.calculator.IGeneticScoreCalculator;
import com.funnel.algorithm.genetic.environment.behaviour.IBehaviour;

/**
 * 马斯洛模型
 * 
 * @author jiangbo3
 * 
 */
public class MaslowCoordinate implements ICoordinate {

	private Logger logger = Logger.getLogger(this.getClass());

	private IGeneticScoreCalculator geneticScoreCalculator;

	private IBehaviour behaviour;

	private Random random = new Random();

	public MaslowCoordinate(IGeneticScoreCalculator geneticScoreCalculator) {
		this.geneticScoreCalculator = geneticScoreCalculator;
		IndividualBeanUtil.individualIdToMaslowLevel = new HashMap<Integer, Double[]>();
	}

	/**
	 * 8到14号染色体为马斯洛需求层次染色体
	 */
	public void mapping(Individual individual) {
		// 获取个体的人格类型
		Double[] doubles = new Double[6];

		int n = 0;
		for (int i = 9; i < 15; i++) {
			setChromosomeScore(individual.getChromosomes().get(i));
			doubles[n] = individual.getChromosomes().get(i).getScore();
			n++;
		}

		IndividualBeanUtil.individualIdToMaslowLevel.put(individual.getId(),
				doubles);

	}

	/**
	 * 在计算个体适应度的时候，我们需要根据基因计算对应的Y值，这里我们设置两个抽象方法，具体实现由类的实现去实现。
	 * 
	 * @param chro
	 */
	private void setChromosomeScore(Chromosome chro) {
		if (chro == null) {
			return;
		}
		double x = geneticScoreCalculator.changeX(chro);
		double y = geneticScoreCalculator.caculateY(x);
		chro.setScore(y);

	}

	@Override
	public void print() {
		Iterator<Integer> iterator = IndividualBeanUtil.individualIdToMaslowLevel
				.keySet().iterator();

		int m = 0;
		int n = 0;
		while (iterator.hasNext()) {
			int i = iterator.next();
			List<Double> list = Arrays
					.asList(IndividualBeanUtil.individualIdToMaslowLevel.get(i));
			Double d = list.get(0);
			if (d > 0.8) {
				m++;
			} else if (d < 0.2) {
				n++;
			}
			logger.info(i + ":" + list);
		}
		logger.info("m=" + m + " n=" + n);
	}

	public IBehaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(IBehaviour behaviour) {
		this.behaviour = behaviour;
	}

	@Override
	public Individual getParentRightIndividual(List<Individual> individualList) {
		// 产生转盘值
		int size = individualList.size();
		int n = random.nextInt(size);
		return individualList.get(n);
	}

	public MaslowLevel getMaslowLevel() {
		return null;

	}

	private class MaslowLevel {
		private Double[] values = new Double[6];

		public MaslowLevel() {

			for (int i = 0; i < values.length; i++) {
				values[i] = random.nextDouble();
			}

		}

		public Double[] getValues() {
			return values;
		}

		public void setValues(Double[] values) {
			this.values = values;
		}

	}

}

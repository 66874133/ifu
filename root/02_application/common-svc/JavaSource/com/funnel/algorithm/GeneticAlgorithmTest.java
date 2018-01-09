package com.funnel.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.funnel.algorithm.genetic.GeneticAlgorithm;
import com.funnel.algorithm.genetic.Population;
import com.funnel.algorithm.genetic.environment.DefaultEnvironmentFactory;
import com.funnel.algorithm.genetic.environment.EnneagramEnvironmentFactory;
import com.funnel.algorithm.genetic.environment.EnvironmentFactory;
import com.funnel.algorithm.genetic.environment.MaslowEnvironmentFactory;
import com.funnel.algorithm.genetic.environment.behaviour.ConsumeBehaviour;
import com.funnel.algorithm.genetic.environment.behaviour.EnneagramAdjustBehaviour;
import com.funnel.algorithm.genetic.environment.behaviour.IBehaviour;
import com.funnel.algorithm.genetic.environment.behaviour.ThinkSatisfiedBehaviour;
import com.funnel.algorithm.genetic.environment.behaviour.TradeBehaviour;
import com.funnel.algorithm.genetic.evolve.PopulationFreeEvolve;
import com.funnel.algorithm.genetic.evolve.PopulationKeepEvolve;

public class GeneticAlgorithmTest {

	private static int geneSize = 24;// 基因最大长度

	private static int chromosomeSize = 16; // 个体携带染色体个数

	private static int popSize = 10;// 种群数量

	public static void main(String[] args) {
		IBehaviour consumeBehaviour = new ConsumeBehaviour();
		IBehaviour thinkBehaviour = new ThinkSatisfiedBehaviour();
		IBehaviour tradeBehaviour = new TradeBehaviour();
		IBehaviour enneagramAdjustBehaviour = new EnneagramAdjustBehaviour();
		PopulationFreeEvolve freeEvolve = new PopulationFreeEvolve();
		PopulationKeepEvolve keepEvolve = new PopulationKeepEvolve();

		EnvironmentFactory environment = new DefaultEnvironmentFactory();
		EnvironmentFactory environment2 = new EnneagramEnvironmentFactory();
		EnvironmentFactory environment3 = new MaslowEnvironmentFactory();

		List<EnvironmentFactory> environments = new ArrayList<EnvironmentFactory>();
		environments.add(environment3);
		environments.add(environment2);
		

		GeneticAlgorithm algorithm = new GeneticAlgorithm();

		environment2.addBehaviour(enneagramAdjustBehaviour);

		environment3.addBehaviour(consumeBehaviour);
		environment3.addBehaviour(thinkBehaviour);
		environment3.addBehaviour(tradeBehaviour);
		Population population = new Population(popSize, chromosomeSize,
				geneSize, freeEvolve, environments);
		algorithm.caculte(population);
	}
}

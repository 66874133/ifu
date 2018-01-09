package com.funnel.algorithm.genetic.environment.coordinate;

import java.util.List;

import com.funnel.algorithm.genetic.Chromosome;
import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.calculator.IGeneticScoreCalculator;

/**
 * 累计得分坐标系
 * 
 * @author jiangbo3
 * 
 */
public class AddCoordinate implements ICoordinate {
	private int populationSize;// 种群大小
	private double bestScore;// 最好得分
	private double worstScore = 100;// 最坏得分
	private double totalScore;// 总得分
	private double averageScore;// 平均得分

	private double x; // 记录历史种群中最好的X值
	private double y; // 记录历史种群中最好的Y值
	private int geneI;// x y所在代数
	private IGeneticScoreCalculator geneticScoreCalculator;

	public AddCoordinate(IGeneticScoreCalculator geneticScoreCalculator,
			int populationSize) {
		this.geneticScoreCalculator = geneticScoreCalculator;
		this.populationSize = populationSize;
	}

	public void mapping(Individual individual) {

		for (Chromosome chro : individual.getChromosomes()) {
			setChromosomeScore(chro);
			if (chro.getScore() > bestScore) { // 设置最好基因值
				bestScore = chro.getScore();
				if (y < bestScore) {
					x = geneticScoreCalculator.changeX(chro);
					y = bestScore;
					// geneI = population.getGeneration();
				}
			}
			if (chro.getScore() < worstScore) { // 设置最坏基因值
				worstScore = chro.getScore();
			}
			totalScore += chro.getScore();
		}

		averageScore = totalScore / populationSize;
		// 因为精度问题导致的平均值大于最好值，将平均值设置成最好值
		averageScore = averageScore > bestScore ? bestScore : averageScore;
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

	public void print() {
		// System.out.println("第 " + generation + " 代");
		// System.out.println("设定的染色体总数:" + popSize);
		// System.out.println("当前代的染色体总数:" + population.size());
		System.out.println("当前代的染色体最高得分:" + bestScore);
		System.out.println("当前代的染色体最低得分:" + worstScore);
		System.out.println("当前代的染色体平均得分:" + averageScore);
		System.out.println("当前代的染色体总和得分:" + totalScore);
		System.out.println("历史最好代数为:" + geneI + "\tx:" + x + "\t历史最高分:" + y);
	}

	/**
	 * 在计算完种群适应度之后，我们需要使用转盘赌法选取可以产生下一代的个体，这里有个条件就是只有个人的适应度不小于平均适应度才会长生下一代（适者生存）。
	 * 
	 * @return
	 */
	public Individual getParentRightIndividual(List<Individual> individuals) {
		// 产生转盘值
		double slice = Math.random() * totalScore;
		double sum = 0;
		for (Individual individual : individuals) {

			double score = 0;
			for (Chromosome chro : individual.getChromosomes()) {
				score = score + chro.getScore();
			}

			sum += score;
			// 大于转盘值并且适应度不小于平均适应度
			if (sum > slice && score >= averageScore) {
				return individual;
			}
		}
		return null;
	}

}

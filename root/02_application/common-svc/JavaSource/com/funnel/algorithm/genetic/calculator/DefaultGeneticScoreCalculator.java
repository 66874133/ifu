package com.funnel.algorithm.genetic.calculator;

import com.funnel.algorithm.genetic.Chromosome;

public class DefaultGeneticScoreCalculator implements IGeneticScoreCalculator {
	/**
	 * 我们假设基因的长度为24（基因的长度由要求结果的有效长度确定），因此对应的二进制最大值为 1<< 24
	 */
	private int geneSize = 24;

	public int maxNum = 1 << geneSize;

	public DefaultGeneticScoreCalculator(int geneSize) {
		this.geneSize = geneSize;
		maxNum = 1 << geneSize;
	}

	@Override
	public double changeX(Chromosome chro) {
		return ((1.0 * chro.getNum() / maxNum) * 100) + 6;

	}

	@Override
	public double caculateY(double x) {
		return 100 - Math.log(x);

	}

}

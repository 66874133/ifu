package com.funnel.algorithm.genetic.calculator;

import com.funnel.algorithm.genetic.Chromosome;

/**
 * 基因比例得分计算
 * 
 * @author jiangbo3
 * 
 */
public class RateGeneticScoreCalculator implements IGeneticScoreCalculator {
	/**
	 * 我们假设基因的长度为24（基因的长度由要求结果的有效长度确定），因此对应的二进制最大值为 1<< 24
	 */
	private int geneSize = 24;

	public int maxNum = 1 << geneSize;

	public RateGeneticScoreCalculator(int geneSize) {
		this.geneSize = geneSize;
		maxNum = 1 << geneSize;
	}

	@Override
	public double changeX(Chromosome chro) {
		return ((1.0 * chro.getNum() / maxNum) * 100);
	}

	@Override
	public double caculateY(double x) {
		return x;
	}

}

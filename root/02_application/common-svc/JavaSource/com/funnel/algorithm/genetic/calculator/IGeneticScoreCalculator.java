package com.funnel.algorithm.genetic.calculator;

import com.funnel.algorithm.genetic.Chromosome;

public interface IGeneticScoreCalculator {
	/**
	 * @param chro
	 * @return
	 * @Description: 将二进制的基因数值化转换
	 */
	public abstract double changeX(Chromosome chro);

	/**
	 * @param x
	 * @return
	 * @Description: 根据基因数值计算基因评价值 评价函数 Y=F(X)
	 */
	public abstract double caculateY(double x);

}

package com.funnel.algorithm.genetic.calculator;

import com.funnel.algorithm.genetic.Chromosome;

/**
 * 基因比例得分计算
 * 
 * @author jiangbo3
 * 
 */
public class GaussianGeneticScoreCalculator implements IGeneticScoreCalculator {
	/**
	 * 我们假设基因的长度为24（基因的长度由要求结果的有效长度确定），因此对应的二进制最大值为 1<< 24
	 */
	private int geneSize = 24;

	public int maxNum = 1 << geneSize;

	// x 最大为100 按5为一个最小距离单位进行划分
	private int distance = 10;
	// 每一层累加的
	private double a = 10;

	private int n = 100 / distance;
	private int maxY = sum(n, 10);

	public GaussianGeneticScoreCalculator(int geneSize) {
		this.geneSize = geneSize;
		maxNum = 1 << geneSize;
	}

	@Override
	public double changeX(Chromosome chro) {
		return ((1.0 * chro.getNum() / maxNum) * 100);
	}

	@Override
	public double caculateY(double x) {

		double y = 0;

		int i = 1;
		while (x > 0) {

			if (x > distance) {
				y = a * i + y;
			} else {
				y = x * i + y;
			}
			x = x - distance;
			i++;
		}
		return y/maxY;
	}

	/**
	 * a*n 多项式求和
	 * 
	 * @param n
	 *            求前多少项
	 * @param a
	 * @return
	 */
	public static int sum(int n, int a) {
		if (n > 0)
			return a * n + sum(n - 1, a); // 调用递归方法
		else
			return 0; // 当num==0时递归结束(递归出口)
	}

	public static double logx(double bottom, double antilogarithm) {
		if (bottom > 0 && bottom != 1) {
			return Math.log(antilogarithm) / Math.log(bottom);
		}
		return 0;

	}

	public static void main(String[] args) {
		System.out.println(sum(20, 10));
	}
}

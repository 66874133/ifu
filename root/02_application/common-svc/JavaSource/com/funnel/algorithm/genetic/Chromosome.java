package com.funnel.algorithm.genetic;

import java.util.ArrayList;
import java.util.List;

/**
 * 染色体
 * 
 * @author jiangbo3
 * 
 */
public class Chromosome {
	private int id;
	private boolean[] gene;// 基因序列
	private double score;// 对应的函数得分

	public Chromosome() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 初始化染色体
	 * 
	 * @param size
	 *            染色体上基因序列长度
	 */
	public Chromosome(int size) {
		if (size <= 0) {
			return;
		}
		initGeneSize(size);
		for (int i = 0; i < size; i++) {
			gene[i] = Math.random() >= 0.5;
		}
	}

	private void initGeneSize(int size) {
		if (size <= 0) {
			return;
		}
		gene = new boolean[size];
	}

	/**
	 * 把基因转化为对应的值，比如101对应的数字是5，这里采用位运算来实现
	 * 
	 * @return
	 */
	public long getNum() {
		if (gene == null) {
			return 0;
		}
		long num = 0;
		for (boolean bool : gene) {
			num <<= 1;
			if (bool) {
				num += 1;
			}
		}
		return num;
	}

	/**
	 * 变异
	 * 
	 * @param num
	 *            变异步长
	 */
	public void mutation(int num) {
		// 允许变异
		int size = gene.length;
		for (int i = 0; i < num; i++) {
			// 寻找变异位置
			int at = ((int) (Math.random() * size)) % size;
			// 变异后的值
			boolean bool = !gene[at];
			gene[at] = bool;
		}
	}

	/**
	 * 克隆染色体
	 * 
	 * @param c
	 * @return
	 */
	public static Chromosome clone(final Chromosome c) {
		if (c == null || c.gene == null) {
			return null;
		}
		Chromosome copy = new Chromosome();
		copy.initGeneSize(c.gene.length);
		for (int i = 0; i < c.gene.length; i++) {
			copy.gene[i] = c.gene[i];
		}
		return copy;
	}

	/**
	 * 父母双方产生下一代，这里两个个体产生两个个体子代，具体哪段基因差生交叉，完全随机
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
		if (p1 == null || p2 == null) { // 染色体有一个为空，不产生下一代
			return null;
		}
		if (p1.gene == null || p2.gene == null) { // 染色体有一个没有基因序列，不产生下一代
			return null;
		}
		if (p1.gene.length != p2.gene.length) { // 染色体基因序列长度不同，不产生下一代
			return null;
		}
		Chromosome c1 = clone(p1);
		Chromosome c2 = clone(p2);
		// 随机产生交叉互换位置
		int size = c1.gene.length;
		int a = ((int) (Math.random() * size)) % size;
		int b = ((int) (Math.random() * size)) % size;
		int min = a > b ? b : a;
		int max = a > b ? a : b;
		// 对位置上的基因进行交叉互换
		for (int i = min; i <= max; i++) {
			boolean t = c1.gene[i];
			c1.gene[i] = c2.gene[i];
			c2.gene[i] = t;
		}
		List<Chromosome> list = new ArrayList<Chromosome>();
		list.add(c1);
		list.add(c2);
		return list;
	}

	/**
	 * 父母双方染色体产生下一代染色体，这里两个个体产生一条新的染色体，具体哪段基因差生交叉，完全随机
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Chromosome geneticOne(Chromosome p1, Chromosome p2) {
		if (p1 == null || p2 == null) { // 染色体有一个为空，不产生下一代
			return null;
		}
		if (p1.gene == null || p2.gene == null) { // 染色体有一个没有基因序列，不产生下一代
			return null;
		}
		if (p1.gene.length != p2.gene.length) { // 染色体基因序列长度不同，不产生下一代
			return null;
		}
		Chromosome c1 = clone(p1);
		Chromosome c2 = clone(p2);
		// 随机产生交叉互换位置
		int size = c1.gene.length;
		int a = ((int) (Math.random() * size)) % size;
		int b = ((int) (Math.random() * size)) % size;
		int min = a > b ? b : a;
		int max = a > b ? a : b;
		// 对位置上的基因进行交叉互换
		for (int i = min; i <= max; i++) {
			boolean t = c1.gene[i];
			c1.gene[i] = c2.gene[i];
			c2.gene[i] = t;
		}
		List<Chromosome> list = new ArrayList<Chromosome>();
		list.add(c1);
		list.add(c2);

		int rd = Math.random() > 0.5 ? 1 : 0;
		return list.get(rd);
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

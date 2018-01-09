package com.funnel.algorithm.genetic.environment.coordinate;

import java.util.List;

import com.funnel.algorithm.genetic.Individual;

public interface ICoordinate {

	/**
	 * 映射(投射)到坐标系中
	 * 
	 * @param individual
	 */
	public void mapping(Individual individual);

	void print();

	/**
	 * 挑选个体进行繁殖
	 * 
	 * @param individualList
	 * @return
	 */
	public Individual getParentRightIndividual(List<Individual> individualList);
}

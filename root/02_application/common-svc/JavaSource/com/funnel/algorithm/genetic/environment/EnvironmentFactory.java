package com.funnel.algorithm.genetic.environment;

import java.util.ArrayList;
import java.util.List;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.environment.behaviour.IBehaviour;
import com.funnel.algorithm.genetic.environment.coordinate.ICoordinate;

/**
 * 当前环境
 * 
 * @author jiangbo3
 * 
 */
public abstract class EnvironmentFactory {

	private List<IBehaviour> behaviours = new ArrayList<IBehaviour>();

	public void addBehaviour(IBehaviour behaviour) {
		behaviours.add(behaviour);
	}

	/**
	 * 当前环境的数值化参考系
	 * 
	 * @return
	 */
	public abstract ICoordinate getCoordinate();

	/**
	 * 映射坐标系
	 * 
	 * @param one
	 *            个体
	 */
	public void mapping(Individual one) {
		getCoordinate().mapping(one);

	}

	/**
	 * 进行行动
	 * 
	 * @param one
	 *            个体
	 */
	public void action(Individual one) {

		for (IBehaviour b : behaviours) {
			b.action(one);
		}
	}
}

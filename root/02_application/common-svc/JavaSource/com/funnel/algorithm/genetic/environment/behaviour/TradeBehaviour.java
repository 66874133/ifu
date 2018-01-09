package com.funnel.algorithm.genetic.environment.behaviour;

import org.apache.log4j.Logger;

import com.funnel.algorithm.genetic.Individual;
import com.funnel.algorithm.genetic.IndividualBeanUtil;

/**
 * 消费(交易)行为 由压力驱动
 * 
 * @author jiangbo3
 * 
 */
public class TradeBehaviour implements IBehaviour {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void action(Individual individual) {
		trade(individual);

	}

	private void trade(Individual individual) {

		// 当前需求的压力
		double d = IndividualBeanUtil.getPressValue(individual.getId(), 0);

		// 原始需求值
		Double[] doubles = IndividualBeanUtil.individualIdToMaslowLevel
				.get(individual.getId());

		double current = IndividualBeanUtil.getCurrentValue(individual.getId(),
				0);

		double money = IndividualBeanUtil.getMoney(individual.getId());

		// 压力越大购买的越多,超过原始需求值,非理性购买
		double m = (1 + d) * doubles[0]*100;
		trade(individual.getId(), money, m);

	}



	/**
	 * 钱交易成需求度 按1:1交易
	 * 
	 * @param money
	 *            当前的钱
	 * @param m
	 *            想要交易获得的需求度
	 * @return
	 */
	private void trade(int id, double money, double m) {

		// 买的起
		if (money > m) {
			IndividualBeanUtil.updateMoney(id, -m);
			IndividualBeanUtil.updateCurrentValue(id, 0, m);
		} else {// 买不起
			IndividualBeanUtil.updateMoney(id, -money);
			IndividualBeanUtil.updateCurrentValue(id, 0, money);
		}

	}
}

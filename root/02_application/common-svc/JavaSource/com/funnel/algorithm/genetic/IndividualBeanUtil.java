package com.funnel.algorithm.genetic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.funnel.algorithm.genetic.environment.enneagram.Enneagrams;

public class IndividualBeanUtil {

	/**
	 * 该值分先天和后天两种因素,先天由基因决定,后天被压力干扰
	 * 每个Double[]分别表示人格系数:0完美型,1全爱型,2成就型,3艺术型,4智慧型,5忠诚型,6活跃型,7领袖型,8和平型
	 */
	public static Map<Integer, Enneagrams> map = new HashMap<Integer, Enneagrams>();

	/**
	 * 
	 * 每个Double[]分别表示对应系数:0生理需求,1安全需求,2社交需求,3尊重需求,4自我实现需求,5超自我实现需求
	 */
	public static Map<Integer, Double[]> individualIdToMaslowLevel = new HashMap<Integer, Double[]>();

	/**
	 * 每个Double[]分别表示对应满意度:0生理需求,1安全需求,2社交需求,3尊重需求,4自我实现需求,5超自我实现需求
	 */
	private static Map<Integer, Double[]> individualIdToMaslowSatisfiedValue = new HashMap<Integer, Double[]>();

	/**
	 * 每个Double[]分别表示对应压力:0生理需求,1安全需求,2社交需求,3尊重需求,4自我实现需求,5超自我实现需求
	 */
	private static Map<Integer, Double[]> individualIdToMaslowLevelPress = new HashMap<Integer, Double[]>();

	/**
	 * 每个Double[]分别表示对应当前个体拥有的可以满足需求的资源:0生理需求,1安全需求,2社交需求,3尊重需求,4自我实现需求,5超自我实现需求
	 */
	public static Map<Integer, Double[]> individualIdToCurrent = new HashMap<Integer, Double[]>();

	/**
	 * 每个Double 金钱
	 */
	private static Map<Integer, Double> individualIdToMoney = new HashMap<Integer, Double>();

	public static double getMoney(int id) {

		if (individualIdToMoney.containsKey(id)) {
			return individualIdToMoney.get(id);
		}

		return 100;
	}

	public static void updateMoney(int id, double d) {

		if (!individualIdToMoney.containsKey(id)) {
			individualIdToMoney.put(id, 100.00);
		}

		individualIdToMoney.put(id, individualIdToMoney.get(id) + d);

		System.out
				.println("id=" + id + " Money=" + individualIdToMoney.get(id));

	}

	public static double getPressValue(int id, int index) {

		if (individualIdToMaslowLevelPress.containsKey(id)) {
			return individualIdToMaslowLevelPress.get(id)[index];
		}
		return 0;
	}

	public static void updatePressValue(int id, int index, boolean increase) {
		if (!individualIdToMaslowLevelPress.containsKey(id)) {
			individualIdToMaslowLevelPress.put(id, new Double[] { 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0 });
		}
		if (increase) {
			individualIdToMaslowLevelPress.get(id)[index] = individualIdToMaslowLevelPress
					.get(id)[index] + 0.1;
		} else {

			if (individualIdToMaslowLevelPress.get(id)[index] > 0) {
				individualIdToMaslowLevelPress.get(id)[index] = individualIdToMaslowLevelPress
						.get(id)[index] - 0.1;
			}

		}

		System.out.println("id=" + id + " PressValue="
				+ individualIdToMaslowLevelPress.get(id)[index]);

	}

	public static void updateSatisfiedValue(int id, int index, boolean increase) {
		if (!individualIdToMaslowSatisfiedValue.containsKey(id)) {
			individualIdToMaslowSatisfiedValue.put(id, new Double[] { 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0 });
		}
		if (increase) {
			individualIdToMaslowSatisfiedValue.get(id)[index] = individualIdToMaslowSatisfiedValue
					.get(id)[index] + 0.1;
		} else {

			if (individualIdToMaslowSatisfiedValue.get(id)[index] > 0) {
				individualIdToMaslowSatisfiedValue.get(id)[index] = individualIdToMaslowSatisfiedValue
						.get(id)[index] - 0.1;
			}

		}

		System.out.println("id=" + id + " SatisfiedValue="
				+ individualIdToMaslowSatisfiedValue.get(id)[index]);

	}

	/**
	 * 获取当前的值
	 * 
	 * @param id
	 * @param index
	 * @return
	 */
	public static double getCurrentValue(int id, int index) {

		if (!individualIdToCurrent.containsKey(id)) {
			individualIdToCurrent.put(id, new Double[] { Math.random() * 100,
					Math.random() * 100, Math.random() * 100,
					Math.random() * 100, Math.random() * 100,
					Math.random() * 100 });

		}
		return individualIdToCurrent.get(id)[index];
	}

	public static void updateCurrentValue(int id, int index, double d) {
		if (!individualIdToCurrent.containsKey(id)) {
			individualIdToCurrent.put(id, new Double[] { 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0 });
		}

		individualIdToCurrent.get(id)[index] = individualIdToCurrent.get(id)[index]
				+ d;

		System.out.println("id=" + id + " CurrentValue="
				+ individualIdToCurrent.get(id)[index]);

	}

	public static void print() {
		Iterator<Integer> iterator = individualIdToCurrent.keySet().iterator();
		while (iterator.hasNext()) {
			int i = iterator.next();
			System.out.println("id=" + i + " CurrentValue="
					+ individualIdToCurrent.get(i)[0]);
		}

		iterator = individualIdToMaslowSatisfiedValue.keySet().iterator();
		while (iterator.hasNext()) {
			int i = iterator.next();
			System.out.println("id=" + i + " SatisfiedValue="
					+ individualIdToMaslowSatisfiedValue.get(i)[0]);
		}

		iterator = individualIdToMaslowLevelPress.keySet().iterator();
		while (iterator.hasNext()) {
			int i = iterator.next();
			System.out.println("id=" + i + " PressValue="
					+ individualIdToMaslowLevelPress.get(i)[0]);
		}

		iterator = individualIdToMoney.keySet().iterator();
		while (iterator.hasNext()) {
			int i = iterator.next();
			System.out.println("id=" + i + " Money="
					+ individualIdToMoney.get(i));
		}
		
		iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			int i = iterator.next();
			System.out.println("id=" + i + " Enneagrams="
					+ map.get(i));
		}
	}
}

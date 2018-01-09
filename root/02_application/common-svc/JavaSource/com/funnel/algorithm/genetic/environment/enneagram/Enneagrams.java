package com.funnel.algorithm.genetic.environment.enneagram;

import java.util.Arrays;

public class Enneagrams {
	private Enneagram[] values = new Enneagram[9];

	private Enneagram[] increments = new Enneagram[9];

	public Enneagrams(Enneagram[] values) {

		this.values = values;

		for (int i = 0; i < values.length; i++) {
			increments[i] = new Enneagram(0.0);
			increments[i].setType(i);
		}

	}

	public void increase(int index) {
		increments[index].setValue(increments[index].getValue() + 0.1);
	}

	public void decrease(int index) {
		switch (index) {
		case 0:
			increase(3);
			break;
		case 1:
			increase(7);
			break;
		case 2:
			increase(8);
			break;
		case 3:
			increase(1);
			break;
		case 4:
			increase(6);
			break;
		case 5:
			increase(2);
			break;
		case 6:
			increase(0);
			break;
		case 7:
			increase(4);
			break;
		case 8:
			increase(5);
			break;
		default:
			break;
		}

		increments[index].setValue(increments[index].getValue() - 0.1);
	}

	public Enneagram[] getValues() {
		return values;
	}

	public void setValues(Enneagram[] values) {
		this.values = values;
	}

	public Enneagram[] getIncrements() {
		return increments;
	}

	public void setIncrements(Enneagram[] increments) {
		this.increments = increments;
	}

	@Override
	public String toString() {
		return "Enneagrams [values=" + Arrays.toString(values)
				+ ", increments=" + Arrays.toString(increments) + "]";
	}

}

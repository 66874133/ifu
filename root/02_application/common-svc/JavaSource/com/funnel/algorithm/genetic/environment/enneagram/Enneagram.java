package com.funnel.algorithm.genetic.environment.enneagram;

public class Enneagram {

	/**
	 * 0完美型,1全爱型,2成就型,3艺术型,4智慧型,5忠诚型,6活跃型,7领袖型,8和平型
	 */
	private int type;
	private Double value;

	public Enneagram(Double value) {

		this.value = value;

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Enneagram [type=" + type + ", value=" + value + "]";
	}

}

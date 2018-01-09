package com.funnel.algorithm.genetic.environment.enneagram;

import java.util.Comparator;

public class EnneagramComparator implements Comparator<Enneagram> {

	@Override
	public int compare(Enneagram o1, Enneagram o2) {

		return (int) (o2.getValue() - o1.getValue());
	}

}

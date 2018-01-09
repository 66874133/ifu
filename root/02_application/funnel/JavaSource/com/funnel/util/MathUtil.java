package com.funnel.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MathUtil {
	public static int getCloseMultipleNumber(int source, int div) {
		int n = source / div;

		return n * div;
	}

	public static List<ICountStat> getCountStatTop(List<ICountStat> list,
			int top) {
		Collections.sort(list, new CountStatComparator());
		int l = Math.min(list.size(),top);
		return list.subList(0, l);
	}

	private static class CountStatComparator implements Comparator<ICountStat> {
		public int compare(ICountStat arg0, ICountStat arg1) {
			return (int) (arg1.getCountValue() - arg0.getCountValue());
		}
	}

}

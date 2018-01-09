package com.funnel.util;

import java.util.Arrays;

public class ImageRGB {


	private int i;
	private int j;
	private int[] rgb = new int[3];

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int[] getRgb() {
		return rgb;
	}

	public void setRgb(int[] rgb) {
		this.rgb = rgb;
	}

	@Override
	public String toString() {
		return "ImageRGB [i=" + i + ", j=" + j + ", rgb="
				+ Arrays.toString(rgb) + "]";
	}

}

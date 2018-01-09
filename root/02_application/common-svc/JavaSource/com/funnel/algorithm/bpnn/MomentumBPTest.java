package com.funnel.algorithm.bpnn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MomentumBPTest {

	public static void printlnNumber(double[][] ds) {
		for (int i = 0; i < ds.length; i++) {
			String str = "";
			for (int j = 0; j < ds[i].length; j++) {

				str = str + (int) ds[i][j];
			}
			System.out.println(str);
		}
	}

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	/**
	 * Bit转Byte
	 */
	public static byte BitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}

	public static double[] to32DoubleArrays(double[][] ds) {
		double[] list = new double[32];
		int n = 0;

		for (int i = 0; i < ds.length; i++) {
			for (int j = 0; j < ds[i].length; j++) {

				list[n] = ds[i][j];
				n++;
			}

		}

		return list;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {


		double[][] ds = MomentumBP.toBinary(5);

		double[][] ds2 = new double[][] { 
				new double[] { 1, 1, 1, 0 },
				new double[] { 0, 0, 0, 1 }, 
				new double[] { 0, 0, 0, 1 },
				new double[] { 0, 0, 1, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 1, 0, 0, 0 }, 
				new double[] { 1, 1, 1, 1 },
				new double[] { 0, 0, 0, 0 } };

		double[][] ds1 = new double[][] { 
				new double[] { 1, 0, 0, 0 },
				new double[] { 1, 0, 0, 0 }, 
				new double[] { 1, 0, 0, 0 },
				new double[] { 1, 0, 0, 0 }, 
				new double[] { 1, 0, 0, 0 },
				new double[] { 1, 0, 0, 0 }, 
				new double[] { 1, 0, 0, 0 },
				new double[] { 1, 0, 0, 0 } };
		
		double[][] ds3 = new double[][] { 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 } };

		Map<Integer, double[][]> map = new HashMap<Integer, double[][]>();
		map.put(1, ds1);
		map.put(1, ds3);
		map.put(2, ds2);

		System.out.println(System.currentTimeMillis());
		MomentumBP bp = new MomentumBP(32, 15, 4);

		Random random = new Random();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i != 1000; i++) {
			int value = random.nextInt();
			list.add(value);
		}

		// 训练样本
		for (int i = 0; i != 200; i++) {

			Iterator<Integer> iterator = map.keySet().iterator();

			while (iterator.hasNext()) {
				double[] real = new double[4];
				int value = iterator.next();
				switch (value) {
				case 2:
					real[0] = 1;
					break;
				case 1:
					real[1] = 1;
					break;
				default:
					break;
				}

				bp.train(to32DoubleArrays(map.get(value)), real);

			}

		}

		System.out.println("训练完毕");

		double[][] number = new double[][] { 
				new double[] { 1, 1, 1, 0 },
				new double[] { 0, 0, 0, 1 }, 
				new double[] { 0, 0, 0, 1 },
				new double[] { 0, 0, 1, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 1, 0, 0, 0 }, 
				new double[] { 1, 1, 1, 1 },
				new double[] { 0, 0, 0, 0 } };

		double[][] number2 = new double[][] { 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 }, 
				new double[] { 0, 1, 0, 0 },
				new double[] { 0, 1, 0, 0 } };
		
		
		test(bp, number);
		test(bp, number2);
		

	}

	public static void test(MomentumBP bp, double[][] test) {

		double[] result = bp.test(to32DoubleArrays(test));

		double max = -Integer.MIN_VALUE;
		int idx = -1;

		// 找出得分最大的标记位
		// 将目标输出视作一个4维的向量
		// [1,0,0,0]代表正奇数，[0,1,0,0]代表正偶数，[0,0,1,0]代表负奇数，[0,0,0,1]代表负偶数。
		for (int i = 0; i != result.length; i++) {
			if (result[i] > max) {
				max = result[i];
				idx = i;
			}
		}

		switch (idx) {
		case 0:
			System.out.println("识别出数字2");
			break;
		case 1:
			System.out.println("识别出数字1");
			break;
		case 2:

			break;
		case 3:

			break;
		}

	}

}

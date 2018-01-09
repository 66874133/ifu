package com.funnel.algorithm.bpnn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MomentumBPTest2 {
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
//		
//		toInt("GT-I9100G");
		
		System.out.println(System.currentTimeMillis());
		MomentumBP bp = new MomentumBP(32, 15, 4);

		Random random = new Random();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i != 1000; i++) {
			int value = random.nextInt();
			list.add(value);
		}

		double[] real = new double[4];
		real[0] = 1;
		
		double[] real2 = new double[4];
		real2[1] = 1;
		
		// 训练样本1000个，训练200次
		for (int i = 0; i != 200; i++) {
			for (int value : list) {
				
				
				double[] binary = to32Binary("GT-I"+value);
				bp.train(binary, real);
				
//				double[] binary2 = to32Binary(value+"GT-I");
//				bp.train(binary2, real2);
			}
		}

		System.out.println("训练完毕，下面请输入一个任意数字，神经网络将自动判断它是正数还是复数，奇数还是偶数。");

		while (true) {
			byte[] input = new byte[10];
			System.in.read(input);
			//Integer value = Integer.parseInt(new String(input).trim());
			String rawVal = new String(input).trim();
			double[] binary = to32Binary(rawVal);

			double[] result = bp.test(binary);

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
				System.out.println("0->"+rawVal);
				break;
			case 1:
				System.out.println("1->"+rawVal);
				break;
			case 2:
				System.out.println("2->"+rawVal);
				break;
			case 3:
				System.out.println("3->"+rawVal);
				break;
			}
		}
	}

	private static double[] to32Binary(int value) {
		double[] binary = new double[32];
		int index = 31;
		do {
			binary[index--] = (value & 1);
			value >>>= 1;
		} while (value != 0);

		return binary;
	}
	
	
	private static double[] to32Binary(String value) {
		double[] binary = new double[32];
	
        byte[]  bs = value.getBytes();
		
		for (int i = 0; i < bs.length; i++) {
			//System.out.println(i+":"+bs[i]);
			binary[i] = bs[i];
		}
		return binary;
	}
	
	

	

}

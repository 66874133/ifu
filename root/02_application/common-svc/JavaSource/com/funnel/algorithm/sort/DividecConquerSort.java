package com.funnel.algorithm.sort;

/**
 * 归并排序
 */
public class DividecConquerSort {

	public static void merge(int[] a, int low, int mid, int high) {// 对两组已经排序的数组进行合并
		int[] b = new int[high - low + 1]; // 临时数组，存储个数为high - low + 1个数据
		int s = low;
		int t = mid + 1;
		int k = 0;
		while (s <= mid && t <= high) {// 直至前半部或后半部数据完全录入暂存
			if (a[s] <= a[t]) // 如果前半部的数据小于后半部的，前半部数据暂存
				b[k++] = a[s++];
			else
				// 否则后半部数据暂存，并下标自加
				b[k++] = a[t++];
		}
		while (s <= mid)
			// 将未参与比较的数组追加到后面,只有当该值始终是较大的一个时,才会导致索引不会发生偏移
			b[k++] = a[s++];
		while (t <= high)
			b[k++] = a[t++];
		for (int i = 0; i < b.length; i++) { // 将暂存的数据重新填充至array[low]--array[high]中
			a[low + i] = b[i];
		}

		System.out.println("排序调整：");
		printArray(a);
	}

	public static void mergesort(int a[], int low, int high) {// 对数组进行递归排序
		int mid;
		if (low < high) {
			mid = (low + high) / 2;
			mergesort(a, low, mid);
			mergesort(a, mid + 1, high);
			merge(a, low, mid, high);
		}
	}

	public static void printArray(int[] arrays) {
		for (int i = 0; i < arrays.length; i++) {
			System.out.print(arrays[i] + " ");
		}
		System.out.print("\r\n");
	}

	public static void main(String[] args) {
		int[] a = { 6, 3, 9, 4, 8, 1 };
		System.out.println("����ǰ����Ϊ��");
		printArray(a);
		mergesort(a, 0, a.length - 1);
		System.out.println("\n���������Ϊ��");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
	}

}

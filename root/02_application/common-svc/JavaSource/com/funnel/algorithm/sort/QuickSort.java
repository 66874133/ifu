/**
 * 
 */
package com.funnel.algorithm.sort;

/**
 * 快速排序
 * 选择一个基准值，将小于基值的放在左边，大于的放后面递归完成
 * 
 */
public class QuickSort {
	public static void main(String[] args) {
		//int arr[] = { 40, 30, 68, 98, 86, 15, 57 };
		int arr[] = { 6, 8, 7, 9, 3, 4, 5 };
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.print("\r\n");
		quickSort(arr, 0, arr.length - 1);
		for (int i : arr) {
			System.out.print(i + " ");
		}

	}

	public static void quickSort(int[] nums, int low, int high) {

		if (low < high) {
			int dp = partition(nums, low, high);
			quickSort(nums, low, dp - 1);
			quickSort(nums, dp + 1, high);
		}
	}

	/**
	 * 
	 * @param nums 输入数组
	 * @param low 左边游标 负责找到比基准值大的
	 * @param high 右边游标 负责找到比基准值小的
	 * @return
	 */
	public static int partition(int[] nums, int low, int high) {
		//取第一个元素为基准元素
		int pivot = nums[low];
		
		while(low<high)
		{
			//右部游标遍历直到找到比基准值小的位置
			while(low<high&&nums[high]>=pivot)
			{
				high--;
			}
			//将该值 填充基值对应的索引位置
			nums[low] = nums[high];
			
			//左部游标遍历直到找到比基准值大的位置移动索引并跳出循环
			while(low<high&&nums[low]<=pivot)
			{
				low++;
			}
			
			//将该值与右边的游标值交换 该步骤相当于是交换了左右游标的值，因为上面已经把右边的游标值放到最左边了
			//相当于利用基准值的索引位置来进行了一次左右游标的交换
			nums[high] = nums[low];
			
			
		}
		//由于基准值位置被占用过，所以此处需要还原基准值位置,此处还原会将基准值设置到排序好的数组中间，从而保证
		//基准值的左边的值更小，右边的更大
		nums[low] = pivot;
		
		for (int i : nums) {
			System.out.print(i + " ");
		}
		System.out.print("\r\n");
		return low;
	
	}
}

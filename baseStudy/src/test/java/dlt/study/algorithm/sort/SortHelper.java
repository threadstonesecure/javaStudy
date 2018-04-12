package dlt.study.algorithm.sort;

public class SortHelper {

    /*
     * 快速排序  http://wangkuiwu.github.io/2013/05/02/quick-sort/
     *快速排序的时间复杂度在最坏情况下是O(N2)，平均的时间复杂度是O(N*lgN)。
     * 参数说明：
     *     a -- 待排序的数组
     *     left -- 数组的左边界(例如，从起始位置开始排序，则left=0)
     *     right -- 数组的右边界(例如，排序截至到数组末尾，则right=a.length-1)
     */
    public static void quickSort(int a[], int left, int right) {
        if (left < right) {
            int i, j, x;
            i = left;
            j = right;
            x = a[i];
            while (i < j) {
                while (i < j && a[j] >= x) // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    a[i++] = a[j]; // a[i] = a[j]; i++;

                while (i < j && a[i] <= x)  // 从左向右找第一个大于x的数
                    i++;
                if (i < j)
                    a[j--] = a[i]; //a[j] = a[i]; j--;
            }
            a[i] = x;
            quickSort(a, left, i - 1); /* 递归调用 */
            quickSort(a, i + 1, right); /* 递归调用 */
        }
    }

    /**
     冒泡排序(Bubble Sort)，又被称为气泡排序或泡沫排序。 时间复杂度是O(N2) http://wangkuiwu.github.io/2013/05/01/bubble-sort/
     它是一种较简单的排序算法。它会遍历若干次要排序的数列，每次遍历时，它都会从前往后依次的比较相邻两个数的大小；如果前者比后者大，则交换它们的位置。
     这样，一次遍历之后，最大的元素就在数列的末尾！ 采用相同的方法再次遍历时，第二大的元素就被排列在最大元素之前。重复此操作，直到整个数列都有序为止
     */
    /**
     * 参数说明：
     * a -- 待排序的数组
     * n -- 数组的长度
     */
    public static void bubbleSort(int[] a, int n) {

        for (int i = n - 1; i > 0; i--) {
            int flag = 0;
            for (int j = 0; j < i; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    flag = 1;
                }
            }
            if (flag == 0) break;
        }

    }
}

package dlt.study.algorithm.sort;

import org.junit.Test;

/**
 * 最大堆：父结点的键值总是大于或等于任何一个子节点的键值；
 */
public class Max2HeapDemo {

    /**
     * 把一个数组调整为最大堆二叉数
     */
    @Test
    public void toMax2Heap() {
        int a[] = {20, 30, 90, 40, 70, 110, 60, 10, 100, 50, 80};

        int i;
        int n = a.length;
        // 从(n/2-1) --> 0逐次遍历。遍历之后，得到的数组实际上是一个(最大)二叉堆。
        for (i = n / 2 - 1; i >= 0; i--)
            maxheap_down(a, i, n - 1);
        for (int i1 : a) {
            System.out.print(i1 + ",");
        }

        System.out.println();
        // 方法二
        int newA[] = new int[a.length];
        for (int j = 0; j < a.length; j++) {
            maxheap_insert(newA,a[j],j);
        }

        for (int i1 : newA) {
            System.out.print(i1 + ",");
        }


    }


    /*
     * (最大)堆的向下调整算法
     *
     * 注：数组实现的堆中，第N个节点的左孩子的索引值是(2N+1)，右孩子的索引是(2N+2)。
     *     其中，N为数组下标索引值，如数组中第1个数对应的N为0。
     *
     * 参数说明：
     *     a -- 待排序的数组
     *     start -- 被下调节点的起始位置(一般为0，表示从第1个开始)
     *     end   -- 截至范围(一般为数组中最后一个元素的索引)
     */
    public static void maxheap_down(int a[], int start, int end) {
        int c = start;          // 当前(current)节点的位置
        int l = 2 * c + 1;        // 左(left)孩子的位置
        int tmp = a[c];         // 当前(current)节点的大小
        for (; l <= end; c = l, l = 2 * l + 1) {
            // "l"是左孩子，"l+1"是右孩子
            if (l < end && a[l] < a[l + 1])
                l++;        // 左右两孩子中选择较大者，即m_heap[l+1]
            if (tmp >= a[l])
                break;      // 调整结束
            else            // 交换值
            {
                a[c] = a[l];
                a[l] = tmp;
            }
        }
    }


    /*
     * 将data插入到二叉堆中
     *
     * 返回值：
     *     0，表示成功
     *    -1，表示失败
     */
    public static void maxheap_insert(int a[], int data, int tail) {
        a[tail] = data;      // 将"数组"插在表尾
        while (true) {
            int parentIndex = (int) Math.floor(tail / 2);
            if (a[tail] > a[parentIndex]) {
                a[tail] = a[parentIndex];
                a[parentIndex] = data;
                tail = parentIndex;
            } else {
                break;
            }
            if (parentIndex == 0)
                break;
        }
    }

    public static void main(String[] args) {
        int i = (int) Math.floor(1 / 2);
        System.out.println(i);

    }
}

package dlt.study.algorithm.sort;

import org.junit.Test;

public class SortDemo {

    private static int a[] = {10, 20, 30, 90, 40, 70, 110, 60, 10, 100, 50, 80}; // {10, 40, 5, 10, 20, 50};

    @Test
    public void quickSort() {
        SortHelper.quickSort(a, 0, a.length - 1);
        for (int i : a) {
            System.out.print(i + ",");
        }
    }

    @Test
    public void bubbleSort() {
        SortHelper.bubbleSort(a, a.length);
        for (int i : a) {
            System.out.print(i + ",");
        }
    }

}

package dlt.study.concurrent.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;

/**
 * Created by denglt on 2016/6/7.
 */
class ForkJoinSortTask extends RecursiveAction {
    private static ForkJoinPool fjpool = new ForkJoinPool();
    final long[] array;
    final int lo;
    final int hi;
    private int THRESHOLD = 30;


    public ForkJoinSortTask(long[] array) {
        this.array = array;
        this.lo = 0;
        this.hi = array.length - 1;
    }

    public ForkJoinSortTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {
        if (hi - lo < THRESHOLD)
            sequentiallySort(array, lo, hi);
        else {
            int pivot = partition(array, lo, hi);
            coInvoke(new ForkJoinSortTask(array, lo, pivot - 1), new ForkJoinSortTask(array, pivot + 1, hi));
        }
    }

    protected void coInvoke(RecursiveAction a, RecursiveAction b) {
        a.invoke();
        b.invoke();
    }

    /**
     *   移动arry[hi]到位置newPos，并确保  所有array[n<newPos] < array[newPos] < 所有array[newPos>i]
     * @param array
     * @param lo
     * @param hi
     * @return
     */
    public int partition(long[] array, int lo, int hi) {
        long x = array[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (array[j] <= x) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, hi);
        return i + 1;
    }

    private void swap(long[] array, int i, int j) {
        if (i != j) {
            long temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private void sequentiallySort(long[] array, int lo, int hi) {
        Arrays.sort(array, lo, hi + 1);
    }

    public static void run(long[] array) throws  Exception  {
        ForkJoinTask sort = new ForkJoinSortTask(array);
        ForkJoinPool fjpool = new ForkJoinPool();
        ForkJoinTask task =  fjpool.submit(sort);
        task.get();
        fjpool.shutdown();  //执行此方法之后，ForkJoinPool 不再接受新的任务，但是已经提交的任务可以继续执行。
                            // 如果希望立刻停止所有的任务，可以尝试 shutdownNow() 方法。

       //System.out.println(fjpool.awaitTermination(30, TimeUnit.SECONDS));
    }

    public static Future<Void> runTask(long[] array) throws  Exception  {
        ForkJoinTask sort = new ForkJoinSortTask(array);
        ForkJoinTask task =  fjpool.submit(sort);
        return task;
    }

    public static void main_bak(String[] args) throws Exception {
        int length = 10000;
        long[] array = new long[length];
        for (int i=0;i< length;i++ ){
            array[i] = (long)(Math.random()*10000);
        }
        ForkJoinSortTask.run(array);
        for (int i=0;i< length;i++ ){
            System.out.println(i+":"+ array[i]);
        }
    }

    public  static void main(String[] args) throws  Exception {
        int length = 10000;
        long[] array = new long[length];
        for (int i=0;i< length;i++ ){
            array[i] = (long)(Math.random()*10000);
        }

        long[] array2 = new long[length];
        for (int i=0;i< length;i++ ){
            array2[i] = (long)(Math.random()*10000);
        }
        Future<Void> task =  ForkJoinSortTask.runTask(array);
        Future<Void> task2 =  ForkJoinSortTask.runTask(array2);
        task.get();
        task2.get();
        for (int i=0;i< length;i++ ){
            System.out.println(i+":"+ array2[i]);
        }
    }

    public static void main2(String[] args) {
        int length = 10;
        long[] array = new long[length];
        for (int i=0;i< length;i++ ){
            array[i] = (long)(Math.random()*10000);
        }
        System.out.println("原始数组：");
        for (int i=0;i< length;i++ ){
            System.out.println(i+":"+ array[i]);
        }
        ForkJoinSortTask sortTask = new ForkJoinSortTask(array);
        System.out.println("分组后数组：");
        int pivot = sortTask.partition(array,0,length-1);
        System.out.println("pivot:" + pivot);
        for (int i=0;i< length;i++ ){
            System.out.println(i+":"+ array[i]);
        }
    }
}
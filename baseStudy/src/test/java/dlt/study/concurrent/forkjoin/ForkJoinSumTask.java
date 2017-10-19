package dlt.study.concurrent.forkjoin;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by denglt on 2016/6/7.
 */
public class ForkJoinSumTask extends RecursiveTask<Long> {  // 注意 RecursiveTask 方法 中的 exec()  返回的是 true {

    private static long sum1(long start, long end) {
        long s = 0l;

        for (long i = start; i <= end; i++) {
            s += i;
        }

        return s;
    }

    private static long sum(long start, long end) {
        if (end > start) {
            return end + sum(start, end - 1);
        } else {
            return start;
        }
    }

    private static final int THRESHOLD = 10000;
    private int start;
    private int end;


    public ForkJoinSumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    protected Long compute() {
        //System.out.println("Thread ID: " + Thread.currentThread().getId());

        Long sum = 0l;

        if ((end - start) <= THRESHOLD) {
            sum = sum1(start, end);
        } else {
            int middle = (start + end) / 2;
            RecursiveTask<Long> leftTask = new ForkJoinSumTask(start, middle);
            RecursiveTask<Long> rightTask = new ForkJoinSumTask(middle + 1, end);
       /*     leftTask.fork(); 这样写浪费了当前线程
            rightTask.fork();*/
            invokeAll(leftTask,rightTask);
            Long leftResult = leftTask.join();
            Long rightResult = rightTask.join();

            sum = leftResult + rightResult;
        }

        return sum;
    }

    public static void main(String[] args) {
        long beginTime = System.nanoTime();
        System.out.println("The sum from 1 to 1000 is " + sum(1, 1000));
        System.out.println("Time consumed(nano second) By recursive algorithm : " + (System.nanoTime() - beginTime));


        beginTime = System.nanoTime();
        System.out.println("The sum from 1 to 1000000000 is " + sum1(1, 1000000000));
        System.out.println("Time consumed(nano second) By loop algorithm : " + (System.nanoTime() - beginTime));


        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinSumTask task = new ForkJoinSumTask(1,1000000000);
        beginTime = System.nanoTime();
        Future<Long> result = forkJoinPool.submit(task);
        //Long invoke = forkJoinPool.invoke(task);
        try{
            System.out.println("The sum from 1 to 1000000000 is " + result.get());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Time consumed(nano second) By ForkJoin algorithm : " + (System.nanoTime() - beginTime));
    }
}
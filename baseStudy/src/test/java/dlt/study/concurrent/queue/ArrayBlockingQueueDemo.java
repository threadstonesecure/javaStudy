package dlt.study.concurrent.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * A BlockingQueue in which producers may wait for consumers to receive elements.
 * A TransferQueue may be useful for example in message passing applications in which producers sometimes (using method transfer(E)) await receipt of elements by consumers invoking take or poll,
 * while at other times enqueue elements (via method put) without waiting for receipt.
 */

/**
 * ArrayBlockingQueue : 使用Object[]存储数据（预先分配），容量固定，通过(takeIndex,putIndex)循环使用数组
 * LinkedBlockingQueue：使用单向列表存储数据（优于ArrayBlockingQueue），维护两把lock(lockputLock，taskLock）、head、last(tail)
 */
public class ArrayBlockingQueueDemo {

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);
		queue.add("a");
		queue.add("b");
		queue.add("c");
		queue.add("d");
		queue.add("e");
		//queue.add("f"); // IllegalStateException if this queue is full
		System.out.println(queue.offer("f")); // false
		queue.put("f"); // block
		System.out.println("ok");
	}
}

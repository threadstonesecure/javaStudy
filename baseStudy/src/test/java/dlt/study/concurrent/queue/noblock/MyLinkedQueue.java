package dlt.study.concurrent.queue.noblock;

import java.util.concurrent.atomic.AtomicReference;

public class MyLinkedQueue<E> {

	private static class Node<E> {
		final E item;
		final Node<E> next;

		Node(E item, Node<E> next) {
			this.item = item;
			this.next = next;
		}
	}
	private AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(
			new Node<E>(null, null));
	private AtomicReference<Node<E>> tail = head;

	public boolean put(E item) {
		Node<E> newNode = new Node<E>(item, null);
		while (true) {
			Node<E> curTail = tail.get();
			Node<E> residue = curTail.next;
			if (curTail == tail.get()) {
				if (residue == null) /* A */{
					AtomicReference<Node<E>> arNext = new AtomicReference<Node<E>>(
							curTail.next);//这是不对的做法；AtomicReference的实质是原子进行指针切换
					if (arNext.compareAndSet(null, newNode)) /* C */{
						tail.compareAndSet(curTail, newNode) /* D */;
						return true;
					}
				} else {
					tail.compareAndSet(curTail, residue) /* B */;
				}
			}
		}
	}
}

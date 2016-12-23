package cn.fansunion.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class Consumer implements Runnable {

	private BlockingQueue<Food> queue;
	private ExecutorService exec;

	public Consumer(BlockingQueue<Food> queue, ExecutorService exec) {
		this.queue = queue;
		this.exec = exec;
	}

	@Override
	public void run() {
		while (!exec.isShutdown()) {
			try {
				Thread.sleep(2000);
				Food food = queue.take();
				System.out.println("Consumer " + food);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RejectedExecutionException e) {

			}
		}
	}

}

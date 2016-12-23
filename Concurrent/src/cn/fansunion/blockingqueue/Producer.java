package cn.fansunion.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class Producer implements Runnable {

	private BlockingQueue<Food> queue;
	private ExecutorService exec;

	public Producer(BlockingQueue<Food> queue, ExecutorService exec) {
		this.queue = queue;
		this.exec = exec;
	}

	@Override
	public void run() {
		while (!exec.isShutdown()) {
			Food food = new Food();
			try {
				Thread.sleep(4000);
				queue.put(food);
				System.out.println("Produce " + food);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RejectedExecutionException e) {

			}
		}
	}

}

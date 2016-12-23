package cn.fansunion.executorframework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

public class ExecutorFrameworkDemo {

	public static Runnable task = new Runnable() {
		@Override
		public void run() {
			System.out.println("Execute a task");
		}
	};

	// 只使用1个工作线程，执行任务
	public static void singleThreadExecutor() {
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(task);
	}

	// 拥有固定线程数目的线程池
	public static void fixedThreadPool() {
		Executor executor = Executors.newFixedThreadPool(10);
		executor.execute(task);
	}

	// 一个Executor的生命周期有三种状态，运行 ，关闭 ，终止
	public static void executorLifeCycle() {
		ExecutorService executorService = Executors.newScheduledThreadPool(10);
		while (!executorService.isShutdown()) {
			try {
				executorService.execute(task);
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();
	}

	// Future<V>代表一个异步执行的操作，通过get()方法可以获得操作的结果.
	// 如果异步操作还没有完成，则get()会使当前线程阻塞。
	// FutureTask<V>实现了Future<V>和Runable<V>。
	// Callable代表一个有返回值得操作。
	private static void futureCalable() {
		Callable<Integer> func = new Callable<Integer>() {
			public Integer call() throws Exception {
				System.out.println("inside callable");
				Thread.sleep(1000);
				Integer n = new Integer(8);
				return n;
			}
		};
		FutureTask<Integer> futureTask = new FutureTask<Integer>(func);
		Thread newThread = new Thread(futureTask);
		newThread.start();

		try {
			System.out.println("blocking here");
			Integer result = futureTask.get();
			System.out.println(result);
		} catch (InterruptedException ignored) {
		} catch (ExecutionException ignored) {
		}
	}

	public static void main(String[] args) {
		singleThreadExecutor();
		fixedThreadPool();
		executorLifeCycle();
		futureCalable();
	}
}

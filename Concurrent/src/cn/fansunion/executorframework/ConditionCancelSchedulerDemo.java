package cn.fansunion.executorframework;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//有条件地，取消调度
public class ConditionCancelSchedulerDemo {

	public static Runnable task = new Runnable() {
		@Override
		public void run() {
			System.out.println("Execute a task");
		}
	};

	// 可执行调度命令(定时+周期性)的线程池，拥有固定的线程数
	// 重复执行，无穷尽
	public static void scheduledThreadPool() {
		int initialDelay = 10;
		int period = 10;
		Executor executor = Executors.newScheduledThreadPool(10);
		ScheduledExecutorService scheduler = (ScheduledExecutorService) executor;
		scheduler.scheduleAtFixedRate(task, initialDelay, period,
				TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws Exception {
		scheduledThreadPool();
		conditionCancelScheduler();
	}

	private static void conditionCancelScheduler() throws InterruptedException {
		final String jobID = "my_job_1";
		final AtomicInteger count = new AtomicInteger(0);
		final Map<String, Future> futures = new HashMap<>();
		// 最多执行10个任务
		final int maxTaskSize = 10;
		// CountDownLatch的更多用法，请参考CountDownLatchDemo
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor();

		Future future = scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println(count.getAndIncrement());
				// 当调度执行，第maxTaskSize+1个任务的时候，取消Future中的任务。第11个任务
				if (count.get() > maxTaskSize) {
					System.out.println("a");
					Future f = futures.get(jobID);
					if (f != null) {
						f.cancel(true);
					}
					// countDownLatch中的计数器-1，变为0
					// “countDownLatch.await()”线程阻塞结束
					countDownLatch.countDown();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);

		futures.put(jobID, future);
		countDownLatch.await();

		scheduler.shutdown();
	}
}
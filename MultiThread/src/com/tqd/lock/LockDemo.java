/**
 * @FileName LockDemo.java
 * @Author tanqidong
 * @Date 2015年6月23日 下午9:39:28
 * @description
 */
package com.tqd.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tanqidong
 *
 */
public class LockDemo {

	/**	
	 * @param args
	 *@Author tanqidong
	 *@Date 2015年6月23日 下午9:39:29
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread threads[]=new Thread[10];
		for(int i=0;i<threads.length;i++){
			
			
			threads[i]=new LockThread(new PinterJob());
			threads[i].start();
		}
		
		for(int i=0;i<threads.length;i++)
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}
	static class PinterJob{
		static ReentrantLock printQueue=new ReentrantLock();
		
		public void print(){
			
			int n=(int) (Math.random()*10);
			printQueue.lock();
			System.out.println(Thread.currentThread().getName()+" start to print task using time "+n+" s");
			try {
				TimeUnit.SECONDS.sleep(n);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(Thread.currentThread().getName()+" print task is accomplished");
			printQueue.unlock();
			
		}
		
		
	}
	
	static class LockThread extends Thread{
		
		PinterJob mPinterJob
		;
		
		
		public LockThread(PinterJob mPinterJob) {
			super();
			this.mPinterJob = mPinterJob;
		}


		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mPinterJob.print();
		}
	}

}

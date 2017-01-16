/**
 * @FileName SemaphoreDemo.java
 * @Author tanqi
 * @Date 2015年7月8日 下午9:18:58
 * @description
 */
package com.tqd.c3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author tanqi
 *
 */
public class SemaphoreDemo {

	/**	
	 * @param args
	 *@Author tanqi
	 *@Date 2015年7月8日 下午9:18:58
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PrintQueue printQueue=new PrintQueue();
		
		PrintTask[] printTasks=new PrintTask[10];
//		启动打印线程
		for(int i=0;i<printTasks.length;i++){
			
			
			printTasks[i]=new PrintTask(printQueue);
			
			printTasks[i].start();
		}
		
		//等待打印线程结束
		for(int i=0;i<printTasks.length;i++){
			 
			try {
				printTasks[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	static class PrintQueue{
		
		Semaphore mSemaphore;
		
		/**
		 * 
		 */
		public PrintQueue() {
			// TODO Auto-generated constructor stub
			//
			/*第一个参数表示同时允许访问的线程数，
			 * 第一个参数可以为负数，这样就意味着，在调用acquire前，必须调用release方法
			 * 第二个参数表示是否公平，true表示公平，等待的线程按照先入先出的方式被唤醒。false表示不公平 等待的线程随机被唤醒
			 */
			mSemaphore=new Semaphore(1, false);
		}
		
		
		public void printPage(){
			
			
			 
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("开始执行打印操作...");
			int n=(int) (Math.random()*10);
			
			try {
				TimeUnit.SECONDS.sleep(n);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("打印结束，耗时:"+n+"s");
			
			mSemaphore.release();
			
		}
		
	}
	
	static class PrintTask extends Thread{
		
		PrintQueue mPrintQueue;

		public PrintTask(PrintQueue mPrintQueue) {
			 
			this.mPrintQueue = mPrintQueue;
		}
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mPrintQueue.printPage();
		}
		
	}

}

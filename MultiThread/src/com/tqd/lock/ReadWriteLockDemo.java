/**
 * @FileName ReadWriteLockDemo.java
 * @Author tanqi
 * @Date 2015年6月29日 下午10:48:27
 * @description
 */
package com.tqd.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author tanqi
 *
 */
public class ReadWriteLockDemo {

	/**
	 * @param args
	 * @Author tanqi
	 * @Date 2015年6月29日 下午10:48:27
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WriteThread wts[] = new WriteThread[10];
		ReadThread rts[] = new ReadThread[10];
		Data mData = new Data("initialized value");
		for (int i = 0; i < 10; i++) {

			wts[i] = new WriteThread(mData);
			rts[i] = new ReadThread(mData);
			// wts[i].start();
			rts[i].start();
		}
		for (int i = 0; i < 10; i++) {

			try {
				wts[i].join();
				rts[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	static class WriteThread extends Thread {

		Data mData;

		public WriteThread(Data mData) {
			super();
			this.mData = mData;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println(getName() + "start to read data");
			mData.setData("data provided by " + getName());
			System.out.println(getName() + "get data end");
		}
	}

	static class ReadThread extends Thread {

		Data mData;

		public ReadThread(Data mData) {
			super();
			this.mData = mData;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println(getName() + "start to read data");
			mData.getData();
			System.out.println(getName() + "get data end");
		}

	}

	static class Data {

		private ReadWriteLock mDataLock = new ReentrantReadWriteLock();

		private String data;

		public Data(String data) {

			this.data = data;
		}

		public String getData() {

			mDataLock.readLock().lock();
			int n = (int) (Math.random() * 10);
			System.out.println(Thread.currentThread().getName()
					+ " read lock  using time " + n);
			try {
				TimeUnit.SECONDS.sleep(n);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String temp = data;
			System.out.println("get value is " + temp);
			System.out.println(Thread.currentThread().getName()
					+ " read lock end..");
			mDataLock.readLock().unlock();
			return temp;

		}

		public void setData(String data) {
			mDataLock.writeLock().lock();
			int n = (int) (Math.random() * 10);
			System.out.println(Thread.currentThread().getName()
					+ " write lock  using time " + n);
			try {
				TimeUnit.SECONDS.sleep(n);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("set value to " + data);
			this.data = data;
			System.out.println(Thread.currentThread().getName()
					+ " write lock end");
			mDataLock.writeLock().unlock();

		}
	}
}

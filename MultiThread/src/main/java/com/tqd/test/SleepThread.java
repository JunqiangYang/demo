/**
 *FileName:SleepThread.java
 * Creator:tanqidong
 * Create Time:2015年4月30日 下午1:56:16
 * Description:
 * 
 * 
 * 
 * 
 * 
 */
package com.tqd.test;

import java.util.concurrent.TimeUnit;

/**
 * @author tanqidong
 *
 */
public class SleepThread extends Thread {

	private static final String defaultThreadName=SleepThread.class.getSimpleName();
	private boolean isRunning =true;

	/**
	 * 
	 */
	public SleepThread() {
		// TODO Auto-generated constructor stub
		super(defaultThreadName);
	}
	
	@Override
	public void run() {
			int count=0;
			while(isRunning)
			{
				System.out.println(getName()+": sleep "+count);
				
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("while I is in sleeping "+count+" someone iterrupted me");
				}
				
				count++;
				
			}
	}
}

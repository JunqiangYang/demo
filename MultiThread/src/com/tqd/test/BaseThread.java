/**
 *FileName:BaseThread.java
 * Creator:tanqidong
 * Create Time:2015骞�4鏈�30鏃� 涓嬪崍1:46:09
 * Description:
 * 
 * 
 * 
 * 
 * 
 */
package com.tqd.test;

/**
 * @author tanqidong
 *
 */
public class BaseThread  extends Thread{
		private static final String defaultThreadName="baseThread";
		private boolean isRunning =true;
 
		/**
		 * 
		 */
		public BaseThread() {
			// TODO Auto-generated constructor stub
			super(defaultThreadName);
		}
		
		@Override 
		public void run() {
				int count=0;
				
				while(isRunning)
				{
					System.out.println(getName()+": cunrrent count is "+count);
					
					if(isInterrupted())
					{
						System.out.println("current Thread "+getName()+" is interrupted");
					}
					count++;
					
				}
		}

}

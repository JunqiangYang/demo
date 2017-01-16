/**
 *FileName:JoinThread.java
 * Creator:tanqidong
 * Create Time:2015年4月30日 下午2:03:08
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
public class JoinThread  extends Thread{
	
	/**
	 * 
	 */
	public JoinThread() {
		// TODO Auto-generated constructor stub
		
		super("小明吃饭的线程");
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		
		int n=10;
		
		while(n>=0)
		{
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(getName()+" "+n);
			n--;
		}
		
		System.out.println("小明的饭吃完了");
	}
	
	public static void main(String[] args) {
		
		JoinThread jt=new JoinThread();
		JoinThread jt2=new JoinThread();
		jt.start();
		jt2.start();
//		等待小明吃饭中
		try {
			jt.join(1000, 1000);
			jt2.join(1000, 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jt.isAlive())
		{
			System.out.println("小明还没吃完");
		}
		else
		{
			System.out.println("等到小明吃完饭了啊");
		}
		
	}
}

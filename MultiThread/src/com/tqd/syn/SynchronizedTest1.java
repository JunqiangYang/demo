package com.tqd.syn;

import java.util.concurrent.TimeUnit;

public class SynchronizedTest1 {

	public static void main(String[] args) {
		Sinema sinema=new Sinema(5,5);
		// TODO Auto-generated method stub
		sallTask sallTask=new sallTask(sinema);
		Thread thread1=new Thread(sallTask);
		
		Thread thread2=new Thread(sallTask);
		long time=System.currentTimeMillis();
		thread2.start();
		thread1.start();
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("use time :"+(System.currentTimeMillis()-time));
		sinema.showInfo();
	}
	
	static class sallTask implements Runnable 
	{
		Sinema mSinema;
		public sallTask(Sinema sinema) {
			// TODO Auto-generated constructor stub
			mSinema=sinema;
		}
			public void run() {
				
				mSinema.sallTicket1(1);
				mSinema.sallTicket2(3);
				mSinema.sallTicket1(4);
				mSinema.sallTicket2(3);
				mSinema.sallTicket1(1);
				mSinema.sallTicket1(3);
				mSinema.sallTicket1(2);
				mSinema.sallTicket2(3);
				mSinema.sallTicket2(1);
				mSinema.sallTicket2(3);
			}
		 
		
	}
	
	static class Sinema{
		
		private Long ticket1;
		private Long ticket2;
		public Sinema(long t1, long t2) {
			// TODO Auto-generated constructor stub
			ticket1=t1;
			ticket2=t2;
		}
		public boolean sallTicket1(int number)
		{
			System.out.println("1--"+number);
			synchronized(ticket1)
			{
				if(number<=ticket1)
				{
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ticket1-=number;
					return true;
				
				}
			}
			return false;
			
		}
		public boolean sallTicket2(int number)
		{
			System.out.println("2--"+number);
			synchronized(ticket2)
			{
				if(number<=ticket2)
				{
					
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ticket2-=number;
					 
					return true;
					
				}
			}
			return false;
			
		}
		
		public void showInfo()
		{
			System.out.println("ticket1:"+ticket1+";"+"ticket2:"+ticket2);
		}
	}

	 
	
	

}

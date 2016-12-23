package com.tqd.test;

public class ThreadLocalTest {

	
	  static class Data{
		  
		int count  ;
		public Data() {
			// TODO Auto-generated constructor stub
			count=0;
		}
		public void addCount()
		{
			count=count+1;
		}
		
		public void showCount()
		{
			System.out.println("current count is "+count);
		}
		  
	  }
	  
	  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Data data=new Data();
		MyTask1 task=new MyTask1();
		for(int i=0;i<10;i++)
		{
			 
			Thread thread=new Thread(task, "test:"+i);
			thread.start();
			
		}
		
		data.showCount();
	}
	
	static class MyThread extends Thread
	{
		private Data mData;
		public MyThread(String name, Data data) {
			// TODO Auto-generated constructor stub
			super(name);
			mData=data;
		}
		
		
		@Override
		public void run() {
			int n=10;
			 while((n--)>0)
			 {
				if(mData!=null)
					mData.addCount();
				System.out.println(getName());
				mData.showCount();
				
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		
	}
	
	 static class MyTask implements Runnable{
		
		 private static long startDate;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startDate=System.currentTimeMillis();
			System.out.println(Thread.currentThread().getName()+" start:"+startDate);
			try {
				Thread.currentThread().sleep((long) (1000*Math.random()*10));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" end:"+startDate);
		}
		
	}
	 
	 
	 static class MyTask1 implements Runnable{
			
		 private static ThreadLocal<Long> startDate=new ThreadLocal<Long>(){
			 
			 protected Long initialValue() {
				 
				 return System.currentTimeMillis();
				 
			 };
		 };
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 
			System.out.println(Thread.currentThread().getName()+" start:"+startDate.get());
			try {
				Thread.currentThread().sleep((long) (1000*Math.random()*10));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" end:"+startDate.get());
		}
		
	}
}

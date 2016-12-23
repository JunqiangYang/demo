/**
 *FileName:DeamonThread.java
 * Creator:tanqidong
 * Create Time:2015年4月30日 下午2:23:14
 * Description:
 */
package com.tqd.test;

 
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import com.tqd.test.DeamonThread.UserThread.Event;

/**
 * @author tanqidong
 *
 */
public class DeamonThread  extends Thread{
	private boolean isRunning=true;
	private Deque<Event> events;
	/**
	 * 守护线程的优先级很低，当一个应用程序中没有其他线程（用户线程）运行的时候，守护线程才运行，它不用用来做重要的工作，
	 * 在Java中 垃圾回收线程就是典型的守护线程，当没有其他线程运行的时候，守护线程随时都可能结束
	 * @param deque 
	 */
	public DeamonThread(Deque<Event> deque) {
		// TODO Auto-generated constructor stub
		super("deamonThread");
		this.setDaemon(true);
		events=deque;
	}
	public void run() {
		
		while(isRunning)
		{
			
			long currentTime=System.currentTimeMillis();
			
			cleanEvent(currentTime);
			
		
		}
		System.out.println(getName()+"结束运行");
	};
	/**
	 * @param date
	 */
	private void cleanEvent(long time) {
		// TODO Auto-generated method stub
		
		long difference;
		boolean delete;
		if(events.size()==0)
		{
			System.out.println("no event to clean");
			return ;
		}
		delete=false;
		
		do{
			Event e=events.getLast();
			
			difference=time-e.when;
			
			if(difference>10000)
			{
				System.out.println("clean event:"+e.description);
				events.removeLast();
				delete=true;
			}
			 
			
		}while(difference>10000);
		
		if(delete)
		{
			System.out.println("the events size is "+events.size());
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Deque<Event> qq=new ArrayDeque<Event>();
		
		DeamonThread dt=new DeamonThread(qq);
		UserThread ut=new UserThread(qq);
		UserThread ut1=new UserThread(qq);
		UserThread ut2=new UserThread(qq);
		ut.start();
		ut1.start();
		ut2.start();
		dt.start();
	}
	
	static class UserThread extends Thread
	{
		public Deque<Event> eventDeque;
		/**
		 * @param deque 
		 * 
		 */
		public UserThread(Deque<Event> deque) {
			// TODO Auto-generated constructor stub
			eventDeque=deque;
		}
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 for(int i=0;i<100;i++)
			 {
				 Event e=new Event();
				 e.when=System.currentTimeMillis();
				 e.description="the event is assign by Thread "+getName();
				 System.out.println("生产第"+i+" 个 Event");
				 eventDeque.addFirst(e);
				 
				 try {
					 Thread.sleep(100);
				//	TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
			 }
			 
			 System.out.println(getName()+" 结束运行");
		}
		
		class Event{

			public String description;
			public long when;
			
			
		}
		
	}
	

}

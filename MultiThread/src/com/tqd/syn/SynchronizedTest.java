/*作用域方法*/
package com.tqd.syn;

import java.util.concurrent.TimeUnit;

public class SynchronizedTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Bank bank=new Bank(1000);
		Company company=new Company(bank);
		Comsumer comsumer=new Comsumer(bank); 
		
		company.start();
		
		comsumer.start();
		
		try {
			company.join();
			comsumer.join();
			bank.showMoney();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	static class Company extends Thread{
		private Bank mBank;
		public Company(Bank bank) {
			// TODO Auto-generated constructor stub
			super("company");
			mBank=bank;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 for(int i=0;i<10;i++)
			 {
				 mBank.addMoney();
				 
				 try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		
	}
	
	static class Comsumer extends Thread{
		private Bank mBank;
		public Comsumer(Bank bank) {
			// TODO Auto-generated constructor stub
			super("company");
			mBank=bank;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 for(int i=0;i<10;i++)
			 {
				 mBank.decreaseMoney();
				 
				 try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		
	}
	
	
	
	static class Bank{
		
		int money;
		
		public void showMoney()
		{
			System.out.println("current money is "+money);
		}
		public Bank(int initValue) {
		
			money=initValue;
			// TODO Auto-generated constructor stub
		}
		
		
		public synchronized void  addMoney(){
			
			int temp=money;
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			money=temp+100;
			
			
		}
		
		public synchronized void decreaseMoney(){
			
			int temp=money;
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			money=temp-100;
			
			
			
		}
		
	}

}

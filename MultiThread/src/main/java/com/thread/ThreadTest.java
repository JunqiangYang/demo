package com.thread;

/**
 * Created by admin on 2017-1-13 0013.
 */


public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {
        RunTask run = new RunTask() ;
        Thread t = new Thread(run);
        t.start();
        while(true){
            System.out.println(t.getState().toString());
            if("TERMINATED".equals(t.getState().toString())){
                System.out.println("=="+t.getState().toString());
                System.out.println("=="+run.isB());
                System.out.println("=========restart");
                Thread.sleep(5000);
                run.setB(true);
                t = new Thread(run);
                t.start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



    }




}

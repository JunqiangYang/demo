package com.thread;

/**
 * Created by admin on 2017-1-13 0013.
 */


public class RunTask implements  Runnable {
    private int i = 11 ;
    private boolean b = true ;

    @Override
    public void run() {
        while(b) {
            i++;
            System.out.println("=====" + i );
            if (i % 10 == 0) {
                b = false ;
                Thread.interrupted() ;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }
}

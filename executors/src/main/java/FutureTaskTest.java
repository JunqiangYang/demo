import java.util.concurrent.ExecutionException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
/**
 * Created by admin on 2016-12-21 0021.
 */


public class FutureTaskTest {

    public static void main(String[] args) {
        Callable<String> c = new Callable<String>() {
            public String call() {
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return "Callable--"+Thread.currentThread().getName();
            }
        };

        //seed a single thread
        FutureTask<String> ft1 = new FutureTask<String>(c);
        Thread t = new Thread(ft1);
        t.start();

        Runnable r = new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        FutureTask<String> ft2 = new FutureTask<String>(r, "Runnable");//give return value directly
        FutureTask<String> ft3 = new FutureTask<String>(c);
        FutureTask<String> ft4 = new FutureTask<String>(c);
        FutureTask<String> ft5 = new FutureTask<String>(c);
        FutureTask<String> ft6 = new FutureTask<String>(c);

        ExecutorService es = Executors.newFixedThreadPool(2);//init ExecutorService
        es.submit(ft2);
        es.submit(ft3);
        es.submit(ft4);
        es.submit(ft5);
        es.submit(ft6);



        try {
            TimeUnit.SECONDS.sleep(1);

            if(ft1.isDone()){
                ft4.cancel(false);

                if(ft4.isCancelled()){
                    System.out.println("task4 cancelled.");
                }
            }

            if(ft2.isDone()){
                ft5.cancel(false);

                if(ft5.isCancelled()){
                    System.out.println("task5 cancelled.");
                }
            }

            if(ft3.isDone()){
                ft6.cancel(false);

                if(ft6.isCancelled()){
                    System.out.println("task5 cancelled.");
                }
            }

            System.out.println("task1 retult:" + ft1.get());
            System.out.println("task2 retult:" + ft2.get());
            System.out.println("task3 retult:" + ft3.get());

            if(! ft4.isCancelled()){
                System.out.println("task4 retult:" + ft4.get());
            }

            if(! ft5.isCancelled()){
                System.out.println("task5 retult:" + ft5.get());
            }

            if(! ft6.isCancelled()){
                System.out.println("task6 retult:" + ft6.get());
            }

            es.shutdown();//shut down ExecutorService
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

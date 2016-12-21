import java.util.concurrent.*;

/**
 * Created by admin on 2016-12-21 0021.
 */


public class CallableFutureTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("start main thread");
        final ExecutorService exec = Executors.newFixedThreadPool(5);


        Callable<String> call = new Callable<String>() { public String call() throws Exception {
            System.out.println(" start new thread.");
            Thread.sleep(1000 * 15);
            System.out.println(" end new thread."); // call方法返回值
            return "some value.";
        }
        };
        Future<String> task = exec.submit(call);
        Thread.sleep(1000 * 2); // 阻塞当前线程，即主线程，并等待子线程结束
       while(true) {
           try {
               String s = task.get(1, TimeUnit.SECONDS);
               if(s.equals("some value.")){
                   break;
               }
               Thread.sleep(1000 * 1);
           } catch (TimeoutException e) {
               System.out.println("请求获取一次 没有拿到");
           }
       }
        exec.shutdown();
        System.out.println("end main thread");
    }

}
